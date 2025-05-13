package unicauca.coreservice.application.in;

import unicauca.coreservice.domain.model.RAAsignatura;

import java.util.List;

public interface RaAsignaturaUsesCase {

    RAAsignatura addRAAsignatura(RAAsignatura newRAAsignatura, Integer idCompetencia, Integer idAsignatura);

    /**
     * Retrieves a list of RAAsignatura objects associated with a specific Asignatura.
     * It retrieves all the periods.
     *
     * @param idAsignatura the unique identifier of the Asignatura whose associated RAAsignatura are being retrieved
     * @return a list of RAAsignatura linked to the specified Asignatura
     */
    List<RAAsignatura> listRAAsignatura(Integer idAsignatura);

    /**
     * Retrieves a list of RAAsignatura entities associated with the specified Asignatura for the current period.
     *
     * @param idAsignatura the unique identifier of the Asignatura whose RAAsignatura entities for the current period are being retrieved
     * @return a list of RAAsignatura entities linked to the given Asignatura in the current period
     */
    List<RAAsignatura> listRAAsignaturaActualPeriod(Integer idAsignatura);

    /**
     * Retrieves a list of RAAsignatura associated with a specific CompetenciaAsignatura in the actual period.
     *
     * @param idCompetencia the unique identifier of the CompetenciaAsignatura for which the associated RAAsignatura will be retrieved
     * @return a list of RAAsignatura linked to the specified CompetenciaAsignatura
     */
    List<RAAsignatura> listRAAsignaturaByCompetencia(Integer idCompetencia);

    /**
     * Retrieves an RAAsignatura entity by its unique identifier.
     *
     * @param id the unique identifier of the RAAsignatura to retrieve
     * @return the RAAsignatura corresponding to the given ID
     */
    RAAsignatura getByIdRAAsignatura(Integer id);

    /**
     * Updates an existing RAAsignatura identified by its unique ID with new details.
     *
     * @param id the unique identifier of the RAAsignatura to be updated
     * @param newRAAsignatura an instance of RAAsignatura containing the updated information
     * @return the updated RAAsignatura instance
     */
    RAAsignatura updateByIdRAAsignatura(Integer id, RAAsignatura newRAAsignatura);

    /**
     * Desactivate a RAAsignatura identified by its unique ID from the system.
     *
     * @param id the unique identifier of the RAAsignatura to be removed
     * @return the removed RAAsignatura instance
     */
    RAAsignatura removeRAAsignatura(Integer id);

    /**
     * Creates a copy of an existing RAAsignatura, associating it with a new Competencia and Asignatura.
     * The RAAOriginal has to be of the past period, and it's going to copy in the actual period.
     *
     * @param idRAAOriginal the unique identifier of the original RAAsignatura to be copied
     * @param idCompetencia the unique identifier of the new CompetenciaAsignatura to associate with the copied RAAsignatura
     * @param idAsignatura the unique identifier of the new Asignatura to associate with the copied RAAsignatura
     * @return a new RAAsignatura instance representing the copied object
     */
    RAAsignatura copyRAAsignatura(Integer idRAAOriginal, Integer idCompetencia, Integer idAsignatura);

}
