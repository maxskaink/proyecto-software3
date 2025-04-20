package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import unicauca.coreservice.application.out.CompAndRAAsignaturaRepositoryOut;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.AsignacionCompetenciaAsignatura;
import unicauca.coreservice.domain.model.CompetenciaAsignatura;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.RAAsignatura;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.*;
import unicauca.coreservice.infrastructure.SQLrepository.entity.*;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.AsignacionCompAsignaturaMapper;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.CompAsignaturaMapper;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.RAAsignaturaMapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class CompAndRaAsignaturaRepository implements CompAndRAAsignaturaRepositoryOut {

    private final JPACompetenciaProgramaRepository repositoryCompPrograma;
    private final JPACompetenciaAsignaturaRepository repositoryCompAsignatura;
    private final JPAAsignacionCompetenciaAsignaturaRepository repositoryAsignacion;
    private final JPARaAsignaturaRepository repositoryRaAsignatura;
    private final JPAConfiguracionRepository repositoryConfiguracion;
    private final JPAAsignaturaRepository repositoryAsignatura;
    private final JPAPeriodoRepository repositoryPeriodo;


    @Override
    public OptionalWrapper<CompetenciaAsignatura> addCompetenciaAsignatura(CompetenciaAsignatura newCompetenciaAsignatura) {
        try{
            newCompetenciaAsignatura.setId(null);

            CompetenciaAsignaturaEntity newComp = CompAsignaturaMapper.toEntity(newCompetenciaAsignatura);

            CompetenciaProgramaEntity compProg = repositoryCompPrograma.findByIdAndActivadoTrue(
                    newCompetenciaAsignatura.getIdCompetenciaPrograma()
            ).orElseThrow(()->new NotFound("Competencia Programa con id " + newCompetenciaAsignatura.getIdCompetenciaPrograma() + " no encontrada"));

            newComp.setCompetenciaPrograma(compProg);

            return new OptionalWrapper<>( CompAsignaturaMapper.toCompAsignatura(
                    repositoryCompAsignatura.save(newComp)
            ) );
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public List<CompetenciaAsignatura> listCompetenciaAsignatura() {
        return repositoryCompAsignatura.findAllByActivadoTrue().stream()
                .map(CompAsignaturaMapper::toCompAsignatura)
                .collect(Collectors.toList());
    }

    @Override
    public List<CompetenciaAsignatura> listCompetenciaAsignatura(Integer idAsignatura) {
        return repositoryAsignacion.findAllByAsignaturaId(idAsignatura).stream()
                .map(AsignacionCompetenciaAsignaturaEntity::getCompetencia)
                .filter(CompetenciaAsignaturaEntity::isActivado)
                .map(CompAsignaturaMapper::toCompAsignatura)
                .toList();
    }

    @Override
    public OptionalWrapper<CompetenciaAsignatura> getById(Integer id) {
        try{
            return new OptionalWrapper<>(CompAsignaturaMapper.toCompAsignatura(
                    repositoryCompAsignatura.findByIdAndActivadoTrue(id)
                        .orElseThrow(() -> new NotFound("Competencia Asignatura con id " + id + " no encontrada"))
            ));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<CompetenciaAsignatura> updateById(Integer id, CompetenciaAsignatura newCompetenciaAsignatura) {
        try{
            CompetenciaAsignaturaEntity actualComp = repositoryCompAsignatura.findByIdAndActivadoTrue(id)
                    .orElseThrow(()-> new NotFound("Competencia Asignatura con id " + id + " no encontrada"));

            actualComp.setDescripcion(newCompetenciaAsignatura.getDescripcion());
            actualComp.setNivel(newCompetenciaAsignatura.getNivel());

            return new OptionalWrapper<>(
                    CompAsignaturaMapper.toCompAsignatura(repositoryCompAsignatura.save(actualComp)
                    ));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<CompetenciaAsignatura> removeCompetenciaAsignatura(Integer id) {
        try{
            CompetenciaAsignaturaEntity actualComp = repositoryCompAsignatura.findByIdAndActivadoTrue(id)
                    .orElseThrow(()-> new NotFound("Competencia Asignatura con id " + id + " no encontrada"));
            actualComp.setActivado(false);
            return new OptionalWrapper<>(
                    CompAsignaturaMapper.toCompAsignatura(repositoryCompAsignatura.save(actualComp)
                    ));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<AsignacionCompetenciaAsignatura> addAsignacionCompetenciaAsignatura(AsignacionCompetenciaAsignatura asignacion) {
        try{
            asignacion.setId(null);

            AsignaturaEntity asignatura =
                    repositoryAsignatura.findByIdAndActivadoTrue(
                            asignacion.getAsignatura().getId()
                    ).orElseThrow(()-> new NotFound("Asignatura con id " + asignacion.getAsignatura().getId() + " no encontrada"));

            CompetenciaAsignaturaEntity competencia =
                    repositoryCompAsignatura.findByIdAndActivadoTrue(
                            asignacion.getCompetencia().getId()
                    ).orElseThrow(() -> new NotFound("Competencia con id "+ asignacion.getCompetencia().getId()+ " no existe"));

            PeriodoEntity periodo =
                    repositoryPeriodo.getReferenceById(
                            asignacion.getPeriodo().getId()
                    );

            AsignacionCompetenciaAsignaturaEntity newAsignacion =
                    AsignacionCompetenciaAsignaturaEntity.builder()
                        .asignatura(asignatura)
                        .competencia(competencia)
                        .periodo(periodo)
                        .build();

            return new OptionalWrapper<>(
                    AsignacionCompAsignaturaMapper.toAsignacionCompAsignatura(
                            repositoryAsignacion.save(newAsignacion)
                    ));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<RAAsignatura> addRAAsignatura(RAAsignatura newRAAsignatura, Integer asignacionCompetencia) {
        try{
            AsignacionCompetenciaAsignaturaEntity asignacion =
                    repositoryAsignacion.getReferenceById(asignacionCompetencia);

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
