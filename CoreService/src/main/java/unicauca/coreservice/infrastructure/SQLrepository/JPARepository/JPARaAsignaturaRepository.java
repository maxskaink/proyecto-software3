package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.RAAsignaturaEntity;

import java.util.Optional;

public interface JPARaAsignaturaRepository extends JpaRepository<RAAsignaturaEntity, Integer> {
    Optional<RAAsignaturaEntity> findByIdAndActivadoTrue(Integer integer);
}
