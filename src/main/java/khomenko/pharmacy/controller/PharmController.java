package khomenko.pharmacy.controller;

import khomenko.pharmacy.domain.Pharm;
import khomenko.pharmacy.domain.Stock;
import khomenko.pharmacy.repository.PharmRepository;
import khomenko.pharmacy.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("pharms")
public class PharmController {

    private final PharmRepository pharms;
    private final StockRepository stocks;

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
    public List<Stock> outOfStock(@PathVariable("id") Pharm pharm) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 2);

        return this.stocks.findByUseUntilDateLessThanEqual( calendar.getTime() );
    }

    @GetMapping("{id}/stock/out_of_stock/")
    public List<Stock> outOfDate(@PathVariable("id") Pharm pharm) {
        return this.stocks.findByPharmId(pharm.getId());
    }

    @PostMapping("fill/")
    public Object create(@RequestBody Stock stock) {
        return  this.stocks.save(stock);
    }

}
