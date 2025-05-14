package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.AssignSubjectCompetencyEntity;

import java.util.List;

public interface JPAAsignacionCompetenciaAsignaturaRepository extends JpaRepository<AssignSubjectCompetencyEntity, Integer> {
    List<AssignSubjectCompetencyEntity> findAllBySubjectId(Integer subjectId);
    List<AssignSubjectCompetencyEntity> findAllByCompetencyId(Integer competencyId);
}
