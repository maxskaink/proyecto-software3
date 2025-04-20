package unicauca.coreservice.application.out;

import unicauca.coreservice.domain.model.AsignacionCompetenciaAsignatura;
import unicauca.coreservice.domain.model.CompetenciaAsignatura;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.RAAsignatura;

import java.util.List;

/**
 * Interface that defines the repository operations for managing
 * {@link CompetenciaAsignatura} and {@link RAAsignatura} entities.
 * <p>
 * This interface specifies CRUD operations for both CompetenciaAsignatura
 * and RAAsignatura, including methods to add, list, retrieve by ID, update,
 * and remove these entities.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 *   <li>Facilitate data persistence and retrieval of CompetenciaAsignatura entities.</li>
 *   <li>Facilitate data persistence and retrieval of RAAsignatura entities.</li>
 *   <li>Wrap responses in {@link OptionalWrapper} to handle both data and exceptions consistently.</li>
 * </ul>
 */
public interface CompAndRAAsignaturaRepositoryOut {

    // ---------------- CompetenciaAsignatura Operations ----------------

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
    OptionalWrapper<CompetenciaAsignatura> getById(Integer id);

    /**
     * Updates an existing CompetenciaAsignatura by its unique identifier.
     *
     * @param id                       The ID of the CompetenciaAsignatura to update.
     * @param newCompetenciaAsignatura The updated CompetenciaAsignatura entity.
     * @return An {@link OptionalWrapper} containing the updated CompetenciaAsignatura or an exception if the update fails.
     */
    OptionalWrapper<CompetenciaAsignatura> updateById(Integer id, CompetenciaAsignatura newCompetenciaAsignatura);

    /**
     * Removes a CompetenciaAsignatura from the repository by its unique identifier.
     *
     * @param id The ID of the CompetenciaAsignatura to remove.
     * @return An {@link OptionalWrapper} indicating the outcome of the operation, or an exception if the removal fails.
     */
    OptionalWrapper<CompetenciaAsignatura> removeCompetenciaAsignatura(Integer id);

    // ---------------- AsignacionCompetenciaAsignatura Operations

    /**
     * Adds a new AsignacionCompetenciaAsignatura to the repository.
     *
     * @param newAsignacionCompetenciaAsignatura The AsignacionCompetenciaAsignatura to be added.
     * @return An {@code OptionalWrapper} containing the added AsignacionCompetenciaAsignatura if successful,
     *         or an exception if the operation fails.
     */
    OptionalWrapper<AsignacionCompetenciaAsignatura> addAsignacionCompetenciaAsignatura(AsignacionCompetenciaAsignatura newAsignacionCompetenciaAsignatura);

    // ---------------- RAAsignatura Operations ----------------

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