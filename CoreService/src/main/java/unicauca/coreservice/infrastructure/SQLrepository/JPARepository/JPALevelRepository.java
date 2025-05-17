package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.LevelEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectEntity;

import java.util.List;
import java.util.Optional;

public interface JPALevelRepository extends JpaRepository<LevelEntity, Integer> {
    Optional<LevelEntity> findActiveLevelById(Integer id);
    List<LevelEntity> findByIsActivatedTrue();
}
