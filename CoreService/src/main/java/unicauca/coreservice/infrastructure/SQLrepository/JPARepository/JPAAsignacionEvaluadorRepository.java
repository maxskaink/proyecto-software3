package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.AsignacionEvaluadorEntity;

public interface JPAAsignacionEvaluadorRepository extends JpaRepository<AsignacionEvaluadorEntity, Integer> {
}
