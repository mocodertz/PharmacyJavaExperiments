package khomenko.pharmacy.controller;

import khomenko.pharmacy.domain.Drug;
import khomenko.pharmacy.repository.DrugRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("drug")
public class DrugController {

    private final DrugRepository drugs;

    @Autowired
    public DrugController(DrugRepository drugs) {
        this.drugs = drugs;
    }

    @GetMapping
    public List list() {
        return drugs.findAll();
    }

    @GetMapping("{id}")
    public Drug getDrug(@PathVariable("id") Drug drug) {
        return drug;
    }

    @PostMapping
    public Object create(@RequestBody Drug drug) {
        return drugs.save(drug);
    }

    @GetMapping("vendor/{vendor}")
    public List<Drug> byVendor(@PathVariable("vendor") String vendor) {
        return drugs.findByVendor(vendor);
    }

    @GetMapping("search/{name}")
    public List<Drug> byName(@PathVariable("name") String name) {
        return drugs.findByName(name);
    }
}
