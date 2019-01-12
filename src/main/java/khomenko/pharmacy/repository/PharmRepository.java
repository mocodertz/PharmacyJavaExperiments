package khomenko.pharmacy.repository;

import khomenko.pharmacy.domain.Pharm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmRepository extends JpaRepository<Pharm, Integer> {
}
