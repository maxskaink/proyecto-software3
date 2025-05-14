package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectOutcomeEntity;

import java.util.Optional;

public interface JPARaAsignaturaRepository extends JpaRepository<SubjectOutcomeEntity, Integer> {
    Optional<SubjectOutcomeEntity> findByIdAndActivadoTrue(Integer integer);
}
