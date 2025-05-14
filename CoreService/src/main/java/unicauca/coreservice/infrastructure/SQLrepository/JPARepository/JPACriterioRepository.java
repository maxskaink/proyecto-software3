package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.CriterionEntity;

public interface JPACriterioRepository extends JpaRepository<CriterionEntity, Integer> {
}
