package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.CriterionEntity;

public interface JPACriterionRepository extends JpaRepository<CriterionEntity, Integer> {

}
