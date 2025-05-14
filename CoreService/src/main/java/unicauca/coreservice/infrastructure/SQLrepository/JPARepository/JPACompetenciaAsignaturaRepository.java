package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectCompetencyEntity;

import java.util.List;
import java.util.Optional;

public interface JPACompetenciaAsignaturaRepository extends JpaRepository<SubjectCompetencyEntity, Integer> {
    List<SubjectCompetencyEntity> findAllByActivadoTrue();
    Optional<SubjectCompetencyEntity> findByIdAndActivadoTrue(Integer integer);
}
