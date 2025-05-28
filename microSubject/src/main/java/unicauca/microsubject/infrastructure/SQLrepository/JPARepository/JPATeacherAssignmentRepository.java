package unicauca.microsubject.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.microsubject.infrastructure.SQLrepository.entity.TeacherAssignmentEntity;

public interface JPATeacherAssignmentRepository extends JpaRepository<TeacherAssignmentEntity, Integer> {
}
