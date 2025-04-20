package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.domain.model.CompetenciaAsignatura;
import unicauca.coreservice.infrastructure.SQLrepository.entity.CompetenciaAsignaturaEntity;

import java.util.List;
import java.util.Optional;

public interface JPACompetenciaAsignaturaRepository extends JpaRepository<CompetenciaAsignaturaEntity, Integer> {
    List<CompetenciaAsignaturaEntity> findAllByActivadoTrue();
    Optional<CompetenciaAsignaturaEntity> findByIdAndActivadoTrue(Integer integer);
}
