package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.CompetenciaProgramaEntity;

import java.util.List;
import java.util.Optional;

public interface JPACompetenciaProgramaRepository extends JpaRepository<CompetenciaProgramaEntity, Integer> {
    List<CompetenciaProgramaEntity> findAllByActivadoTrue();
    Optional<CompetenciaProgramaEntity> findByIdAndActivadoTrue(Integer integer);
}
