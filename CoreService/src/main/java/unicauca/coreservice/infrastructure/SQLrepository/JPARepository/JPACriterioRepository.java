package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.CriterioEntity;

public interface JPACriterioRepository extends JpaRepository<CriterioEntity, Integer> {
}
