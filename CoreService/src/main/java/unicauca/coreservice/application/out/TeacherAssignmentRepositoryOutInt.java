package unicauca.coreservice.application.out;

import java.util.List;

import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.TeacherAssignment;

public interface TeacherAssignmentRepositoryOutInt {

    /**
     * Retrieves a TeacherAssignment by its ID.
     *
     * @param id The ID of the TeacherAssignment to retrieve.
     * @return An OptionalWrapper containing the TeacherAssignment or an exception if not found.
     */
    OptionalWrapper<TeacherAssignment> getById(Integer id);

    /**
     * Retrieves a TeacherAssignment by its teacher UID and subject ID in the active term.
     *
     * @param teacherUid The UID of the teacher.
     * @param subjectId The ID of the subject.
     * @return An OptionalWrapper containing the TeacherAssignment or an exception if not found.
     */
    OptionalWrapper<TeacherAssignment> getByTeacherAndSubjectInActiveTerm(String teacherUid, Integer subjectId);

    /**
     * Lists all TeacherAssignments associated with the active term.
     * 
     * @return A list of TeacherAssignments associated with the active term.
     */
    List<TeacherAssignment> listByActiveTerm();

    /**
     * List all TeacherAssignments associated with a specific subject ID.
     * 
     * @param subjectId The ID of the subject for which to list TeacherAssignments.
     * @return An OptionalWrapper containing a list of TeacherAssignments or an exception if the operation fails.
     */
    List<TeacherAssignment> listBySubjectId(Integer subjectId);

    /**
     * Lists all TeacherAssignments associated with a specific teacher UID.
     * 
     * @param teacherUid The UID of the teacher for which to list TeacherAssignments.
     * @return An OptionalWrapper containing a list of TeacherAssignments or an exception if the operation fails.
     */
    List<TeacherAssignment> listByTeacherUid(String teacherUid);

}
