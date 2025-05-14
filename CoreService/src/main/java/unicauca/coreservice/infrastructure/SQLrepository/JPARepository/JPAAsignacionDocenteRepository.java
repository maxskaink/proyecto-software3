package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.AssignTeacherEntity;

public interface JPAAsignacionDocenteRepository extends JpaRepository<AssignTeacherEntity, Integer> {
}
