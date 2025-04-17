package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.CompetenciaPrograma;

public interface CompetenciaProgramaRepository extends JpaRepository<CompetenciaPrograma, Integer> {
}
