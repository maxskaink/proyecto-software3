package unicauca.coreservice.application.out;

import unicauca.coreservice.domain.model.Asignatura;
import unicauca.coreservice.domain.model.OptionalWrapper;

import java.util.List;

public interface AsignaturaRepositoryOut {
    /**
     * Add Asignatura to the DB
     * @param newAsignatura instance of the new Asignatura
     * @return The OptionalWrapper for robust response.
     */
    OptionalWrapper<Asignatura> addAsignatura(Asignatura newAsignatura);

    /**
     * Get all the Asignatura in DB.
     * @return the List of the Asignatura
     */
    List<Asignatura> getAsignaturas();

    /**
     * Get an Asignatura by his ID
     * @param id id to search in DB
     * @return The OptionalWrapper for robust response
     */
    OptionalWrapper<Asignatura> getById(Integer id);

    /**
     * Update the Asignatura in DB
     * @param id id of the Asignatura to update
     * @param newAsignatura instance of the new Aisnatura
     * @return The OptionalWrapper for robust response
     */
    OptionalWrapper<Asignatura> updateById(Integer id, Asignatura newAsignatura);

    /**
     * Deactivate the Asignatura in DB
     * @param id Id of the Asignatura to delet
     * @return The OptionalWrapper for robust response
     */
    OptionalWrapper<Asignatura> removeAsignatura(Integer id);
}
