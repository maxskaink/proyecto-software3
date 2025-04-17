package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.Periodo;

public interface PeriodoRepository extends JpaRepository<Periodo, Integer> {

}
