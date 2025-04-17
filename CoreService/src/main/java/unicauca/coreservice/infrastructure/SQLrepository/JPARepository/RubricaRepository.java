package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.Rubrica;

public interface RubricaRepository extends JpaRepository<Rubrica, Integer> {
}
