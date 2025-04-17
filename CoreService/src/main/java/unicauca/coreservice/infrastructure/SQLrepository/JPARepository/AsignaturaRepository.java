package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.Asignatura;

public interface AsignaturaRepository extends JpaRepository<Asignatura, Integer> {
}
