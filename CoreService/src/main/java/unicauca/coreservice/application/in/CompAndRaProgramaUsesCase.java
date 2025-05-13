package unicauca.coreservice.application.in;

import unicauca.coreservice.domain.model.CompetenciaPrograma;
import unicauca.coreservice.domain.model.RAPrograma;

import java.util.List;

public interface CompAndRaProgramaUsesCase {

    /**
     * Add a new CompetenciaPrograma in the system; it also adds the RA associated to it
     * @param newCompetenciaPrograma instance of the new CompetenciaPrograma
     * @return The CompetenciaPrograma added or the exception if it fails.
     */
    CompetenciaPrograma addCompPrograma(CompetenciaPrograma newCompetenciaPrograma) throws Exception;

    /**
     * Get all the CompetenciaPrograma activated in the system
     * @return the list of CompetenciaProgramas
     */
    List<CompetenciaPrograma> getCompetenciaProgramas();

    /**
     * Get the CompetenciaPrograma by his ID
     * @param id ID to search
     * @return the CompetenciaPrograma found
     */
    CompetenciaPrograma getCompById(Integer id) throws Exception;

    /**
     * Update a CompetenciaPrograma in system, it can fail if break some rules
     * @param id id of the CompetenciaPrograma to update
     * @param newCompetenciaPrograma instance of the new CompetenciaPrograma
     * @return the CompetenciaPrograma saved in DB or the exception if it fails
     */
    CompetenciaPrograma updateCompPrograma(Integer id, CompetenciaPrograma newCompetenciaPrograma) throws Exception;

    /**
     * Desactivate the CompetenciaPrograma and his RA in a system
     * @param id the id of the ComptenciaPrograma to desactivate
     * @return the CompetenciaPrograma desactivated
     */
    CompetenciaPrograma deleteCompPrograma(Integer id) throws Exception;

    /**
     * Get all the RA of the program
     * @return A list of RAProgramas
     */
    List<RAPrograma> getRAProgramas();

    /**
     * Get a RAPrograma by ID
     * @param id id to search
     * @return the RAPrograma found, or the exception if it fails
     */
    RAPrograma getRAById(Integer id) throws Exception;

    /**
     * update an RAPrograma in system
     * @param id id of the RAPrograma to update
     * @param newRAPrograma the instance of the RAPrograma to update
     * @return the RAPrograma updated in DB or the exception if it fails
     */
    RAPrograma updateRAPrograma(Integer id, RAPrograma newRAPrograma) throws Exception;

}