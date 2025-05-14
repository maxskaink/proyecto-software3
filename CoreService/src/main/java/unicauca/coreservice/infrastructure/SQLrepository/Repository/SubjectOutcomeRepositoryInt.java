package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import unicauca.coreservice.application.out.SubjectOutcomeRepositoryOutInt;
import unicauca.coreservice.domain.exception.DuplicateInformation;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.SubjectOutcome;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.*;
import unicauca.coreservice.infrastructure.SQLrepository.entity.AssignSubjectCompetencyEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.ConfigurationEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectOutcomeEntity;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.RAAsignaturaMapper;

import java.util.List;
import java.util.Objects;

@Repository
@AllArgsConstructor
public class SubjectOutcomeRepositoryInt implements SubjectOutcomeRepositoryOutInt {

    private final JPAAsignacionCompetenciaAsignaturaRepository repositoryAsignacion;
    private final JPARaAsignaturaRepository repositoryRaAsignatura;
    private final JPAConfiguracionRepository repositoryConfiguracion;

    @Override
    public OptionalWrapper<SubjectOutcome> add(SubjectOutcome newSubjectOutcome, Integer asignacionCompetencia) {
        try{
            AssignSubjectCompetencyEntity asignacion =
                    repositoryAsignacion.getReferenceById(asignacionCompetencia);

            // Verificar si ya existe un RA con la misma descripción para esta asignación
            boolean exists = asignacion.getSubjectOutcomes().stream()
                    .filter(SubjectOutcomeEntity::isIsActivated)
                    .anyMatch(ra -> ra.getDescription().equalsIgnoreCase(newSubjectOutcome.getDescription()));

            if (exists)
                return new OptionalWrapper<>(new DuplicateInformation("Ya existe un RA con esta descripción para la competencia " + asignacion.getCompetency().getDescription() + " en el periodo actual"));

            SubjectOutcomeEntity newRa = RAAsignaturaMapper.toEntity(newSubjectOutcome);

            newRa.setCompetencyAssignment(asignacion);

            return new OptionalWrapper<>(
                    RAAsignaturaMapper.toRAAsignatura(
                            repositoryRaAsignatura.save(newRa)
                    )
            );

        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public List<SubjectOutcome> listAllBySubjectId(Integer subjectId) {

        ConfigurationEntity conf = repositoryConfiguracion.getReferenceById(1);

        return repositoryAsignacion.findAllBySubjectId(subjectId).stream()
                .filter(asignacion -> Objects.equals(conf.getActiveTerm().getId(), asignacion.getTerm().getId()))
                .flatMap(asignacion -> asignacion.getSubjectOutcomes().stream())
                .filter(SubjectOutcomeEntity::isIsActivated)
                .map(RAAsignaturaMapper::toRAAsignatura)
                .toList();
    }

    @Override
    public List<SubjectOutcome> listAllByCompetencyId(Integer competencyId) {
        ConfigurationEntity conf = repositoryConfiguracion.getReferenceById(1);

        return repositoryAsignacion.findAllByCompetencyId(competencyId).stream()
                .filter(asignacion -> Objects.equals(conf.getActiveTerm().getId(), asignacion.getTerm().getId()))
                .flatMap(asignacion -> asignacion.getSubjectOutcomes().stream())
                .filter(SubjectOutcomeEntity::isIsActivated)
                .map(RAAsignaturaMapper::toRAAsignatura)
                .toList();
    }

    @Override
    public OptionalWrapper<SubjectOutcome> getBySubjectId(Integer id) {
        try{
            return new OptionalWrapper<>(RAAsignaturaMapper.toRAAsignatura(
                    repositoryRaAsignatura.findByIdAndActivadoTrue(id)
                            .orElseThrow(() -> new NotFound("SubjectOutcome con id " + id + " no encontrada"))
            ));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<SubjectOutcome> update(Integer id, SubjectOutcome newSubjectOutcome) {
        try{
            SubjectOutcomeEntity actual = repositoryRaAsignatura.findByIdAndActivadoTrue(id)
                    .orElseThrow(() -> new NotFound("SubjectOutcome con id " + id + " no encontrada"));
            actual.setDescription(newSubjectOutcome.getDescription());

            return new OptionalWrapper<>( RAAsignaturaMapper.toRAAsignatura(
                    repositoryRaAsignatura.save(actual)
            ));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<SubjectOutcome> remove(Integer id) {
        try{
            SubjectOutcomeEntity actual = repositoryRaAsignatura.findByIdAndActivadoTrue(id)
                    .orElseThrow(() -> new NotFound("SubjectOutcome con id " + id + " no encontrada"));
            actual.setIsActivated(false);
            return new OptionalWrapper<>( RAAsignaturaMapper.toRAAsignatura(
                    repositoryRaAsignatura.save(actual)
            ));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }
}
