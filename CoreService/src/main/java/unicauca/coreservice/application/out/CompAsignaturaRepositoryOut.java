package unicauca.coreservice.application.out;

import unicauca.coreservice.domain.model.CompetenciaAsignatura;
import unicauca.coreservice.domain.model.OptionalWrapper;

import java.util.List;

public interface CompAsignaturaRepositoryOut {
    /**
     * Adds a new CompetenciaAsignatura to the repository.
     *
     * @param newCompetenciaAsignatura The CompetenciaAsignatura to be added.
     * @return An {@link OptionalWrapper} containing the added CompetenciaAsignatura or an exception if the process fails.
     */
    OptionalWrapper<CompetenciaAsignatura> addCompetenciaAsignatura(CompetenciaAsignatura newCompetenciaAsignatura);

    /**
     * Retrieves all CompetenciaAsignatura entities from the repository.
     *
     * @return A {@link List} of all CompetenciaAsignatura entities.
     */
    List<CompetenciaAsignatura> listCompetenciaAsignatura();

    /**
     * Retrieves a list of CompetenciaAsignatura associated with a specific Asignatura.
     *
     * @param idAsignatura The unique identifier of the Asignatura whose associated CompetenciaAsignatura entities are to be retrieved.
     * @return A list of CompetenciaAsignatura objects associated with the specified Asignatura.
     */
    List<CompetenciaAsignatura> listCompetenciaAsignatura(Integer idAsignatura);
    /**
     * Retrieves a CompetenciaAsignatura by its unique identifier.
     *
     * @param id The ID of the CompetenciaAsignatura to retrieve.
     * @return An {@link OptionalWrapper} containing the found CompetenciaAsignatura or an exception if not found.
     */
    OptionalWrapper<CompetenciaAsignatura> getCompetenciaById(Integer id);

    /**
     * Updates an existing CompetenciaAsignatura by its unique identifier.
     *
     * @param id                       The ID of the CompetenciaAsignatura to update.
     * @param newCompetenciaAsignatura The updated CompetenciaAsignatura entity.
     * @return An {@link OptionalWrapper} containing the updated CompetenciaAsignatura or an exception if the update fails.
     */
    OptionalWrapper<CompetenciaAsignatura> updateCompetenciaById(Integer id, CompetenciaAsignatura newCompetenciaAsignatura);

    /**
     * Removes a CompetenciaAsignatura from the repository by its unique identifier.
     *
     * @param id The ID of the CompetenciaAsignatura to remove.
     * @return An {@link OptionalWrapper} indicating the outcome of the operation, or an exception if the removal fails.
     */
    OptionalWrapper<CompetenciaAsignatura> removeCompetenciaAsignatura(Integer id);


}
