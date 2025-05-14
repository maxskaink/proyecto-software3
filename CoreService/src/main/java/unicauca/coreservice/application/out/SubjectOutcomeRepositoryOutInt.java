package unicauca.coreservice.application.out;

import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.RAAsignatura;

import java.util.List;

public interface RaAsignaturaRepositoryOut {

    /**
     * Adds a new RAAsignatura to the repository associated with a specific AsignacionCompetencia.
     *
     * @param newRAAsignatura The RAAsignatura to be added.
     * @param asignacionCompetencia The identifier of the AsignacionCompetencia to associate with the RAAsignatura.
     * @return An {@code OptionalWrapper} containing the added RAAsignatura if successful, or an exception if the operation fails.
     */
    OptionalWrapper<RAAsignatura> addRAAsignatura(RAAsignatura newRAAsignatura, Integer asignacionCompetencia);

    /**
     * Retrieves a list of RAAsignatura associated with a specific Asignatura.
     *
     * @param idAsignatura The unique identifier of the Asignatura whose associated RAAsignatura entities are to be retrieved.
     * @return A list of RAAsignatura objects associated with the specified Asignatura.
     */
    List<RAAsignatura> listRAAsignatura(Integer idAsignatura);

    /**
     * Retrieves a list of RAAsignatura associated with a specific Competencia.
     *
     * @param idCompetencia The unique identifier of the Competencia.
     * @return A list of RAAsignatura objects associated with the specified Competencia.
     */
    List<RAAsignatura> listRAAsignaturaByCompetencia(Integer idCompetencia);
    /**
     * Retrieves a RAAsignatura by its unique identifier.
     *
     * @param id The ID of the RAAsignatura to retrieve.
     * @return An {@link OptionalWrapper} containing the found RAAsignatura or an exception if not found.
     */
    OptionalWrapper<RAAsignatura> getByIdRAAsignatura(Integer id);

    /**
     * Updates an existing RAAsignatura by its unique identifier.
     *
     * @param id              The ID of the RAAsignatura to update.
     * @param newRAAsignatura The updated RAAsignatura entity.
     * @return An {@link OptionalWrapper} containing the updated RAAsignatura or an exception if the update fails.
     */
    OptionalWrapper<RAAsignatura> updateByIdRAAsignatura(Integer id, RAAsignatura newRAAsignatura);

    /**
     * Removes a RAAsignatura from the repository by its unique identifier.
     *
     * @param id The ID of the RAAsignatura to remove.
     * @return An {@link OptionalWrapper} indicating the outcome of the operation, or an exception if the removal fails.
     */
    OptionalWrapper<RAAsignatura> removeRAAsignatura(Integer id);

}
