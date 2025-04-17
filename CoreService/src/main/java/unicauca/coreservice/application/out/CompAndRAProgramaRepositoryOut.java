package unicauca.coreservice.application.out;

import unicauca.coreservice.domain.model.CompetenciaPrograma;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.RAPrograma;

import java.util.List;

/**
 * It is the repository for all the Competencias and RA of programa
 */
public interface CompAndRAProgramaRepositoryOut {

    /**
     * Add a new CompetenciaPrograma in DB; it also adds the RA associated to it
     * @param newCompetenciaPrograma instance of the new CompetenciaPrograma
     * @return The CompetenciaPrograma added or the exception if it fails.
     */
    OptionalWrapper<CompetenciaPrograma> addCompPrograma(CompetenciaPrograma newCompetenciaPrograma);

    /**
     * Get all the CompetenciaPrograma in DB
     * @return the list of CompetenciaProgramas
     */
    List<CompetenciaPrograma> getCompetenciaProgramas();

    /**
     * Get the CompetenciaPrograma by his ID
     * @param id ID to search
     * @return the CompetenciaPrograma found
     */
    OptionalWrapper<CompetenciaPrograma> getCompById(Integer id);

    /**
     * Update a CompetenciaPrograma in DB, it can fail if break some rules
     * @param id id of the CompetenciaPrograma to update
     * @param newCompetenciaPrograma instance of the new CompetenciaPrograma
     * @return the CompetenciaPrograma saved in DB or the exception if it fails
     */
    OptionalWrapper<CompetenciaPrograma> updateCompPrograma(Integer id, CompetenciaPrograma newCompetenciaPrograma);

    /**
     * Desactivate the CompetenciaPrograma in DB
     * @param id the id of the ComptenciaPrograma to desactivate
     * @return the CompetenciaPrograma desactivated
     */
    OptionalWrapper<CompetenciaPrograma> deleteCompPrograma(Integer id);

    /**
     * Get all the RA of Programas
     * @return A list of RAProgramas
     */
    List<RAPrograma> getRAPrograma();

    /**
     * Get a RAPrograma by ID
     * @param id id to search
     * @return the RAPrograma found, or the exception if it fails
     */
    OptionalWrapper<RAPrograma> getRAById(Integer id);

    /**
     * update an RAPrograma in DB
     * @param id id of the RAPrograma to update
     * @param newRAPrograma the instance of the RAPrograma to update
     * @return the RAPrograma updated in DB or the exception if it fails
     */
    OptionalWrapper<RAPrograma> updateRAPrograma(Integer id, RAPrograma newRAPrograma);

    /**
     * Deactivate an RAPrograma in DB
     * @param id id of the RAPrograma to deactivate
     * @return The RAPrograma deactivated
     */
    OptionalWrapper<RAPrograma> deleteRAPrograma(Integer id);

}
