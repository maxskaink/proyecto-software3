package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.TeacherAssignmentEntity;

public interface JPATeacherAssignmentRepository extends JpaRepository<TeacherAssignmentEntity, Integer> {
}
