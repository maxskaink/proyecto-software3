package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.RAAsignatura;

public interface RaAsignaturaRepository extends JpaRepository<RAAsignatura, Integer> {
}
