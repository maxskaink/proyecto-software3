package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.RubricEntity;

public interface JPARubricaRepository extends JpaRepository<RubricEntity, Integer> {
}
