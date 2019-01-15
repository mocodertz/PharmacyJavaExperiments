package khomenko.pharmacy.controller;

import khomenko.pharmacy.domain.Pharm;
import khomenko.pharmacy.domain.Stock;
import khomenko.pharmacy.repository.PharmRepository;
import khomenko.pharmacy.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("pharms")
public class PharmController {

    private final PharmRepository pharms;
    private final StockRepository stocks;

    static Integer OUT_OF_DATE_COUNT = 2;
    static Integer OUT_OF_DATE_CHARGE = Calendar.MONTH;

    static Integer OUT_OF_STOCK_AMOUNT = 100;

    @Autowired
    public PharmController(PharmRepository pharms, StockRepository stocks)
    {
        this.pharms = pharms;
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

        HashMap<Integer, Stock> drugStatuses = new HashMap<Integer, Stock>();
        for(Stock item : stockStatus) {
            if(!drugStatuses.containsKey(item.getDrugId())) {
                drugStatuses.put(item.getDrugId(), item);
            } else {
                Stock existing = drugStatuses.get(item.getDrugId());
                existing.setAmount( existing.getAmount() + item.getAmount() );
            }
        }

        stockStatus.clear();
        for(Map.Entry<Integer, Stock> entry : drugStatuses.entrySet()) {
            Stock tmpStock = entry.getValue();
            if(tmpStock.getAmount() < OUT_OF_STOCK_AMOUNT) {
                stockStatus.add(tmpStock);
            }
        }

        return stockStatus;
    }

    @PostMapping("fill/")
    public Object create(@RequestBody Stock stock) {
        return  this.stocks.save(stock);
    }

    private Date getDateOut() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(OUT_OF_DATE_CHARGE, OUT_OF_DATE_COUNT);

        return calendar.getTime();
    }

}
