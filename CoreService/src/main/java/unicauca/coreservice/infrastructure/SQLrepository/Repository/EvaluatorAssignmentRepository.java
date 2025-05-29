package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import unicauca.coreservice.application.out.EvaluatorAssignmentRepositoryOutInt;
import unicauca.coreservice.application.out.IAuthenticationService;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.EvaluatorAssignment;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPAEvaluatorAssignmentRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.EvaluatorAssignmentEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectOutcomeEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.TermEntity;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.EvaluatorAssignmentMapper;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.SubjectOutcomeMapper;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.TermMapper;

import java.util.List;

@Repository
@AllArgsConstructor
public class EvaluatorAssignmentRepository implements EvaluatorAssignmentRepositoryOutInt {
    private final JPAEvaluatorAssignmentRepository evaluatorAssignmentRepository;
    private final TermRepository termRepository;
    private final IAuthenticationService authenticationService;

    @Override
    public OptionalWrapper<EvaluatorAssignment> getById(Integer id) {
        try{
            EvaluatorAssignmentEntity entity = evaluatorAssignmentRepository.findById(id)
                    .orElseThrow(() -> new NotFound("Evaluator Assignment not found"));
            
            EvaluatorAssignment evaluatorAssignment = EvaluatorAssignmentMapper.toEvaluatorAssignment(entity);
            evaluatorAssignment.setTerm(TermMapper.toTerm(entity.getTerm()));
            evaluatorAssignment.setSubjectOutcome(SubjectOutcomeMapper.toSubjectOutcome(entity.getSubjectOutcome()));
            return new OptionalWrapper<>(evaluatorAssignment);
        }catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<EvaluatorAssignment> getByEvaluatorAndSubjectOutcomeInActiveTerm(String evaluatorUid, Integer subjectOutcomeId) {
        try{
            EvaluatorAssignmentEntity entity = evaluatorAssignmentRepository.findAll().stream().
                    filter(ea -> ea.getEvaluatorUid() != null && ea.getEvaluatorUid().equals(evaluatorUid)).
                    filter(ea -> ea.getSubjectOutcome() != null && ea.getSubjectOutcome().getId().equals(subjectOutcomeId)).
                    filter(ea -> ea.getTerm() != null && termRepository.getActiveTerm().getValue().isPresent() &&
                            ea.getTerm().getId().equals(termRepository.getActiveTerm().getValue().get().getId())).
                    findFirst().orElseThrow(() -> new NotFound("Evaluator Assignment not found for the given evaluator and subject outcome in active term"));

            EvaluatorAssignment evaluatorAssignment = EvaluatorAssignmentMapper.toEvaluatorAssignment(entity);
            evaluatorAssignment.setTerm(TermMapper.toTerm(entity.getTerm()));
            evaluatorAssignment.setSubjectOutcome(SubjectOutcomeMapper.toSubjectOutcome(entity.getSubjectOutcome()));
            return new OptionalWrapper<>(evaluatorAssignment);
        }catch(Exception e){
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public List<EvaluatorAssignment> listByActiveTerm() {
        return evaluatorAssignmentRepository.findAll().stream()
                .filter(ea -> ea.getTerm() != null && termRepository.getActiveTerm().getValue().isPresent() &&
                        ea.getTerm().getId().equals(termRepository.getActiveTerm().getValue().get().getId()))
                .map(entity -> {
                    EvaluatorAssignment evaluatorAssignment = EvaluatorAssignmentMapper.toEvaluatorAssignment(entity);
                    evaluatorAssignment.setTerm(TermMapper.toTerm(entity.getTerm()));
                    evaluatorAssignment.setSubjectOutcome(SubjectOutcomeMapper.toSubjectOutcome(entity.getSubjectOutcome()));
                    return evaluatorAssignment;
                }).toList();
    }

    @Override
    public List<EvaluatorAssignment> listBySubjectOutcomeId(Integer subjectOutcomeId) {
        return evaluatorAssignmentRepository.findAll().stream()
                .filter(ea -> ea.getSubjectOutcome() != null && ea.getSubjectOutcome().getId().equals(subjectOutcomeId))
                .map(entity -> {
                    EvaluatorAssignment evaluatorAssignment = EvaluatorAssignmentMapper.toEvaluatorAssignment(entity);
                    evaluatorAssignment.setTerm(TermMapper.toTerm(entity.getTerm()));
                    evaluatorAssignment.setSubjectOutcome(SubjectOutcomeMapper.toSubjectOutcome(entity.getSubjectOutcome()));
                    return evaluatorAssignment;
                }).toList();
    }

    @Override
    public List<EvaluatorAssignment> listByEvaluatorUid(String evaluatorUid) {
        return evaluatorAssignmentRepository.findAll().stream()
                .filter(ea -> ea.getEvaluatorUid() != null && ea.getEvaluatorUid().equals(evaluatorUid))
                .map(entity -> {
                    EvaluatorAssignment evaluatorAssignment = EvaluatorAssignmentMapper.toEvaluatorAssignment(entity);
                    evaluatorAssignment.setTerm(TermMapper.toTerm(entity.getTerm()));
                    evaluatorAssignment.setSubjectOutcome(SubjectOutcomeMapper.toSubjectOutcome(entity.getSubjectOutcome()));
                    return evaluatorAssignment;
                }).toList();
    }

}
