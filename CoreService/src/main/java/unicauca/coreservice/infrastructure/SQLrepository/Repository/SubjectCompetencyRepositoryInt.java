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
import unicauca.coreservice.infrastructure.SQLrepository.entity.AssignSubjectCompetencyEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectCompetencyEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.ProgramCompetencyEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.ConfigurationEntity;
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

            SubjectCompetencyEntity newComp = CompAsignaturaMapper.toEntity(newSubjectCompetency);

            ProgramCompetencyEntity compProg = repositoryCompPrograma.findByIdAndActivadoTrue(
                    newSubjectCompetency.getSubjectCompetencyId()
            ).orElseThrow(()->new NotFound("Competencia Programa con id " + newSubjectCompetency.getSubjectCompetencyId() + " no encontrada"));

            newComp.setProgramCompetency(compProg);

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
        ConfigurationEntity conf = repositoryConfiguracion.getReferenceById(1);
        return repositoryAsignacion.findAllBySubjectId(subjectId).stream()
                .filter(asignacion -> Objects.equals(conf.getActiveTerm().getId(), asignacion.getTerm().getId()))
                .map(AssignSubjectCompetencyEntity::getCompetency)
                .filter(SubjectCompetencyEntity::isIsActivated)
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
            SubjectCompetencyEntity actualComp = repositoryCompAsignatura.findByIdAndActivadoTrue(id)
                    .orElseThrow(()-> new NotFound("Competencia Subject con id " + id + " no encontrada"));

            actualComp.setDescription(newSubjectCompetency.getDescription());
            actualComp.setLevel(newSubjectCompetency.getLevel());

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
            SubjectCompetencyEntity actualComp = repositoryCompAsignatura.findByIdAndActivadoTrue(id)
                    .orElseThrow(()-> new NotFound("Competencia Subject con id " + id + " no encontrada"));
            actualComp.setIsActivated(false);
            return new OptionalWrapper<>(
                    CompAsignaturaMapper.toCompAsignatura(repositoryCompAsignatura.save(actualComp)
                    ));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }


}
