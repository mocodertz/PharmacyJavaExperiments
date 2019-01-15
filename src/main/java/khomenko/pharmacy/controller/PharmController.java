package khomenko.pharmacy.controller;

import khomenko.pharmacy.domain.*;
import khomenko.pharmacy.repository.DrugRepository;
import khomenko.pharmacy.repository.PharmRepository;
import khomenko.pharmacy.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("pharms")
public class PharmController {

    private final PharmRepository pharms;
    private final StockRepository stocks;
    private final DrugRepository drugs;

    static Integer OUT_OF_DATE_COUNT = 2;
    static Integer OUT_OF_DATE_CHARGE = Calendar.MONTH;

    static Integer OUT_OF_STOCK_AMOUNT = 50;

    @Autowired
    public PharmController(PharmRepository pharms, StockRepository stocks, DrugRepository drugs)
    {
        this.pharms = pharms;
        this.drugs = drugs;
        this.stocks = stocks;
    }

    @GetMapping
    public List<Pharm> list() {
        return pharms.findAll();
    }

    @GetMapping("{id}")
    public Pharm single(@PathVariable("id") Pharm pharm) {
        return pharm;
    }

    @GetMapping("{id}/stock/")
    public List<Stock> stock(@PathVariable("id") Pharm pharm) {
        return this.stocks.findByPharmId(pharm.getId());
    }

    @GetMapping("{id}/stock/out_of_date/")
    public List<Stock> outOfDate(@PathVariable("id") Pharm pharm) {
        return this.stocks.findByUseUntilDateLessThanEqual(
            this.getDateOut()
        );
    }

    @GetMapping("{id}/stock/out_of_stock/")
    public List<Stock> outOfStock(@PathVariable("id") Pharm pharm) {

        List <Stock> stockStatus = this.stocks.findByPharmIdAndUseUntilDateGreaterThan(
            pharm.getId(), this.getDateOut()
        );

        return this.groupStockByDrugId(stockStatus).stream()
                .filter( p -> p.getAmount() < OUT_OF_STOCK_AMOUNT).collect(Collectors.toList());
    }

    @PostMapping("fill/")
    public Object create(@RequestBody Stock stock) {
        return this.stocks.save(stock);
    }

    @PostMapping("{id}/buy/")
    public Object buy(@PathVariable("id") Pharm pharm, @RequestBody Order order) {
        List<Stock> pharmStock = this.groupStockByDrugId( this.stocks.findByPharmId(pharm.getId()) );

        for(OrderItem item : order.getItems()) {

            if(item.getDrugSearchName().isEmpty()) {
                return new CheckoutResult(903, "Нельзя просто взять и не указать имя товара");
            }

            String[] nameParts = item.getDrugSearchName().split(" - ");
            List<Drug> itemDrugs = drugs.findByNameContaining(nameParts[0]);

            if(nameParts.length > 1) {
                itemDrugs = itemDrugs.stream()
                        .filter( p -> p.getVendor().contains(nameParts[1]) )
                        .collect(Collectors.toList());
            }

            switch (itemDrugs.size()) {
                case 0:
                    return new CheckoutResult(
                            904,
                            "Товара с названием \"" + item.getDrugSearchName() + "\" нет в базе"
                    );
                case 1:
                    break;
                default:
                    String message = "Найдено более одного товара с названием " + item.getDrugSearchName() + ": \n";
                    int i = 1;
                    for(Drug drugItem : itemDrugs) {
                        message += i + ". " + drugItem.getName() + " - " + drugItem.getVendor() + "\n";
                        i++;
                    }
                    return new CheckoutResult(909, message);
            }

            Integer drugId = itemDrugs.get(0).getId();
            List<Stock> drugStock = pharmStock.stream().filter( p -> p.getDrugId().equals(drugId)).collect(Collectors.toList());

            if(drugStock.size() == 0) {
                return new CheckoutResult(
                        902,
                        "К сожалению, " + item.getDrugSearchName() + " не доступен в этой аптеке"
                );
            }

            if(drugStock.get(0).getAmount() < item.getAmount()) {
                return new CheckoutResult(
                        902,
                        "К сожалению, " + item.getDrugSearchName() + " в данной аптеке только: "
                                + drugStock.get(0).getAmount()
                );
            }

        }

        return new CheckoutResult(900, "Спасибо за покупку. Поправляйтесь");
    }

    /**
     * Fresh drug date generator
     * @return Date date, when drug will be risky to use
     */
    private Date getDateOut() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(OUT_OF_DATE_CHARGE, OUT_OF_DATE_COUNT);

        return calendar.getTime();
    }

    /**
     * Stock filter. Group stock by drugId
     * @return List filtered stock
     */
    private List<Stock> groupStockByDrugId(List<Stock> stock) {
        System.out.println(stock.size());
        HashMap<Integer, Stock> drugGroups = new HashMap<Integer, Stock>();
        for(Stock item : stock) {
            if(!drugGroups.containsKey(item.getDrugId())) {
                drugGroups.put(item.getDrugId(), item);
            } else {
                Stock existing = drugGroups.get(item.getDrugId());
                existing.setAmount( existing.getAmount() + item.getAmount() );
            }
        }

        stock.clear();
        for(Map.Entry<Integer, Stock> entry : drugGroups.entrySet()) {
            stock.add(entry.getValue());
        }

        return stock;
    }

}
