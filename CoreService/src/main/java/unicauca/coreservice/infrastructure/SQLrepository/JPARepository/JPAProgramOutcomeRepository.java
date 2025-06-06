package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.ProgramOutcomeEntity;

import java.util.List;
import java.util.Optional;

public interface JPAProgramOutcomeRepository extends JpaRepository<ProgramOutcomeEntity, Integer> {
    List<ProgramOutcomeEntity> findByIsActivatedTrue();
    Optional<ProgramOutcomeEntity> findByIdAndIsActivatedTrue(Integer integer);
}
