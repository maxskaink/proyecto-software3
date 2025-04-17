package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.AsignaturaEntity;

public interface JPAAsignaturaRepository extends JpaRepository<AsignaturaEntity, Integer> {
}
