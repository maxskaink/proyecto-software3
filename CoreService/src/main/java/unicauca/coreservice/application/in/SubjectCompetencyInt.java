package unicauca.coreservice.application.in;

import unicauca.coreservice.domain.model.CompetenciaAsignatura;
import unicauca.coreservice.domain.model.RAAsignatura;

import java.util.List;

public interface CompAsignaturaUsesCase {
    /**
     * Adds a new CompetenciaAsignatura to the specified Asignatura in the actual period.
     *
     * @param newCompetenciaAsignatura the instance of the new CompetenciaAsignatura to be added
     * @param firstRA First RA of the competence
     * @param idAsignatura             the ID of the Asignatura to which the CompetenciaAsignatura will be added
     * @return the added CompetenciaAsignatura
     */
    CompetenciaAsignatura addCompetencia(CompetenciaAsignatura newCompetenciaAsignatura, RAAsignatura firstRA, Integer idAsignatura) throws Exception;

    /**
     * Retrieves a list of CompetenciaAsignatura associated with a specific Asignatura.
     *
     * @param idAsignatura the ID of the Asignatura whose associated CompetenciaAsignatura are being retrieved
     * @return a list of CompetenciaAsignatura linked to the given Asignatura
     */
    List<CompetenciaAsignatura> listCompetenciaAsignatura(Integer idAsignatura);

    /**
     * Retrieves a CompetenciaAsignatura by its unique identifier.
     *
     * @param id the unique identifier of the CompetenciaAsignatura to retrieve
     * @return the CompetenciaAsignatura corresponding to the given ID
     */
    CompetenciaAsignatura getCompetenciaById(Integer id);

    /**
     * Updates an existing CompetenciaAsignatura identified by its unique ID with new details.
     * It only update the description.
     *
     * @param id the unique identifier of the CompetenciaAsignatura to be updated
     * @param newCompetenciaAsignatura an instance of CompetenciaAsignatura containing the updated information
     * @return the updated CompetenciaAsignatura instance
     */
    CompetenciaAsignatura updateCompetenciaById(Integer id, CompetenciaAsignatura newCompetenciaAsignatura) throws Exception;

    /**
     * Desactivate a CompetenciaAsignatura identified by its unique ID, and desactivate all associated RA's in the system.
     *
     * @param id the unique identifier of the CompetenciaAsignatura to be removed
     * @return the removed CompetenciaAsignatura instance
     */
    CompetenciaAsignatura removeCompetenciaAsignatura(Integer id) throws Exception;

    /**
     * Adds a new RAAsignatura associated with a specific CompetenciaAsignatura and Asignatura.
     * It in the actual period.
     *
     * @param newRAAsignatura the RAAsignatura instance to be added
     * @param idCompetencia the unique identifier of the CompetenciaAsignatura associated with the RAAsignatura
     * @param idAsignatura the unique identifier of the Asignatura associated with the RAAsignatura
     * @return the newly added RAAsignatura instance
     */

}
