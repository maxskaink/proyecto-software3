package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.Criterio;

public interface CriterioRepository extends JpaRepository<Criterio, Integer> {
}
