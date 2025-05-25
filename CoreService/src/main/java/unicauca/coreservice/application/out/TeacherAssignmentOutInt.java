package unicauca.coreservice.application.out;

import java.util.List;

import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.TeacherAssignment;

public interface TeacherAssignmentOutInt {

    /**
     * Adds a new TeacherAssignment to the repository.
     *
     * @param teacherAssignment The TeacherAssignment to be added.
     * @return An OptionalWrapper containing the added TeacherAssignment or an exception if the operation fails.
     */
    OptionalWrapper<TeacherAssignment> add(TeacherAssignment teacherAssignment);

    /**
     * Retrieves a TeacherAssignment by its ID.
     *
     * @param id The ID of the TeacherAssignment to retrieve.
     * @return An OptionalWrapper containing the TeacherAssignment or an exception if not found.
     */
    OptionalWrapper<TeacherAssignment> getById(Integer id);

    /**
     * Lists all TeacherAssignments associated with a specific term ID.
     *
     * @param termId The ID of the term for which to list TeacherAssignments.
     * @return An OptionalWrapper containing a list of TeacherAssignments or an exception if the operation fails.
     */
    List<TeacherAssignment> listByTermId(Integer termId);

    /**
     * List all TeacherAssignments associated with a specific subject ID.
     * 
     * @param subjectId The ID of the subject for which to list TeacherAssignments.
     * @return An OptionalWrapper containing a list of TeacherAssignments or an exception if the operation fails.
     */
    List<TeacherAssignment> listBySubjectId(Integer subjectId);

    /**
     * Removes a TeacherAssignment from the repository.
     * 
     * @param id The ID of the TeacherAssignment to remove.
     * @return An OptionalWrapper containing the removed TeacherAssignment or an exception if the operation fails.
     */
    OptionalWrapper<TeacherAssignment> remove(Integer id);  

}
