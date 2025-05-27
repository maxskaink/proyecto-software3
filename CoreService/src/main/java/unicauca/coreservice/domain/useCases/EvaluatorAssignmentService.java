package unicauca.coreservice.domain.useCases;

import lombok.AllArgsConstructor;
import unicauca.coreservice.application.in.EvaluatorAssignmentInt;
import unicauca.coreservice.application.out.EvaluatorAssignmentRepositoryOutInt;
import unicauca.coreservice.application.out.SubjectOutcomeRepositoryOutInt;
import unicauca.coreservice.application.out.TermRepositoryOutInt;
import unicauca.coreservice.domain.model.EvaluatorAssignment;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.SubjectOutcome;
import unicauca.coreservice.domain.model.Term;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class EvaluatorAssignmentService implements EvaluatorAssignmentInt {
    private final EvaluatorAssignmentRepositoryOutInt evaluatorAssignmentRepository;
    private final TermRepositoryOutInt termRepository;
    private final SubjectOutcomeRepositoryOutInt subjectOutcomeRepository;

    @Override
    public EvaluatorAssignment add(String evaluatorUid, Integer subjectId) throws Exception {
        OptionalWrapper<EvaluatorAssignment> evaluatorAssignmentWrapper = evaluatorAssignmentRepository.getByEvaluatorAndSubjectOutcomeInActiveTerm(evaluatorUid, subjectId);
        Optional<EvaluatorAssignment> evaluatorAssignmentOpt = evaluatorAssignmentWrapper.getValue();
        if (evaluatorAssignmentOpt.isPresent()) {
            throw new Exception("Evaluator assignment already exists for this evaluator and subject in the active term.");
        }

        Optional<Term> activeTerm = termRepository.getActiveTerm().getValue();
        if (activeTerm.isEmpty()) {
            throw termRepository.getActiveTerm().getException();
        }

        Optional<SubjectOutcome> subjectOutcome = subjectOutcomeRepository.getById(subjectId).getValue();
        if(subjectOutcome.isEmpty())
        {
            throw subjectOutcomeRepository.getById(subjectId).getException();
        }
        
        EvaluatorAssignment evaluatorAssignment = new EvaluatorAssignment(null, activeTerm.get(), subjectOutcome.get(), evaluatorUid);
        OptionalWrapper<EvaluatorAssignment> response = evaluatorAssignmentRepository.add(evaluatorAssignment);
        return response.getValue().orElseThrow(response::getException);
    }

    @Override
    public EvaluatorAssignment getById(Integer id) throws Exception {
        OptionalWrapper<EvaluatorAssignment> response = evaluatorAssignmentRepository.getById(id);
        return response.getValue().orElseThrow(response::getException);
    }

    @Override
    public EvaluatorAssignment getByEvaluatorAndSubjectInActiveTerm(String evaluatorUid, Integer subjectId) throws Exception {
        OptionalWrapper<EvaluatorAssignment> response = evaluatorAssignmentRepository.getByEvaluatorAndSubjectOutcomeInActiveTerm(evaluatorUid, subjectId);
        return response.getValue().orElseThrow(response::getException);
    }

    @Override
    public List<EvaluatorAssignment> listEvaluatorAssignmentsInActiveTerm() {
        return evaluatorAssignmentRepository.listByActiveTerm();
    }

    @Override
    public List<EvaluatorAssignment> listBySubjectId(Integer subjectId) {
        return evaluatorAssignmentRepository.listBySubjectOutcomeId(subjectId);
    }

    @Override
    public List<EvaluatorAssignment> listByEvaluatorUid(String evaluatorUid) {
        return evaluatorAssignmentRepository.listByEvaluatorUid(evaluatorUid);
    }

    @Override
    public EvaluatorAssignment remove(Integer id) throws Exception {
        OptionalWrapper<EvaluatorAssignment> response = evaluatorAssignmentRepository.remove(id);
        return response.getValue().orElseThrow(response::getException);
    }

    @Override
    public EvaluatorAssignment removeByEvaluatorAndSubjectInActiveTerm(String evaluatorUid, Integer subjectId) throws Exception {
        OptionalWrapper<EvaluatorAssignment> response = evaluatorAssignmentRepository.removeByEvaluatorAndSubjectOutcomeInActiveTerm(evaluatorUid, subjectId);
        return response.getValue().orElseThrow(response::getException);
    }
}
