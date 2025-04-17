package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.CompetenciaAsignatura;

public interface CompetenciaAsignaturaRepository extends JpaRepository<CompetenciaAsignatura, Integer> {
}
