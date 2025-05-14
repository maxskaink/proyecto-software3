package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import unicauca.coreservice.application.out.CompAsignaturaRepositoryOut;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.CompetenciaAsignatura;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPAAsignacionCompetenciaAsignaturaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPACompetenciaAsignaturaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPACompetenciaProgramaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPAConfiguracionRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.AsignacionCompetenciaAsignaturaEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.CompetenciaAsignaturaEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.CompetenciaProgramaEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.ConfiguracionEntity;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.CompAsignaturaMapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class CompAsignaturaRepository implements CompAsignaturaRepositoryOut {
    private final JPACompetenciaProgramaRepository repositoryCompPrograma;
    private final JPACompetenciaAsignaturaRepository repositoryCompAsignatura;
    private final JPAAsignacionCompetenciaAsignaturaRepository repositoryAsignacion;
    private final JPAConfiguracionRepository repositoryConfiguracion;

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
        ConfiguracionEntity conf = repositoryConfiguracion.getReferenceById(1);
        return repositoryAsignacion.findAllByAsignaturaId(idAsignatura).stream()
                .filter(asignacion -> Objects.equals(conf.getPeriodoActual().getId(), asignacion.getPeriodo().getId()))
                .map(AsignacionCompetenciaAsignaturaEntity::getCompetencia)
                .filter(CompetenciaAsignaturaEntity::isActivado)
                .map(CompAsignaturaMapper::toCompAsignatura)
                .toList();
    }

    @Override
    public OptionalWrapper<CompetenciaAsignatura> getCompetenciaById(Integer id) {
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
    public OptionalWrapper<CompetenciaAsignatura> updateCompetenciaById(Integer id, CompetenciaAsignatura newCompetenciaAsignatura) {
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


}
