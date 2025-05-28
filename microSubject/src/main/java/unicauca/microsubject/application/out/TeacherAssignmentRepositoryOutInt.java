package unicauca.microsubject.application.out;

import java.util.List;

import unicauca.microsubject.domain.model.OptionalWrapper;
import unicauca.microsubject.domain.model.TeacherAssignment;

public interface TeacherAssignmentRepositoryOutInt {

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

    /**
     * Lists all TeacherAssignments associated with a specific teacher UID in the active term.
     *
     * @param teacherUid The UID of the teacher for which to list TeacherAssignments.
     * @return An OptionalWrapper containing a list of TeacherAssignments or an exception if the operation fails.
     */
    List<TeacherAssignment> listByTeacherUidActiveTerm(String teacherUid) throws Exception;

    /**
     * Removes a TeacherAssignment from the repository.
     * 
     * @param id The ID of the TeacherAssignment to remove.
     * @return An OptionalWrapper containing the removed TeacherAssignment or an exception if the operation fails.
     */
    OptionalWrapper<TeacherAssignment> remove(Integer id);

    /**
     * Removes a TeacherAssignment by its teacher UID and subject ID in the active term.
     * 
     * @param teacherUid The UID of the teacher.
     * @param subjectId The ID of the subject.
     * @return An OptionalWrapper containing the removed TeacherAssignment or an exception if the operation fails.
     */
    OptionalWrapper<TeacherAssignment> removeByTeacherAndSubjectInActiveTerm(String teacherUid, Integer subjectId);
}
