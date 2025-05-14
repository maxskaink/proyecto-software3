package unicauca.coreservice.application.out;

import unicauca.coreservice.domain.model.ProgramCompetency;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.ProgramOutcome;

import java.util.List;

/**
 * It is the repository for all the Competencias and RA of programa
 */
public interface ProgramCompetencyAndOutcomeRepositoryOutInt {

    /**
     * Add a new ProgramCompetency in DB; it also adds the RA associated to it
     * @param newProgramCompetency instance of the new ProgramCompetency
     * @return The ProgramCompetency added or the exception if it fails.
     */
    OptionalWrapper<ProgramCompetency> add(ProgramCompetency newProgramCompetency);

    /**
     * Get all the ProgramCompetency in DB
     * @return the list of CompetenciaProgramas
     */
    List<ProgramCompetency> listAll();

    /**
     * Get the ProgramCompetency by his
     * @param id ID to search
     * @return the ProgramCompetency found
     */
    OptionalWrapper<ProgramCompetency> getCompetencyById(Integer id);

    /**
     * Update a ProgramCompetency in DB, it can fail if break some rules
     * @param id id of the ProgramCompetency to updateProgramCompetency
     * @param newProgramCompetency instance of the new ProgramCompetency
     * @return the ProgramCompetency saved in DB or the exception if it fails
     */
    OptionalWrapper<ProgramCompetency> updateProgramCompetency(Integer id, ProgramCompetency newProgramCompetency);

    /**
     * Desactivate the ProgramCompetency in DB
     * @param id the id of the ComptenciaPrograma to desactivate
     * @return the ProgramCompetency desactivated
     */
    OptionalWrapper<ProgramCompetency> remove(Integer id);

    /**
     * Get all the RA of Programas
     * @return A list of RAProgramas
     */
    List<ProgramOutcome> getProgramOutcome();

    /**
     * Get a ProgramOutcome by ID
     * @param id id to search
     * @return the ProgramOutcome found, or the exception if it fails
     */
    OptionalWrapper<ProgramOutcome> getProgramOutcomeById(Integer id);

    /**
     * updateProgramCompetency an ProgramOutcome in DB
     * @param id id of the ProgramOutcome to updateProgramCompetency
     * @param newProgramOutcome the instance of the ProgramOutcome to updateProgramCompetency
     * @return the ProgramOutcome updated in DB or the exception if it fails
     */
    OptionalWrapper<ProgramOutcome> updateProgramOutcome(Integer id, ProgramOutcome newProgramOutcome);

}
