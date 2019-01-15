package khomenko.pharmacy.repository;

import khomenko.pharmacy.domain.Pharm;
import khomenko.pharmacy.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Integer> {

    List<Stock> findByPharmId(Integer id);
    List<Stock> findByUseUntilDateLessThanEqual(Date date);
    List<Stock> findByPharmIdAndUseUntilDateGreaterThan(Integer id, Date date);

}
