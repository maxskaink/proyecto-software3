package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import unicauca.coreservice.application.out.RaAsignaturaRepositoryOut;
import unicauca.coreservice.domain.exception.DuplicateInformation;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.RAAsignatura;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.*;
import unicauca.coreservice.infrastructure.SQLrepository.entity.AsignacionCompetenciaAsignaturaEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.ConfiguracionEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.RAAsignaturaEntity;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.RAAsignaturaMapper;

import java.util.List;
import java.util.Objects;

@Repository
@AllArgsConstructor
public class RaAsignaturaRepository implements RaAsignaturaRepositoryOut {

    private final JPAAsignacionCompetenciaAsignaturaRepository repositoryAsignacion;
    private final JPARaAsignaturaRepository repositoryRaAsignatura;
    private final JPAConfiguracionRepository repositoryConfiguracion;

    @Override
    public OptionalWrapper<RAAsignatura> addRAAsignatura(RAAsignatura newRAAsignatura, Integer asignacionCompetencia) {
        try{
            AsignacionCompetenciaAsignaturaEntity asignacion =
                    repositoryAsignacion.getReferenceById(asignacionCompetencia);

            // Verificar si ya existe un RA con la misma descripción para esta asignación
            boolean exists = asignacion.getRAAsignaturas().stream()
                    .filter(RAAsignaturaEntity::isActivado)
                    .anyMatch(ra -> ra.getDescripcion().equalsIgnoreCase(newRAAsignatura.getDescripcion()));

            if (exists)
                return new OptionalWrapper<>(new DuplicateInformation("Ya existe un RA con esta descripción para la competencia " + asignacion.getCompetencia().getDescripcion() + " en el periodo actual"));

            RAAsignaturaEntity newRa = RAAsignaturaMapper.toEntity(newRAAsignatura);

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
    public List<RAAsignatura> listRAAsignatura(Integer idAsignatura) {

        ConfiguracionEntity conf = repositoryConfiguracion.getReferenceById(1);

        return repositoryAsignacion.findAllByAsignaturaId(idAsignatura).stream()
                .filter(asignacion -> Objects.equals(conf.getPeriodoActual().getId(), asignacion.getPeriodo().getId()))
                .flatMap(asignacion -> asignacion.getRAAsignaturas().stream())
                .filter(RAAsignaturaEntity::isActivado)
                .map(RAAsignaturaMapper::toRAAsignatura)
                .toList();
    }

    @Override
    public List<RAAsignatura> listRAAsignaturaByCompetencia(Integer idCompetencia) {
        ConfiguracionEntity conf = repositoryConfiguracion.getReferenceById(1);

        return repositoryAsignacion.findAllByCompetenciaId(idCompetencia).stream()
                .filter(asignacion -> Objects.equals(conf.getPeriodoActual().getId(), asignacion.getPeriodo().getId()))
                .flatMap(asignacion -> asignacion.getRAAsignaturas().stream())
                .filter(RAAsignaturaEntity::isActivado)
                .map(RAAsignaturaMapper::toRAAsignatura)
                .toList();
    }

    @Override
    public OptionalWrapper<RAAsignatura> getByIdRAAsignatura(Integer id) {
        try{
            return new OptionalWrapper<>(RAAsignaturaMapper.toRAAsignatura(
                    repositoryRaAsignatura.findByIdAndActivadoTrue(id)
                            .orElseThrow(() -> new NotFound("RAAsignatura con id " + id + " no encontrada"))
            ));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<RAAsignatura> updateByIdRAAsignatura(Integer id, RAAsignatura newRAAsignatura) {
        try{
            RAAsignaturaEntity actual = repositoryRaAsignatura.findByIdAndActivadoTrue(id)
                    .orElseThrow(() -> new NotFound("RAAsignatura con id " + id + " no encontrada"));
            actual.setDescripcion(newRAAsignatura.getDescripcion());

            return new OptionalWrapper<>( RAAsignaturaMapper.toRAAsignatura(
                    repositoryRaAsignatura.save(actual)
            ));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<RAAsignatura> removeRAAsignatura(Integer id) {
        try{
            RAAsignaturaEntity actual = repositoryRaAsignatura.findByIdAndActivadoTrue(id)
                    .orElseThrow(() -> new NotFound("RAAsignatura con id " + id + " no encontrada"));
            actual.setActivado(false);
            return new OptionalWrapper<>( RAAsignaturaMapper.toRAAsignatura(
                    repositoryRaAsignatura.save(actual)
            ));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }
}
