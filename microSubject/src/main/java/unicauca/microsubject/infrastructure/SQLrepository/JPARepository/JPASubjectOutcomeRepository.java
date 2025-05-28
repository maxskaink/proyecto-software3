package unicauca.microsubject.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.microsubject.infrastructure.SQLrepository.entity.SubjectOutcomeEntity;

import java.util.Optional;

public interface JPASubjectOutcomeRepository extends JpaRepository<SubjectOutcomeEntity, Integer> {
    Optional<SubjectOutcomeEntity> findActiveSubjectOutcomeById(Integer integer);
}
