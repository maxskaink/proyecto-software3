package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.ProgramCompetencyEntity;

import java.util.List;
import java.util.Optional;

public interface JPACompetenciaProgramaRepository extends JpaRepository<ProgramCompetencyEntity, Integer> {
    List<ProgramCompetencyEntity> findAllByActivadoTrue();
    Optional<ProgramCompetencyEntity> findByIdAndActivadoTrue(Integer integer);
}
