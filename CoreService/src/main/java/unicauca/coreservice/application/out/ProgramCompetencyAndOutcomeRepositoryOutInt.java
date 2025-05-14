package unicauca.coreservice.application.out;

import unicauca.coreservice.domain.model.ProgramCompetency;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.ProgramOutcome;

import java.util.List;

/**
 * Repository interface for managing program competencies and program outcomes.
 */
public interface ProgramCompetencyAndOutcomeRepositoryOutInt {

    /**
     * Adds a new ProgramCompetency to the database, including its associated program outcomes.
     *
     * @param newProgramCompetency the ProgramCompetency to be added
     * @return the added ProgramCompetency wrapped in an OptionalWrapper, or an exception if the operation fails
     */
    OptionalWrapper<ProgramCompetency> add(ProgramCompetency newProgramCompetency);

    /**
     * Retrieves all ProgramCompetency records from the database.
     *
     * @return a list of all ProgramCompetency instances
     */
    List<ProgramCompetency> listAll();

    /**
     * Retrieves a ProgramCompetency by its ID.
     *
     * @param id the ID of the ProgramCompetency to retrieve
     * @return the corresponding ProgramCompetency wrapped in an OptionalWrapper, or an exception if not found
     */
    OptionalWrapper<ProgramCompetency> getCompetencyById(Integer id);

    /**
     * Updates an existing ProgramCompetency in the database.
     *
     * @param id the ID of the ProgramCompetency to update
     * @param newProgramCompetency the new ProgramCompetency data
     * @return the updated ProgramCompetency wrapped in an OptionalWrapper, or an exception if the operation fails
     */
    OptionalWrapper<ProgramCompetency> updateProgramCompetency(Integer id, ProgramCompetency newProgramCompetency);

    /**
     * Deactivates a ProgramCompetency in the database.
     *
     * @param id the ID of the ProgramCompetency to deactivate
     * @return the deactivated ProgramCompetency wrapped in an OptionalWrapper
     */
    OptionalWrapper<ProgramCompetency> remove(Integer id);

    /**
     * Retrieves all ProgramOutcome records from the database.
     *
     * @return a list of all ProgramOutcome instances
     */
    List<ProgramOutcome> getProgramOutcome();

    /**
     * Retrieves a ProgramOutcome by its ID.
     *
     * @param id the ID of the ProgramOutcome to retrieve
     * @return the corresponding ProgramOutcome wrapped in an OptionalWrapper, or an exception if not found
     */
    OptionalWrapper<ProgramOutcome> getProgramOutcomeById(Integer id);

    /**
     * Updates an existing ProgramOutcome in the database.
     *
     * @param id the ID of the ProgramOutcome to update
     * @param newProgramOutcome the new ProgramOutcome data
     * @return the updated ProgramOutcome wrapped in an OptionalWrapper, or an exception if the operation fails
     */
    OptionalWrapper<ProgramOutcome> updateProgramOutcome(Integer id, ProgramOutcome newProgramOutcome);
}
