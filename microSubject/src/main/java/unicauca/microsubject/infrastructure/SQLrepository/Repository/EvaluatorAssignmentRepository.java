package unicauca.microsubject.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import unicauca.microsubject.application.out.EvaluatorAssignmentRepositoryOutInt;
import unicauca.microsubject.application.out.IAuthenticationService;
import unicauca.microsubject.domain.exception.NotFound;
import unicauca.microsubject.domain.model.EvaluatorAssignment;
import unicauca.microsubject.domain.model.OptionalWrapper;
import unicauca.microsubject.infrastructure.SQLrepository.JPARepository.JPAEvaluatorAssignmentRepository;
import unicauca.microsubject.infrastructure.SQLrepository.entity.EvaluatorAssignmentEntity;
import unicauca.microsubject.infrastructure.SQLrepository.entity.SubjectOutcomeEntity;
import unicauca.microsubject.infrastructure.SQLrepository.entity.TermEntity;
import unicauca.microsubject.infrastructure.SQLrepository.mapper.EvaluatorAssignmentMapper;
import unicauca.microsubject.infrastructure.SQLrepository.mapper.SubjectOutcomeMapper;
import unicauca.microsubject.infrastructure.SQLrepository.mapper.TermMapper;
import java.util.List;

@Repository
@AllArgsConstructor
public class EvaluatorAssignmentRepository implements EvaluatorAssignmentRepositoryOutInt {
    private final JPAEvaluatorAssignmentRepository evaluatorAssignmentRepository;
    private final  TermRepository termRepository;
    private final IAuthenticationService authenticationService;


    @Override
    public OptionalWrapper<EvaluatorAssignment> add(EvaluatorAssignment newEvaluatorAssignment) {
        try {
            newEvaluatorAssignment.setId(null);

            if (!authenticationService.userExists(newEvaluatorAssignment.getEvaluatorUid()))
                throw new NotFound("Teacher does not exist");
                
            EvaluatorAssignmentEntity entity = EvaluatorAssignmentMapper.toEvaluatorAssignmentEntity(newEvaluatorAssignment);

            TermEntity termEntity = TermMapper.toTermEntity(newEvaluatorAssignment.getTerm());
            SubjectOutcomeEntity subjectOutcomeEntity = SubjectOutcomeMapper.toSubjectOutcomeEntity(newEvaluatorAssignment.getSubjectOutcome());

            entity.setTerm(termEntity);
            entity.setSubjectOutcome(subjectOutcomeEntity);

            EvaluatorAssignmentEntity savedEntity = evaluatorAssignmentRepository.save(entity);
            EvaluatorAssignment savedEvaluatorAssignment = EvaluatorAssignmentMapper.toEvaluatorAssignment(savedEntity);

            savedEvaluatorAssignment.setTerm(newEvaluatorAssignment.getTerm());
            savedEvaluatorAssignment.setSubjectOutcome(newEvaluatorAssignment.getSubjectOutcome());
            return new OptionalWrapper<>(savedEvaluatorAssignment);
        }catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

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

    @Override
    public OptionalWrapper<EvaluatorAssignment> remove(Integer id) {
        try {
            EvaluatorAssignmentEntity entity = evaluatorAssignmentRepository.findById(id)
                    .orElseThrow(() -> new NotFound("Evaluator Assignment not found"));

            evaluatorAssignmentRepository.delete(entity);
            EvaluatorAssignment evaluatorAssignment = EvaluatorAssignmentMapper.toEvaluatorAssignment(entity);
            evaluatorAssignment.setTerm(TermMapper.toTerm(entity.getTerm()));
            evaluatorAssignment.setSubjectOutcome(SubjectOutcomeMapper.toSubjectOutcome(entity.getSubjectOutcome()));
            return new OptionalWrapper<>(evaluatorAssignment);
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<EvaluatorAssignment> removeByEvaluatorAndSubjectOutcomeInActiveTerm(String evaluatorUid, Integer subjectOutcomeId) {
       try {
            EvaluatorAssignmentEntity entity = evaluatorAssignmentRepository.findAll().stream()
                    .filter(ea -> ea.getEvaluatorUid() != null && ea.getEvaluatorUid().equals(evaluatorUid))
                    .filter(ea -> ea.getSubjectOutcome() != null && ea.getSubjectOutcome().getId().equals(subjectOutcomeId))
                    .filter(ea -> ea.getTerm() != null && termRepository.getActiveTerm().getValue().isPresent() &&
                            ea.getTerm().getId().equals(termRepository.getActiveTerm().getValue().get().getId()))
                    .findFirst()
                    .orElseThrow(() -> new NotFound("Evaluator Assignment not found for the given evaluator and subject outcome in active term"));

            evaluatorAssignmentRepository.delete(entity);
            EvaluatorAssignment evaluatorAssignment = EvaluatorAssignmentMapper.toEvaluatorAssignment(entity);
            evaluatorAssignment.setTerm(TermMapper.toTerm(entity.getTerm()));
            evaluatorAssignment.setSubjectOutcome(SubjectOutcomeMapper.toSubjectOutcome(entity.getSubjectOutcome()));
            return new OptionalWrapper<>(evaluatorAssignment);
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }
}
