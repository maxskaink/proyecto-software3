package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import unicauca.coreservice.application.out.AssignCompetencyToSubjectRepositoryOutInt;
import unicauca.coreservice.domain.exception.DuplicateInformation;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.AssignCompetencyToSubject;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.*;
import unicauca.coreservice.infrastructure.SQLrepository.entity.AssignSubjectCompetencyEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectCompetencyEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.TermEntity;
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
            boolean exists = repositoryAsignacion.findAllBySubjectId(newAsignacion.getSubject().getId()).stream()
                    .filter(asignacion -> asignacion.getTerm().getId().equals(newAsignacion.getTerm().getId()))
                    .filter(asignacion -> asignacion.isIsActivated())
                    .anyMatch(asignacion -> asignacion.getCompetency().getDescription()
                            .equalsIgnoreCase(newAsignacion.getCompetency().getDescription()));

            if (exists)
                return new OptionalWrapper<>(new DuplicateInformation("Ya existe una competencia con esta descripción para esta asignatura en el periodo actual"));


            SubjectEntity asignatura =
                    repositoryAsignatura.findByIdAndActivadoTrue(
                            newAsignacion.getSubject().getId()
                    ).orElseThrow(()-> new NotFound("Subject con id " + newAsignacion.getSubject().getId() + " no encontrada"));

            SubjectCompetencyEntity competencia =
                    repositoryCompAsignatura.findByIdAndActivadoTrue(
                            newAsignacion.getCompetency().getId()
                    ).orElseThrow(() -> new NotFound("Competencia con id "+ newAsignacion.getCompetency().getId()+ " no existe"));

            TermEntity periodo =
                    repositoryPeriodo.getReferenceById(
                            newAsignacion.getTerm().getId()
                    );

            AssignSubjectCompetencyEntity finalAsignacion =
                    AssignSubjectCompetencyEntity.builder()
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
            AssignSubjectCompetencyEntity asignacion =
                    repositoryAsignacion.findAllByCompetencyId(idCompetencia).stream()
                            .filter(a -> Objects.equals(a.getCompetency().getId(), idCompetencia))
                            .findFirst().orElseThrow(()-> new NotFound("No se encuentra una asignacion de la competencia con id " + idCompetencia));
            return new OptionalWrapper<>(AsignacionCompAsignaturaMapper.toAsignacionCompAsignatura(asignacion));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<AssignCompetencyToSubject> remove(Integer idAsignacion) {
        try{
            AssignSubjectCompetencyEntity acutalEntity = repositoryAsignacion.getReferenceById(idAsignacion);

            if(!acutalEntity.isIsActivated())
                throw new NotFound("Asignacion con id " + idAsignacion + " no encontrada");

            acutalEntity.setIsActivated(false);

            return new OptionalWrapper<>(AsignacionCompAsignaturaMapper.toAsignacionCompAsignatura(repositoryAsignacion.save(acutalEntity)));

        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

}
