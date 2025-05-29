package unicauca.microsubject.application.in;

import jakarta.validation.constraints.NotNull;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import unicauca.microsubject.domain.model.TeacherAssignment;

@Validated
public interface TeacherAssignmentInt {

    /**
     * This method is used to add a teacher assignment to the system.
     * @param teacherUid The user id of the teacher.
     * @param subjectId The id of the subject.
     * @return The teacher assignment object that was added to the system.
     */
    TeacherAssignment add(@NotNull(message = "The teacher assignment user id can not be null" ) String teacherUid,
                    @NotNull(message = "The teacher assignment subject id can not be null") Integer subjectId) throws Exception;


    /**
     * This method is used to get a teacher assignment by its id.
     * @param id The id of the teacher assignment.
     * @return The teacher assignment object that was found in the system.
     */
    TeacherAssignment getById(@NotNull(message = "The teacher assignment id can not be null") Integer id) throws Exception;

    /**
     * This method is used to get a teacher assignment by its teacher uid and subject id in the active term.
     * @param teacherUid The user id of the teacher.
     * @param subjectId The id of the subject.
     * @return The teacher assignment object that was found in the system.
     */
    TeacherAssignment getByTeacherAndSubjectInActiveTerm(@NotNull(message = "The teacher assignment user id can not be null" ) String teacherUid,
                    @NotNull(message = "The teacher assignment subject id can not be null") Integer subjectId) throws Exception;
    
    /**
     * This method is used to get a teacher assignment by its id.
     * @return The teacher assignment object that was found in the system.
     */
    List<TeacherAssignment> listTeacherAssignmentsInActiveTerm();

    /**
     * This method is used to get a teacher assignment by its term id.
     * @param subjectId The id of the term.
     * @return The teacher assignment object that was found in the system.
     */
    List<TeacherAssignment> listBySubjectId(@NotNull(message = "The teacher assignment subject id can not be null") Integer subjectId);

    /**
     * This method is used to get a teacher assignment by its teacher uid.
     * @param teacherUid The user id of the teacher.
     * @return The teacher assignment object that was found in the system.
     */
    List<TeacherAssignment> listByTeacherUid(@NotNull(message = "The teacher assignment user id can not be null" ) String teacherUid);

    /**
     * This method is used to remove a teacher assignment from the system.
     * @param id The id of the teacher assignment.
     * @return The teacher assignment object that was removed from the system.
     */
    TeacherAssignment remove(@NotNull(message = "The teacher assignment id can not be null") Integer id) throws Exception;

    /**
     * This method is used to remove a teacher assignment from the system.
     * @param teacherUid The user id of the teacher.
     * @param subjectId The id of the subject.
     * @return The teacher assignment object that was removed from the system.
     */
    TeacherAssignment removeByTeacherAndSubjectInActiveTerm(@NotNull(message = "The teacher assignment user id can not be null" ) String teacherUid,
                    @NotNull(message = "The teacher assignment subject id can not be null") Integer subjectId) throws Exception;
}
