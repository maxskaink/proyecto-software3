package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.AsignaturaEntity;

import java.util.Optional;

public interface JPAAsignaturaRepository extends JpaRepository<AsignaturaEntity, Integer> {
    Optional<AsignaturaEntity> findByIdAndActivadoTrue(Integer id);
}
