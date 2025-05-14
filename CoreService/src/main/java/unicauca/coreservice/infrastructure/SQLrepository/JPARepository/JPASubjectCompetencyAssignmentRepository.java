package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectCompetencyAssignmentEntity;

import java.util.List;

public interface JPASubjectCompetencyAssignmentRepository extends JpaRepository<SubjectCompetencyAssignmentEntity, Integer> {
    List<SubjectCompetencyAssignmentEntity> findAllBySubjectId(Integer subjectId);
    List<SubjectCompetencyAssignmentEntity> findAllByCompetencyId(Integer competencyId);
}
