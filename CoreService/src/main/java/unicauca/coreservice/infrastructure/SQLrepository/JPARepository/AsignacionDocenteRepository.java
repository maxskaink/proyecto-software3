package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.AsignacionDocente;

public interface AsignacionDocenteRepository extends JpaRepository<AsignacionDocente, Integer> {
}
