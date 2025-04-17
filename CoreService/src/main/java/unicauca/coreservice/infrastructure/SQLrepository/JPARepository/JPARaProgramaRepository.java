package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.RAProgramaEntity;

public interface JPARaProgramaRepository extends JpaRepository<RAProgramaEntity, Integer> {
}
