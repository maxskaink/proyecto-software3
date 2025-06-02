package unicauca.microsubject.application.in;

import jakarta.validation.constraints.NotNull;

import java.util.List;

import org.springframework.validation.annotation.Validated;


import unicauca.microsubject.domain.model.EvaluatorAssignment;

@Validated
public interface EvaluatorAssignmentInt {

    /**
     * Adds a new evaluator assignment.
     *
     * @param evaluatorUid The evaluator assignment to add.
     * @param outcomeId outcome to asociated
     * @return The added evaluator assignment.
     * @throws Exception If an error occurs while adding the evaluator assignment.
     */
    EvaluatorAssignment add(@NotNull(message="The evaluator assignment user id can not be null") String evaluatorUid,
                    @NotNull(message = "The evaluator assignment subject id can not be null") Integer outcomeId) throws Exception;

    
    /**
     * Retrieves an evaluator assignment by its ID.
     *  
     * @param id The ID of the evaluator assignment to retrieve.
     * @return The evaluator assignment with the specified ID.
     * @throws Exception If an error occurs while retrieving the evaluator assignment.
     */
    EvaluatorAssignment getById(@NotNull(message = "The evaluator assignment id can not be null") Integer id) throws Exception;

    /**
     * Retrieves a list of evaluator assignments for a specific subject in the active term.
     *
     * @param subjectId The ID of the subject.
     * @return A list of evaluator assignments for the specified subject.
     * @throws Exception If an error occurs while retrieving the evaluator assignments.
     */
    EvaluatorAssignment getByEvaluatorAndSubjectInActiveTerm(
            @NotNull(message = "The evaluator assignment user id can not be null") String evaluatorUid,
            @NotNull(message = "The evaluator assignment subject id can not be null") Integer subjectId) throws Exception;

    /**
     * Retrieves a list of evaluator assignments in the active term.
     * * @return A list of evaluator assignments in the active term.
     */
    List<EvaluatorAssignment> listEvaluatorAssignmentsInActiveTerm();

    /**
     * Retrieves a list of evaluator assignments by subject ID.
     *
     * @param subjectOutcomeId The ID of the subject.
     * @return A list of evaluator assignments for the specified subject.
     */
    List<EvaluatorAssignment> listBySubjectOutcomeId(
            @NotNull(message = "The evaluator assignment subject id can not be null") Integer subjectOutcomeId);

    /**
     * Retrieves a list of evaluator assignments by evaluator user ID.
     * 
     * @param evaluatorUid The user ID of the evaluator.
     * @return A list of evaluator assignments for the specified evaluator.
     * @throws Exception If an error occurs while retrieving the evaluator assignments.
     */
    List<EvaluatorAssignment> listByEvaluatorUid(
            @NotNull(message = "The evaluator assignment user id can not be null") String evaluatorUid);

    /**
     * Removes an evaluator assignment by its ID.
     *  
     * @param id The ID of the evaluator assignment to remove.
     * @return The removed evaluator assignment.
     * @throws Exception If an error occurs while removing the evaluator assignment.
     */
    EvaluatorAssignment remove(
            @NotNull(message = "The evaluator assignment id can not be null") Integer id) throws Exception;

    /**
     * Removes an evaluator assignment by evaluator user ID and subject ID in the active term.
     * * @param evaluatorUid The user ID of the evaluator.
     * @param subjectId The ID of the subject.
     * @return The removed evaluator assignment.
     * @throws Exception If an error occurs while removing the evaluator assignment.
     */
    EvaluatorAssignment removeByEvaluatorAndSubjectInActiveTerm(
            @NotNull(message = "The evaluator assignment user id can not be null") String evaluatorUid,
            @NotNull(message = "The evaluator assignment subject id can not be null") Integer subjectId) throws Exception;
 }