package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import unicauca.coreservice.application.out.SubjectOutcomeRepositoryOutInt;
import unicauca.coreservice.domain.exception.DuplicateInformation;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.SubjectOutcome;
import unicauca.coreservice.domain.model.Term;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.*;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectCompetencyAssignmentEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.ConfigurationEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectOutcomeEntity;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.SubjectOutcomeMapper;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class SubjectOutcomeRepository implements SubjectOutcomeRepositoryOutInt {

    private final JPASubjectCompetencyAssignmentRepository assignmentRepository;
    private final JPASubjectOutcomeRepository subjectOutcomeRepository;
    private final JPAConfigurationRepository configurationRepository;
    private final JPATermRepository termRepository;

    @Override
    public OptionalWrapper<SubjectOutcome> add(SubjectOutcome newSubjectOutcome, Integer competencyAssignment) {
        try{
            newSubjectOutcome.setId(null);
            newSubjectOutcome.setRubric(null);
            SubjectCompetencyAssignmentEntity assignment =
                    assignmentRepository.findByIdAndIsActivatedTrue(competencyAssignment)
                            .orElseThrow(()-> new NotFound("Competency assignment with id " + competencyAssignment + " was not found"));
            Integer idActualTerm = configurationRepository.getReferenceById(1).getActiveTerm().getId();
            // verify if exists a learning outcome with the same description for this competency in this term
            boolean exists = assignment.getSubjectOutcomes().stream()
                    .filter(SubjectOutcomeEntity::isActivated)
                    .anyMatch(ra -> ra.getDescription().equalsIgnoreCase(newSubjectOutcome.getDescription())&&
                            ra.getCompetencyAssignment().getTerm().getId().equals(idActualTerm));

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
                .peek(entity -> {
                    if(entity.getRubric() != null && !entity.getRubric().isActivated())
                        entity.setRubric(null);
                })
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
    public List<SubjectOutcome> listAllBySubjectIdAndTermId(Integer subjectId, Integer idTerm) {
        return assignmentRepository.findAllBySubjectId(subjectId).stream()
                .filter(assignment -> Objects.equals(idTerm, assignment.getTerm().getId()))
                .flatMap(assignment -> assignment.getSubjectOutcomes().stream())
                .filter(SubjectOutcomeEntity::isActivated)
                .peek(entity -> {
                    if(entity.getRubric() != null && !entity.getRubric().isActivated())
                        entity.setRubric(null);
                })
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
                .peek(entity -> {
                    if(entity.getRubric() != null && !entity.getRubric().isActivated())
                        entity.setRubric(null);
                })
                .map(SubjectOutcomeMapper::toSubjectOutcome)
                .toList();
    }

    @Override
    public OptionalWrapper<SubjectOutcome> getById(Integer id) {
        try{
            SubjectOutcomeEntity entity = subjectOutcomeRepository.findActiveSubjectOutcomeById(id)
                    .orElseThrow(() -> new NotFound("SubjectOutcome with id " + id + " was not found"));

            if(entity.getRubric() != null && !entity.getRubric().isActivated())
                entity.setRubric(null);

            return new OptionalWrapper<>(SubjectOutcomeMapper.toSubjectOutcome(entity));
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
