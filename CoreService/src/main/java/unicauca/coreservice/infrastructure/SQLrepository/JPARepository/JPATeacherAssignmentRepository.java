package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.TeacherAssignmentEntity;

import java.util.Optional;

public interface JPATeacherAssignmentRepository extends JpaRepository<TeacherAssignmentEntity, Integer> {
    Optional<TeacherAssignmentEntity> findActiveTeacherById(Long teacherId);
}
