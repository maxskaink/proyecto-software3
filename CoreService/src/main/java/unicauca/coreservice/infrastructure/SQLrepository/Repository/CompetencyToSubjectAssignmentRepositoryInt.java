package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import unicauca.coreservice.application.out.CompetencyToSubjectAssignmentRepositoryOutInt;
import unicauca.coreservice.domain.exception.DuplicateInformation;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.CompetencyToSubjectAssignment;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.*;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectCompetencyAssignmentEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectCompetencyEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.TermEntity;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.AsignacionCompAsignaturaMapper;

import java.util.Objects;

@Repository
@AllArgsConstructor
public class CompetencyToSubjectAssignmentRepositoryInt implements CompetencyToSubjectAssignmentRepositoryOutInt {

    private final JPASubjectCompetencyRepository repositoryCompAsignatura;
    private final JPASubjectCompetencyAssignmentRepository repositoryAsignacion;
    private final JPASubjectRepository repositoryAsignatura;
    private final JPATermRepository repositoryPeriodo;


    @Override
    public OptionalWrapper<CompetencyToSubjectAssignment> add(CompetencyToSubjectAssignment newAsignacion) {
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
                    repositoryAsignatura.findActiveSubjectById(
                            newAsignacion.getSubject().getId()
                    ).orElseThrow(()-> new NotFound("Subject con id " + newAsignacion.getSubject().getId() + " no encontrada"));

            SubjectCompetencyEntity competencia =
                    repositoryCompAsignatura.findActiveSubjectCompetencyById(
                            newAsignacion.getCompetency().getId()
                    ).orElseThrow(() -> new NotFound("Competencia con id "+ newAsignacion.getCompetency().getId()+ " no existe"));

            TermEntity periodo =
                    repositoryPeriodo.getReferenceById(
                            newAsignacion.getTerm().getId()
                    );

            SubjectCompetencyAssignmentEntity finalAsignacion =
                    SubjectCompetencyAssignmentEntity.builder()
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
    public OptionalWrapper<CompetencyToSubjectAssignment> getBySubjectId(Integer idCompetencia) {
        try{
            SubjectCompetencyAssignmentEntity asignacion =
                    repositoryAsignacion.findAllByCompetencyId(idCompetencia).stream()
                            .filter(a -> Objects.equals(a.getCompetency().getId(), idCompetencia))
                            .findFirst().orElseThrow(()-> new NotFound("No se encuentra una asignacion de la competencia con id " + idCompetencia));
            return new OptionalWrapper<>(AsignacionCompAsignaturaMapper.toAsignacionCompAsignatura(asignacion));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<CompetencyToSubjectAssignment> remove(Integer idAsignacion) {
        try{
            SubjectCompetencyAssignmentEntity acutalEntity = repositoryAsignacion.getReferenceById(idAsignacion);

            if(!acutalEntity.isIsActivated())
                throw new NotFound("Asignacion con id " + idAsignacion + " no encontrada");

            acutalEntity.setIsActivated(false);

            return new OptionalWrapper<>(AsignacionCompAsignaturaMapper.toAsignacionCompAsignatura(repositoryAsignacion.save(acutalEntity)));

        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

}
