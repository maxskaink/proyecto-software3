package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import unicauca.coreservice.application.out.SubjectCompetencyRepositoryOutInt;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.SubjectCompetency;
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
public class SubjectCompetencyRepositoryInt implements SubjectCompetencyRepositoryOutInt {
    private final JPACompetenciaProgramaRepository repositoryCompPrograma;
    private final JPACompetenciaAsignaturaRepository repositoryCompAsignatura;
    private final JPAAsignacionCompetenciaAsignaturaRepository repositoryAsignacion;
    private final JPAConfiguracionRepository repositoryConfiguracion;

    @Override
    public OptionalWrapper<SubjectCompetency> add(SubjectCompetency newSubjectCompetency) {
        try{
            newSubjectCompetency.setId(null);

            CompetenciaAsignaturaEntity newComp = CompAsignaturaMapper.toEntity(newSubjectCompetency);

            CompetenciaProgramaEntity compProg = repositoryCompPrograma.findByIdAndActivadoTrue(
                    newSubjectCompetency.getSubjectCompetencyId()
            ).orElseThrow(()->new NotFound("Competencia Programa con id " + newSubjectCompetency.getSubjectCompetencyId() + " no encontrada"));

            newComp.setCompetenciaPrograma(compProg);

            return new OptionalWrapper<>( CompAsignaturaMapper.toCompAsignatura(
                    repositoryCompAsignatura.save(newComp)
            ) );
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public List<SubjectCompetency> listAll() {
        return repositoryCompAsignatura.findAllByActivadoTrue().stream()
                .map(CompAsignaturaMapper::toCompAsignatura)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubjectCompetency> listAllBySubjectId(Integer subjectId) {
        ConfiguracionEntity conf = repositoryConfiguracion.getReferenceById(1);
        return repositoryAsignacion.findAllByAsignaturaId(subjectId).stream()
                .filter(asignacion -> Objects.equals(conf.getPeriodoActual().getId(), asignacion.getPeriodo().getId()))
                .map(AsignacionCompetenciaAsignaturaEntity::getCompetencia)
                .filter(CompetenciaAsignaturaEntity::isActivado)
                .map(CompAsignaturaMapper::toCompAsignatura)
                .toList();
    }

    @Override
    public OptionalWrapper<SubjectCompetency> getById(Integer id) {
        try{
            return new OptionalWrapper<>(CompAsignaturaMapper.toCompAsignatura(
                    repositoryCompAsignatura.findByIdAndActivadoTrue(id)
                            .orElseThrow(() -> new NotFound("Competencia Subject con id " + id + " no encontrada"))
            ));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<SubjectCompetency> update(Integer id, SubjectCompetency newSubjectCompetency) {
        try{
            CompetenciaAsignaturaEntity actualComp = repositoryCompAsignatura.findByIdAndActivadoTrue(id)
                    .orElseThrow(()-> new NotFound("Competencia Subject con id " + id + " no encontrada"));

            actualComp.setDescripcion(newSubjectCompetency.getDescription());
            actualComp.setNivel(newSubjectCompetency.getLevel());

            return new OptionalWrapper<>(
                    CompAsignaturaMapper.toCompAsignatura(repositoryCompAsignatura.save(actualComp)
                    ));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<SubjectCompetency> remove(Integer id) {
        try{
            CompetenciaAsignaturaEntity actualComp = repositoryCompAsignatura.findByIdAndActivadoTrue(id)
                    .orElseThrow(()-> new NotFound("Competencia Subject con id " + id + " no encontrada"));
            actualComp.setActivado(false);
            return new OptionalWrapper<>(
                    CompAsignaturaMapper.toCompAsignatura(repositoryCompAsignatura.save(actualComp)
                    ));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }


}
