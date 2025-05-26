package unicauca.coreservice.application.in;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import unicauca.coreservice.domain.model.SubjectOutcome;

import java.util.List;

@Validated
public interface SubjectOutcomeInt {

    /**
     * Add a new outcome to the respective subject
     * @param newSubjectOutcome instance of SubjectOutcome to be added
     * @param competencyId id of the competency of the competency to be associated
     * @param subjectId subject id of the subject to be associated
     * @param uid uid of the user adding the outcome to the subject
     * @return the added SubjectOutcome
     * @throws Exception Depends on the technology
     */

    SubjectOutcome addSubjectOutcome(
            @NotNull(message="The new subject outcome can not be null")
            SubjectOutcome newSubjectOutcome,
            @NotNull(message="The competency id can not be null")
            Integer competencyId,
            @NotNull(message="The subject id can not be null")
            Integer subjectId,
            @NotNull(message="The uid can not be null")
            String uid) throws Exception;

    /**
     * Retrieves a list of SubjectOutcome objects associated with a specific Subject.
     * It retrieves all the periods.
     * @param subjectId the unique identifier of the Subject whose associated SubjectOutcome are being retrieved
     * @param uid uid of the user retrieving the outcomes of the subject.
     * @return a list of SubjectOutcome linked to the specified Subject
     */
    List<SubjectOutcome> listAll(
            @NotNull(message="The subject id can not be null")
            Integer subjectId,
            @NotNull(message = "The uid can not be null")
            String uid
    ) throws Exception;

    /**
     * Retrieves a list of SubjectOutcome entities associated with the specified Subject for the current period.
     *
     * @param subjectId the unique identifier of the Subject whose SubjectOutcome entities for the current period are being retrieved
     * @param uid uid of the user retrieving the outcomes of the subject. The user must be a RAA in the current period.
     * @return a list of SubjectOutcome entities linked to the given Subject in the current period
     */
    List<SubjectOutcome> listAllInCurrentTerm(
            @NotNull(message="The subject id can not be null")
            Integer subjectId,
            @NotNull(message = "The uid can not be null")
            String uid
    );

    /**
     * Retrieves a list of SubjectOutcome associated with a specific SubjectCompetency in the actual period.
     *
     * @param competencyId the unique identifier of the SubjectCompetency for which the associated SubjectOutcome will be retrieved
     * @param uid uid of the user retrieving the outcomes of the competency. The user must be a RAA in the current period.
     * @return a list of SubjectOutcome linked to the specified SubjectCompetency
     */
    List<SubjectOutcome> listAllByCompetencyId(
            @NotNull(message="The competency id can not be null")
            Integer competencyId,
            @NotNull(message = "The uid can not be null")
            String uid
    );

    /**
     * Retrieves an SubjectOutcome entity by its unique identifier.
     *
     * @param id the unique identifier of the SubjectOutcome to retrieve
     * @param uid uid of the user retrieving the outcome by its id. The user must be a RAA in the current period.
     * @return the SubjectOutcome corresponding to the given ID
     */
    SubjectOutcome getById(
            @NotNull(message="The id can not be null")
            Integer id,
            @NotNull(message = "The uid can not be null")
            String uid
    ) throws Exception;

    /**
     * Updates an existing SubjectOutcome identified by its unique ID with new details.
     *
     * @param id the unique identifier of the SubjectOutcome to be updated
     * @param newSubjectOutcome an instance of SubjectOutcome containing the updated information
     * @param uid uid of the user updating the outcome. The user must be a RAA in the current period. The user must be the owner of the SubjectOutcome.
     * @return the updated SubjectOutcome instance
     */
    SubjectOutcome update(
            @NotNull(message="the subject outcome id can not be null")
            Integer id,
            @NotNull(message="The subject outcome can not be null")
            SubjectOutcome newSubjectOutcome,
            @NotNull(message = "The uid can not be null")
            String uid
    ) throws Exception;

    /**
     * Deactivate a SubjectOutcome identified by its unique ID from the system.
     *
     * @param id the unique identifier of the SubjectOutcome to be removed
     * @param uid uid of the user removing the outcome. The user must be a RAA in the current period. The user must be the owner of the SubjectOutcome.
     * @return the removed SubjectOutcome instance
     */
    SubjectOutcome remove(
            @NotNull(message="The subject outcome id can not be null")
            Integer id,
            @NotNull(message = "The uid can not be null")
            String uid
    ) throws Exception;

    /**
     * Creates a copy of an existing SubjectOutcome, associating it with a new Competency and Subject.
     * The RAAOriginal has to be of the past period, and it's going to copy in the actual period.
     *
     * @param idRAAOriginal the unique identifier of the original SubjectOutcome to be copied
     * @param competencyId the unique identifier of the new SubjectCompetency to associate with the copied SubjectOutcome
     * @param subjectId the unique identifier of the new Subject to associate with the copied SubjectOutcome
     * @param uid uid of the user coping the outcome 
     * @return a new SubjectOutcome instance representing the copied object
     */
    SubjectOutcome copy(
            @NotNull(message="The id of the original outcome can not be null")
            Integer idRAAOriginal,
            @NotNull(message="The id of the competency can not be null")
            Integer competencyId,
            @NotNull(message="The id of the subject id can not be null")
            Integer subjectId,
            @NotNull(message = "The uid can not be null")
            String uid
    ) throws Exception;

}
