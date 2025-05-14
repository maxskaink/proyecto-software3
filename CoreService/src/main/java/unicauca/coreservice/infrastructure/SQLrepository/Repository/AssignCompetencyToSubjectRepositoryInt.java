package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Repository;
import unicauca.coreservice.application.out.AsignacionCompAsignaturaRepositoryOut;
import unicauca.coreservice.domain.exception.DuplicateInformation;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.AsignacionCompetenciaAsignatura;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.*;
import unicauca.coreservice.infrastructure.SQLrepository.entity.AsignacionCompetenciaAsignaturaEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.AsignaturaEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.CompetenciaAsignaturaEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.PeriodoEntity;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.AsignacionCompAsignaturaMapper;

import java.util.List;
import java.util.Objects;

@Repository
@AllArgsConstructor
public class AsignacionCompAsignaturaRepository implements AsignacionCompAsignaturaRepositoryOut {

    private final JPACompetenciaAsignaturaRepository repositoryCompAsignatura;
    private final JPAAsignacionCompetenciaAsignaturaRepository repositoryAsignacion;
    private final JPAAsignaturaRepository repositoryAsignatura;
    private final JPAPeriodoRepository repositoryPeriodo;


    @Override
    public OptionalWrapper<AsignacionCompetenciaAsignatura> addAsignacionCompetenciaAsignatura(AsignacionCompetenciaAsignatura newAsignacion) {
        try{
            newAsignacion.setId(null);

            // Verificar si ya existe una competencia con la misma descripción para esta asignatura y periodo
            boolean exists = repositoryAsignacion.findAllByAsignaturaId(newAsignacion.getAsignatura().getId()).stream()
                    .filter(asignacion -> asignacion.getPeriodo().getId().equals(newAsignacion.getPeriodo().getId()))
                    .filter(asignacion -> asignacion.isActivado())
                    .anyMatch(asignacion -> asignacion.getCompetencia().getDescripcion()
                            .equalsIgnoreCase(newAsignacion.getCompetencia().getDescripcion()));

            if (exists)
                return new OptionalWrapper<>(new DuplicateInformation("Ya existe una competencia con esta descripción para esta asignatura en el periodo actual"));


            AsignaturaEntity asignatura =
                    repositoryAsignatura.findByIdAndActivadoTrue(
                            newAsignacion.getAsignatura().getId()
                    ).orElseThrow(()-> new NotFound("Asignatura con id " + newAsignacion.getAsignatura().getId() + " no encontrada"));

            CompetenciaAsignaturaEntity competencia =
                    repositoryCompAsignatura.findByIdAndActivadoTrue(
                            newAsignacion.getCompetencia().getId()
                    ).orElseThrow(() -> new NotFound("Competencia con id "+ newAsignacion.getCompetencia().getId()+ " no existe"));

            PeriodoEntity periodo =
                    repositoryPeriodo.getReferenceById(
                            newAsignacion.getPeriodo().getId()
                    );

            AsignacionCompetenciaAsignaturaEntity finalAsignacion =
                    AsignacionCompetenciaAsignaturaEntity.builder()
                            .asignatura(asignatura)
                            .competencia(competencia)
                            .periodo(periodo)
                            .build();

            return new OptionalWrapper<>(
                    AsignacionCompAsignaturaMapper.toAsignacionCompAsignatura(
                            repositoryAsignacion.save(finalAsignacion)
                    ));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<AsignacionCompetenciaAsignatura> findByIdCompetencia(Integer idCompetencia) {
        try{
            AsignacionCompetenciaAsignaturaEntity asignacion =
                    repositoryAsignacion.findAllByCompetenciaId(idCompetencia).stream()
                            .filter(a -> Objects.equals(a.getCompetencia().getId(), idCompetencia))
                            .findFirst().orElseThrow(()-> new NotFound("No se encuentra una asignacion de la competencia con id " + idCompetencia));
            return new OptionalWrapper<>(AsignacionCompAsignaturaMapper.toAsignacionCompAsignatura(asignacion));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<AsignacionCompetenciaAsignatura> deleteById(Integer idAsignacion) {
        try{
            AsignacionCompetenciaAsignaturaEntity acutalEntity = repositoryAsignacion.getReferenceById(idAsignacion);

            if(!acutalEntity.isActivado())
                throw new NotFound("Asignacion con id " + idAsignacion + " no encontrada");

            acutalEntity.setActivado(false);

            return new OptionalWrapper<>(AsignacionCompAsignaturaMapper.toAsignacionCompAsignatura(repositoryAsignacion.save(acutalEntity)));

        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

}
