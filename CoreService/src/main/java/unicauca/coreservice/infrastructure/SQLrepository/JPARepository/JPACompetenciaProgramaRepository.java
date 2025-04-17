package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.CompetenciaProgramaEntity;

public interface JPACompetenciaProgramaRepository extends JpaRepository<CompetenciaProgramaEntity, Integer> {
}
