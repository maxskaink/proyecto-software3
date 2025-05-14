package unicauca.coreservice.application.out;

import unicauca.coreservice.domain.model.AsignacionCompetenciaAsignatura;
import unicauca.coreservice.domain.model.OptionalWrapper;

public interface AsignacionCompAsignaturaRepositoryOut {

    /**
     * Adds a new AsignacionCompetenciaAsignatura to the repository.
     *
     * @param newAsignacionCompetenciaAsignatura The AsignacionCompetenciaAsignatura to be added.
     * @return An {@code OptionalWrapper} containing the added AsignacionCompetenciaAsignatura if successful,
     *         or an exception if the operation fails.
     */
    OptionalWrapper<AsignacionCompetenciaAsignatura> addAsignacionCompetenciaAsignatura(AsignacionCompetenciaAsignatura newAsignacionCompetenciaAsignatura);

    /**
     * Find an AsignacionCompetenciaAsignatura by the given Competencia ID.
     *
     * @param idCompetencia the unique identifier of the Competencia whose associated AsignacionCompetenciaAsignatura is to be deleted
     * @return an OptionalWrapper containing the deleted AsignacionCompetenciaAsignatura if successful,
     *         or an exception if the operation fails
     */
    OptionalWrapper<AsignacionCompetenciaAsignatura> findByIdCompetencia(Integer idCompetencia);

    /**
     * Deletes an AsignacionCompetenciaAsignatura entity identified by the specified ID.
     *
     * @param idAsignacion The unique identifier of the AsignacionCompetenciaAsignatura to be deleted.
     * @return An {@code OptionalWrapper} containing the deleted AsignacionCompetenciaAsignatura if the operation is successful,
     *         or an exception if the deletion fails.
     */
    OptionalWrapper<AsignacionCompetenciaAsignatura> deleteById(Integer idAsignacion);
}
