package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import unicauca.coreservice.application.out.SubjectCompetencyRepositoryOutInt;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.SubjectCompetency;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPASubjectCompetencyAssignmentRepository;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPASubjectCompetencyRepository;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPAProgramCompetencyRepository;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPAConfigurationRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectCompetencyAssignmentEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectCompetencyEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.ProgramCompetencyEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.ConfigurationEntity;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.SubjectCompetencyMapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class SubjectCompetencyRepositoryInt implements SubjectCompetencyRepositoryOutInt {
    private final JPAProgramCompetencyRepository programCompetencyRepository;
    private final JPASubjectCompetencyRepository subjectCompetencyRepository;
    private final JPASubjectCompetencyAssignmentRepository subjectCompetencyAssignmentRepository;
    private final JPAConfigurationRepository configurationRepository;

    @Override
    public OptionalWrapper<SubjectCompetency> add(SubjectCompetency newSubjectCompetency) {
        try{
            newSubjectCompetency.setId(null);

            SubjectCompetencyEntity newComp = SubjectCompetencyMapper.toSubjectCompetencyEntity(newSubjectCompetency);

            ProgramCompetencyEntity compProg = programCompetencyRepository.findActiveProgramCompetencyById(
                    newSubjectCompetency.getProgramCompetencyId()
            ).orElseThrow(()->new NotFound("Program competency with id " + newSubjectCompetency.getProgramCompetencyId() + " was not found"));

            newComp.setProgramCompetency(compProg);

            return new OptionalWrapper<>( SubjectCompetencyMapper.toSubjectCompetency(
                    subjectCompetencyRepository.save(newComp)
            ) );
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public List<SubjectCompetency> listAll() {
        return subjectCompetencyRepository.findByIsActivatedTrue().stream()
                .map(SubjectCompetencyMapper::toSubjectCompetency)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubjectCompetency> listAllBySubjectId(Integer subjectId) {
        ConfigurationEntity conf = configurationRepository.getReferenceById(1);
        return subjectCompetencyAssignmentRepository.findAllBySubjectId(subjectId).stream()
                .filter(assignment -> Objects.equals(conf.getActiveTerm().getId(), assignment.getTerm().getId()))
                .map(SubjectCompetencyAssignmentEntity::getCompetency)
                .filter(SubjectCompetencyEntity::isActivated)
                .map(SubjectCompetencyMapper::toSubjectCompetency)
                .toList();
    }

    @Override
    public OptionalWrapper<SubjectCompetency> getById(Integer id) {
        try{
            return new OptionalWrapper<>(SubjectCompetencyMapper.toSubjectCompetency(
                    subjectCompetencyRepository.findByIdAndIsActivatedTrue(id)
                            .orElseThrow(() -> new NotFound("Subject competency with id " + id + " was not found"))
            ));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<SubjectCompetency> update(Integer id, SubjectCompetency newSubjectCompetency) {
        try{
            SubjectCompetencyEntity actualComp = subjectCompetencyRepository.findByIdAndIsActivatedTrue(id)
                    .orElseThrow(()-> new NotFound("Subject competency with id " + id + " was not found"));

            actualComp.setDescription(newSubjectCompetency.getDescription());
            actualComp.setLevel(newSubjectCompetency.getLevel());

            return new OptionalWrapper<>(
                    SubjectCompetencyMapper.toSubjectCompetency(subjectCompetencyRepository.save(actualComp)
                    ));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<SubjectCompetency> remove(Integer id) {
        try{
            SubjectCompetencyEntity actualComp = subjectCompetencyRepository.findByIdAndIsActivatedTrue(id)
                    .orElseThrow(()-> new NotFound("Subject competency with id " + id + " was not found"));
            actualComp.setActivated(false);
            return new OptionalWrapper<>(
                    SubjectCompetencyMapper.toSubjectCompetency(subjectCompetencyRepository.save(actualComp)
                    ));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }


}
