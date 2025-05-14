package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.LevelEntity;

public interface JPANivelRepository extends JpaRepository<LevelEntity, Integer> {
}
