package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import unicauca.coreservice.application.out.SubjectOutcomeRepositoryOutInt;
import unicauca.coreservice.domain.exception.DuplicateInformation;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.SubjectOutcome;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.*;
import unicauca.coreservice.infrastructure.SQLrepository.entity.AsignacionCompetenciaAsignaturaEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.ConfiguracionEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.RAAsignaturaEntity;
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
            AsignacionCompetenciaAsignaturaEntity asignacion =
                    repositoryAsignacion.getReferenceById(asignacionCompetencia);

            // Verificar si ya existe un RA con la misma descripción para esta asignación
            boolean exists = asignacion.getRAAsignaturas().stream()
                    .filter(RAAsignaturaEntity::isActivado)
                    .anyMatch(ra -> ra.getDescripcion().equalsIgnoreCase(newSubjectOutcome.getDescription()));

            if (exists)
                return new OptionalWrapper<>(new DuplicateInformation("Ya existe un RA con esta descripción para la competencia " + asignacion.getCompetencia().getDescripcion() + " en el periodo actual"));

            RAAsignaturaEntity newRa = RAAsignaturaMapper.toEntity(newSubjectOutcome);

            newRa.setAsignacionCompetencia(asignacion);

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

        ConfiguracionEntity conf = repositoryConfiguracion.getReferenceById(1);

        return repositoryAsignacion.findAllByAsignaturaId(subjectId).stream()
                .filter(asignacion -> Objects.equals(conf.getPeriodoActual().getId(), asignacion.getPeriodo().getId()))
                .flatMap(asignacion -> asignacion.getRAAsignaturas().stream())
                .filter(RAAsignaturaEntity::isActivado)
                .map(RAAsignaturaMapper::toRAAsignatura)
                .toList();
    }

    @Override
    public List<SubjectOutcome> listAllByCompetencyId(Integer competencyId) {
        ConfiguracionEntity conf = repositoryConfiguracion.getReferenceById(1);

        return repositoryAsignacion.findAllByCompetenciaId(competencyId).stream()
                .filter(asignacion -> Objects.equals(conf.getPeriodoActual().getId(), asignacion.getPeriodo().getId()))
                .flatMap(asignacion -> asignacion.getRAAsignaturas().stream())
                .filter(RAAsignaturaEntity::isActivado)
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
            RAAsignaturaEntity actual = repositoryRaAsignatura.findByIdAndActivadoTrue(id)
                    .orElseThrow(() -> new NotFound("SubjectOutcome con id " + id + " no encontrada"));
            actual.setDescripcion(newSubjectOutcome.getDescription());

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
            RAAsignaturaEntity actual = repositoryRaAsignatura.findByIdAndActivadoTrue(id)
                    .orElseThrow(() -> new NotFound("SubjectOutcome con id " + id + " no encontrada"));
            actual.setActivado(false);
            return new OptionalWrapper<>( RAAsignaturaMapper.toRAAsignatura(
                    repositoryRaAsignatura.save(actual)
            ));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }
}
