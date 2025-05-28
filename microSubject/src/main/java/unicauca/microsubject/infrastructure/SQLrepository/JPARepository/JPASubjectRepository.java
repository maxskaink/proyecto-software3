package unicauca.microsubject.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.microsubject.infrastructure.SQLrepository.entity.SubjectEntity;

import java.util.List;
import java.util.Optional;

public interface JPASubjectRepository extends JpaRepository<SubjectEntity, Integer> {
    Optional<SubjectEntity> findByIdAndIsActivatedTrue(Integer id);
    List<SubjectEntity> findAllByIsActivatedTrue();
    Optional<SubjectEntity> findByName(String name);
}
