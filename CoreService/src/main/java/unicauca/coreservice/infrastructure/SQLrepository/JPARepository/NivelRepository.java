package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.Nivel;

public interface NivelRepository extends JpaRepository<Nivel, Integer> {
}
