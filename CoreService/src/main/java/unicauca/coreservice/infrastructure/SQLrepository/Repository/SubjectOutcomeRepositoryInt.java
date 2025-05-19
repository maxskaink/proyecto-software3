package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import unicauca.coreservice.application.out.SubjectOutcomeRepositoryOutInt;
import unicauca.coreservice.domain.exception.DuplicateInformation;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.SubjectOutcome;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.*;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectCompetencyAssignmentEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.ConfigurationEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectOutcomeEntity;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.SubjectOutcomeMapper;

import java.util.List;
import java.util.Objects;

@Repository
@AllArgsConstructor
public class SubjectOutcomeRepositoryInt implements SubjectOutcomeRepositoryOutInt {

    private final JPASubjectCompetencyAssignmentRepository assignmentRepository;
    private final JPASubjectOutcomeRepository subjectOutcomeRepository;
    private final JPAConfigurationRepository configurationRepository;

    @Override
    public OptionalWrapper<SubjectOutcome> add(SubjectOutcome newSubjectOutcome, Integer competencyAssignment) {
        try{
            SubjectCompetencyAssignmentEntity assignment =
                    assignmentRepository.findByIdAndIsActivatedTrue(competencyAssignment)
                            .orElseThrow(()-> new NotFound("Competency assignment with id " + competencyAssignment + " was not found"));

            // verify if exists a learning outcome with the same description for this competency in this term
            boolean exists = assignment.getSubjectOutcomes().stream()
                    .filter(SubjectOutcomeEntity::isActivated)
                    .anyMatch(ra -> ra.getDescription().equalsIgnoreCase(newSubjectOutcome.getDescription()));

            if (exists)
                return new OptionalWrapper<>(new DuplicateInformation("There is an outcome with this description in the competency " + assignment.getCompetency().getDescription() + " added in the active Term"));

            SubjectOutcomeEntity newRa = SubjectOutcomeMapper.toSubjectOutcomeEntity(newSubjectOutcome);

            newRa.setCompetencyAssignment(assignment);

            return new OptionalWrapper<>(
                    SubjectOutcomeMapper.toSubjectOutcome(
                            subjectOutcomeRepository.save(newRa)
                    )
            );

        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public List<SubjectOutcome> listAllBySubjectId(Integer subjectId, boolean activeTerm) {
        if (activeTerm){
            ConfigurationEntity conf = configurationRepository.getReferenceById(1);
            return assignmentRepository.findAllBySubjectId(subjectId).stream()
                .filter(assignment -> Objects.equals(conf.getActiveTerm().getId(), assignment.getTerm().getId()))
                .flatMap(assignment -> assignment.getSubjectOutcomes().stream())
                .filter(SubjectOutcomeEntity::isActivated)
                .map(SubjectOutcomeMapper::toSubjectOutcome)
                .toList();
        }
        else
            return assignmentRepository.findAllBySubjectId(subjectId).stream()
                    .flatMap(assignment -> assignment.getSubjectOutcomes().stream())
                    .filter(SubjectOutcomeEntity::isActivated)
                    .map(SubjectOutcomeMapper::toSubjectOutcome)
                    .toList();
    }

    @Override
    public List<SubjectOutcome> listAllByCompetencyId(Integer competencyId) {
        ConfigurationEntity conf = configurationRepository.getReferenceById(1);

        return assignmentRepository.findAllByCompetencyId(competencyId).stream()
                .filter(assignment -> Objects.equals(conf.getActiveTerm().getId(), assignment.getTerm().getId()))
                .flatMap(assignment -> assignment.getSubjectOutcomes().stream())
                .filter(SubjectOutcomeEntity::isActivated)
                .map(SubjectOutcomeMapper::toSubjectOutcome)
                .toList();
    }

    @Override
    public OptionalWrapper<SubjectOutcome> getById(Integer id) {
        try{
            return new OptionalWrapper<>(SubjectOutcomeMapper.toSubjectOutcome(
                    subjectOutcomeRepository.findActiveSubjectOutcomeById(id)
                            .orElseThrow(() -> new NotFound("SubjectOutcome with id " + id + " was not found"))
            ));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<SubjectOutcome> update(Integer id, SubjectOutcome newSubjectOutcome) {
        try{
            SubjectOutcomeEntity actual = subjectOutcomeRepository.findActiveSubjectOutcomeById(id)
                    .orElseThrow(() -> new NotFound("SubjectOutcome with id " + id + " was not found"));
            actual.setDescription(newSubjectOutcome.getDescription());

            return new OptionalWrapper<>( SubjectOutcomeMapper.toSubjectOutcome(
                    subjectOutcomeRepository.save(actual)
            ));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<SubjectOutcome> remove(Integer id) {
        try{
            SubjectOutcomeEntity actual = subjectOutcomeRepository.findActiveSubjectOutcomeById(id)
                    .orElseThrow(() -> new NotFound("SubjectOutcome with id " + id + " was not found"));

            int amountOutcomes = actual.getCompetencyAssignment().getSubjectOutcomes().stream()
                    .filter(SubjectOutcomeEntity::isActivated).toList().size();

            if( amountOutcomes== 1)
                throw new DataIntegrityViolationException("Competency with id "+ actual.getCompetencyAssignment().getCompetency().getId() +
                        " must be have, at least one active outcome");

            actual.setActivated(false);
            return new OptionalWrapper<>( SubjectOutcomeMapper.toSubjectOutcome(
                    subjectOutcomeRepository.save(actual)
            ));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }
}
