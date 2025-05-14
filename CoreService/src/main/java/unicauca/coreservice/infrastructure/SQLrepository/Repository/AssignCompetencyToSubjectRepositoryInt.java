package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import unicauca.coreservice.application.out.AssignCompetencyToSubjectRepositoryOutInt;
import unicauca.coreservice.domain.exception.DuplicateInformation;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.AssignCompetencyToSubject;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.*;
import unicauca.coreservice.infrastructure.SQLrepository.entity.AsignacionCompetenciaAsignaturaEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.AsignaturaEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.CompetenciaAsignaturaEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.PeriodoEntity;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.AsignacionCompAsignaturaMapper;

import java.util.Objects;

@Repository
@AllArgsConstructor
public class AssignCompetencyToSubjectRepositoryInt implements AssignCompetencyToSubjectRepositoryOutInt {

    private final JPACompetenciaAsignaturaRepository repositoryCompAsignatura;
    private final JPAAsignacionCompetenciaAsignaturaRepository repositoryAsignacion;
    private final JPAAsignaturaRepository repositoryAsignatura;
    private final JPAPeriodoRepository repositoryPeriodo;


    @Override
    public OptionalWrapper<AssignCompetencyToSubject> add(AssignCompetencyToSubject newAsignacion) {
        try{
            newAsignacion.setId(null);

            // Verificar si ya existe una competencia con la misma descripción para esta asignatura y periodo
            boolean exists = repositoryAsignacion.findAllByAsignaturaId(newAsignacion.getSubject().getId()).stream()
                    .filter(asignacion -> asignacion.getPeriodo().getId().equals(newAsignacion.getTerm().getId()))
                    .filter(asignacion -> asignacion.isActivado())
                    .anyMatch(asignacion -> asignacion.getCompetencia().getDescripcion()
                            .equalsIgnoreCase(newAsignacion.getCompetency().getDescription()));

            if (exists)
                return new OptionalWrapper<>(new DuplicateInformation("Ya existe una competencia con esta descripción para esta asignatura en el periodo actual"));


            AsignaturaEntity asignatura =
                    repositoryAsignatura.findByIdAndActivadoTrue(
                            newAsignacion.getSubject().getId()
                    ).orElseThrow(()-> new NotFound("Subject con id " + newAsignacion.getSubject().getId() + " no encontrada"));

            CompetenciaAsignaturaEntity competencia =
                    repositoryCompAsignatura.findByIdAndActivadoTrue(
                            newAsignacion.getCompetency().getId()
                    ).orElseThrow(() -> new NotFound("Competencia con id "+ newAsignacion.getCompetency().getId()+ " no existe"));

            PeriodoEntity periodo =
                    repositoryPeriodo.getReferenceById(
                            newAsignacion.getTerm().getId()
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
    public OptionalWrapper<AssignCompetencyToSubject> getBySubjectId(Integer idCompetencia) {
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
    public OptionalWrapper<AssignCompetencyToSubject> remove(Integer idAsignacion) {
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
