package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.AsignacionCompetenciaAsignaturaEntity;

import java.util.List;

public interface JPAAsignacionCompetenciaAsignaturaRepository extends JpaRepository<AsignacionCompetenciaAsignaturaEntity, Integer> {
    List<AsignacionCompetenciaAsignaturaEntity> findAllByAsignaturaId(Integer asignaturaId);
}
