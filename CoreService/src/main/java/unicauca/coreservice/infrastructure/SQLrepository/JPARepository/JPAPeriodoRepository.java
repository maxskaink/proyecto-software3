package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.PeriodoEntity;

public interface JPAPeriodoRepository extends JpaRepository<PeriodoEntity, Integer> {

}
