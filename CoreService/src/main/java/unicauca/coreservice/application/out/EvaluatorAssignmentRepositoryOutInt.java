package unicauca.coreservice.application.out;

import java.util.List;

import unicauca.coreservice.domain.model.EvaluatorAssignment;
import unicauca.coreservice.domain.model.OptionalWrapper;

public interface EvaluatorAssignmentRepositoryOutInt {

    /**
     * Retrieves an EvaluatorAssignment by its ID.
     *
     * @param id the ID of the EvaluatorAssignment
     * @return an OptionalWrapper containing the EvaluatorAssignment if found, or empty if not found
     */
    OptionalWrapper<EvaluatorAssignment> getById(Integer id);

    /**
     * Retrieves an EvaluatorAssignment by evaluator UID and subject outcome ID in the active term.
     *
     * @param evaluatorUid the UID of the evaluator
     * @param subjectOutcomeId the ID of the subject outcome
     * @return an OptionalWrapper containing the EvaluatorAssignment if found, or empty if not found
     */
    OptionalWrapper<EvaluatorAssignment> getByEvaluatorAndSubjectOutcomeInActiveTerm(
            String evaluatorUid, Integer subjectOutcomeId);

    /**
     * Lists all EvaluatorAssignments for the active term.
     *  
     * @return a list of EvaluatorAssignments for the active term
     */
    List<EvaluatorAssignment> listByActiveTerm();

    /**
     * Lists all EvaluatorAssignments by subject outcome ID.
     *
     * @param subjectOutcomeId the ID of the subject outcome
     * @return a list of EvaluatorAssignments associated with the specified subject outcome ID
     */
    List<EvaluatorAssignment> listBySubjectOutcomeId(Integer subjectOutcomeId);

    /**
     * Lists all EvaluatorAssignments by evaluator UID.
     *
     * @param evaluatorUid the UID of the evaluator
     * @return a list of EvaluatorAssignments associated with the specified evaluator UID
     */
    List<EvaluatorAssignment> listByEvaluatorUid(String evaluatorUid);

}
