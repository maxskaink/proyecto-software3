package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.CompetenciaAsignaturaEntity;

public interface JPACompetenciaAsignaturaRepository extends JpaRepository<CompetenciaAsignaturaEntity, Integer> {
}
