package unicauca.microsubject.application.out;

import unicauca.microsubject.domain.model.Subject;
import unicauca.microsubject.domain.model.OptionalWrapper;

import java.util.List;

public interface SubjectRepositoryOutInt {
    /**
     * Add Subject to the DB
     * @param newSubject instance of the new Subject
     * @return The OptionalWrapper for robust response.
     */
    OptionalWrapper<Subject> add(Subject newSubject);

    /**
     * Get all the Subject in DB.
     * @return the List of the Subject
     */
    List<Subject> listAll();

    /**
     * Get a Subject by his ID
     * @param id id to search in DB
     * @return The OptionalWrapper for robust response
     */
    OptionalWrapper<Subject> getById(Integer id);

    /**
     * Update the Subject in DB
     * @param id id of the Subject to updateProgramCompetency
     * @param newSubject instance of the new Subject
     * @return The OptionalWrapper for robust response
     */
    OptionalWrapper<Subject> update(Integer id, Subject newSubject);

    /**
     * Deactivate the Subject in DB
     * @param id of the Subject to remove
     * @return The OptionalWrapper for robust response
     */
    OptionalWrapper<Subject> remove(Integer id);
}
