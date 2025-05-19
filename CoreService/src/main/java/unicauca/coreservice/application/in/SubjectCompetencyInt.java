package unicauca.coreservice.application.in;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import unicauca.coreservice.domain.model.SubjectCompetency;
import unicauca.coreservice.domain.model.SubjectOutcome;

import java.util.List;

@Validated
public interface SubjectCompetencyInt {
    /**
     * Adds a new SubjectCompetency to the specified Subject in the actual period.
     *
     * @param newProgramCompetency the instance of the new SubjectCompetency to be added
     * @param initialOutcome Initial Outcome of the competence
     * @param subjectId             the ID of the Subject to which the SubjectCompetency will be added
     * @return the added SubjectCompetency
     */
    SubjectCompetency add(
            @NotNull(message ="The new Subject competency can not be null")
            SubjectCompetency newProgramCompetency,
            @NotNull(message = "The initial outcome can not be null")
            SubjectOutcome initialOutcome,
            @NotNull(message=" The subject id can not be null")
            Integer subjectId) throws Exception;

    /**
     * Retrieves a list of SubjectCompetency associated with a specific Subject.
     *
     * @param subjectId the ID of the Subject whose associated SubjectCompetency are being retrieved
     * @return a list of SubjectCompetency linked to the given Subject
     */
    List<SubjectCompetency> listAllBySubjectId(
            @NotNull(message="The id is not valid, it can not be null")
            Integer subjectId);

    /**
     * Retrieves a SubjectCompetency by its unique identifier.
     *
     * @param id the unique identifier of the SubjectCompetency to retrieve
     * @return the SubjectCompetency corresponding to the given ID
     */
    SubjectCompetency getById(
            @NotNull(message="The id is not valid, it can not be null")
            Integer id);

    /**
     * Updates an existing SubjectCompetency identified by its unique ID with new details.
     * It only updateProgramCompetency the description.
     *
     * @param id the unique identifier of the SubjectCompetency to be updated
     * @param newProgramCompetency an instance of SubjectCompetency containing the updated information
     * @return the updated SubjectCompetency instance
     */
    SubjectCompetency update(
            @NotNull(message="The id is no valid, it can not be null")
            Integer id,
            @NotNull(message="Instance of competency is invalid, it can not be null")
            SubjectCompetency newProgramCompetency) throws Exception;

    /**
     * Deactivate a SubjectCompetency identified by its unique ID, and deactivate all associated RA's in the system.
     *
     * @param id the unique identifier of the SubjectCompetency to be removed
     * @return the removed SubjectCompetency instance
     */
    SubjectCompetency remove(
            @NotNull(message = "The id is invalid, it can not be null")
            Integer id) throws Exception;


}
