package unicauca.coreservice.application.in;

import unicauca.coreservice.domain.model.ProgramCompetency;
import unicauca.coreservice.domain.model.ProgramOutcome;

import java.util.List;

public interface ProgramCompetencyAndOutcomeInt {

    /**
     * Add a new ProgramCompetency in the system; it also adds the RA associated to it
     * @param newProgramCompetency instance of the new ProgramCompetency
     * @return The ProgramCompetency added or the exception if it fails.
     */
    ProgramCompetency add(ProgramCompetency newProgramCompetency) throws Exception;

    /**
     * Get all the ProgramCompetency activated in the system
     * @return the list of CompetenciaProgramas
     */
    List<ProgramCompetency> listAllProgramCompetencies();

    /**
     * Get the ProgramCompetency by his ID
     * @param id ID to search
     * @return the ProgramCompetency found
     */
    ProgramCompetency getProgramCompetencyById(Integer id) throws Exception;

    /**
     * Update a ProgramCompetency in system, it can fail if break some rules
     * @param id id of the ProgramCompetency to updateProgramCompetency
     * @param newProgramCompetency instance of the new ProgramCompetency
     * @return the ProgramCompetency saved in DB or the exception if it fails
     */
    ProgramCompetency updateProgramCompetency(Integer id, ProgramCompetency newProgramCompetency) throws Exception;

    /**
     * Desactivate the ProgramCompetency and his RA in a system
     * @param id the id of the ComptenciaPrograma to desactivate
     * @return the ProgramCompetency desactivated
     */
    ProgramCompetency remove(Integer id) throws Exception;

    /**
     * Get all the RA of the program
     * @return A list of RAProgramas
     */
    List<ProgramOutcome> listAllProgramOutcomes();

    /**
     * Get a ProgramOutcome by ID
     * @param id id to search
     * @return the ProgramOutcome found, or the exception if it fails
     */
    ProgramOutcome getProgramOutcomeById(Integer id) throws Exception;

    /**
     * updateProgramCompetency an ProgramOutcome in system
     * @param id id of the ProgramOutcome to updateProgramCompetency
     * @param newProgramOutcome the instance of the ProgramOutcome to updateProgramCompetency
     * @return the ProgramOutcome updated in DB or the exception if it fails
     */
    ProgramOutcome updateProgramOutcome(Integer id, ProgramOutcome newProgramOutcome) throws Exception;

}