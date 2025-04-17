package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.RubricaEntity;

public interface JPARubricaRepository extends JpaRepository<RubricaEntity, Integer> {
}
