package khomenko.pharmacy.repository;

import khomenko.pharmacy.domain.Drug;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DrugRepository extends JpaRepository<Drug, Integer> {

    List<Drug> findByVendor(String vendor);
    List<Drug> findByName(String name);

    List<Drug> findByNameContaining(String name);
    List<Drug> findByVendorContaining(String name);

}
