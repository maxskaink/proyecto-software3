package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.RAProgramaEntity;

import java.util.List;
import java.util.Optional;

public interface JPARaProgramaRepository extends JpaRepository<RAProgramaEntity, Integer> {
    List<RAProgramaEntity> findAllByActivadoTrue();
    Optional<RAProgramaEntity> findByIdAndActivadoTrue(Integer integer);
}
