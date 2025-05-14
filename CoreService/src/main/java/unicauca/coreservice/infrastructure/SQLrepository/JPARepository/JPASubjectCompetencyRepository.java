package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectCompetencyEntity;

import java.util.List;
import java.util.Optional;

public interface JPASubjectCompetencyRepository extends JpaRepository<SubjectCompetencyEntity, Integer> {
    List<SubjectCompetencyEntity> findAllActiveSubjectCompetencies();
    Optional<SubjectCompetencyEntity> findActiveSubjectCompetencyById(Integer integer);
}
