package unicauca.coreservice.application.in;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import unicauca.coreservice.domain.model.SubjectOutcome;

import java.util.List;

@Validated
public interface SubjectOutcomeInt {

    SubjectOutcome addSubjectOutcome(
            @NotNull(message="The new subject outcome can not be null")
            SubjectOutcome newSubjectOutcome,
            @NotNull(message="The competency id can not be null")
            Integer competencyId,
            @NotNull(message="The subject id can not be null")
            Integer subjectId) throws Exception;

    /**
     * Retrieves a list of SubjectOutcome objects associated with a specific Subject.
     * It retrieves all the periods.
     *
     * @param subjectId the unique identifier of the Subject whose associated SubjectOutcome are being retrieved
     * @return a list of SubjectOutcome linked to the specified Subject
     */
    List<SubjectOutcome> listAll(
            @NotNull(message="The subject id can not be null")
            Integer subjectId);

    /**
     * Retrieves a list of SubjectOutcome entities associated with the specified Subject for the current period.
     *
     * @param subjectId the unique identifier of the Subject whose SubjectOutcome entities for the current period are being retrieved
     * @return a list of SubjectOutcome entities linked to the given Subject in the current period
     */
    List<SubjectOutcome> listAllInCurrentTerm(
            @NotNull(message="The subject id can not be null")
            Integer subjectId);

    /**
     * Retrieves a list of SubjectOutcome associated with a specific SubjectCompetency in the actual period.
     *
     * @param competencyId the unique identifier of the SubjectCompetency for which the associated SubjectOutcome will be retrieved
     * @return a list of SubjectOutcome linked to the specified SubjectCompetency
     */
    List<SubjectOutcome> listAllByCompetencyId(
            @NotNull(message="The competency id can not be null")
            Integer competencyId);

    /**
     * Retrieves an SubjectOutcome entity by its unique identifier.
     *
     * @param id the unique identifier of the SubjectOutcome to retrieve
     * @return the SubjectOutcome corresponding to the given ID
     */
    SubjectOutcome getById(
            @NotNull(message="The id can not be null")
            Integer id) throws Exception;

    /**
     * Updates an existing SubjectOutcome identified by its unique ID with new details.
     *
     * @param id the unique identifier of the SubjectOutcome to be updated
     * @param newSubjectOutcome an instance of SubjectOutcome containing the updated information
     * @return the updated SubjectOutcome instance
     */
    SubjectOutcome update(
            @NotNull(message="the subject outcome id can not be null")
            Integer id,
            @NotNull(message="The subject outcome can not be null")
            SubjectOutcome newSubjectOutcome) throws Exception;

    /**
     * Deactivate a SubjectOutcome identified by its unique ID from the system.
     *
     * @param id the unique identifier of the SubjectOutcome to be removed
     * @return the removed SubjectOutcome instance
     */
    SubjectOutcome remove(
            @NotNull(message="The subject outcome id can not be null")
            Integer id) throws Exception;

    /**
     * Creates a copy of an existing SubjectOutcome, associating it with a new Competency and Subject.
     * The RAAOriginal has to be of the past period, and it's going to copy in the actual period.
     *
     * @param idRAAOriginal the unique identifier of the original SubjectOutcome to be copied
     * @param competencyId the unique identifier of the new SubjectCompetency to associate with the copied SubjectOutcome
     * @param subjectId the unique identifier of the new Subject to associate with the copied SubjectOutcome
     * @return a new SubjectOutcome instance representing the copied object
     */
    SubjectOutcome copy(
            @NotNull(message="The id of the original outcome can not be null")
            Integer idRAAOriginal,
            @NotNull(message="The id of the competency can not be null")
            Integer competencyId,
            @NotNull(message="The id of the subject id can not be null")
            Integer subjectId) throws Exception;

}
