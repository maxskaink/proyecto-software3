package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.TermEntity;

public interface JPAPeriodoRepository extends JpaRepository<TermEntity, Integer> {

}
