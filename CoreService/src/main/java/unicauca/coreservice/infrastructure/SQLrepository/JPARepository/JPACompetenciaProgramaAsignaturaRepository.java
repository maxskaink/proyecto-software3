package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.CompetenciaProgramaAsignaturaEntity;

public interface JPACompetenciaProgramaAsignaturaRepository extends JpaRepository<CompetenciaProgramaAsignaturaEntity, Integer> {
}
