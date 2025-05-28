package unicauca.microsubject.infrastructure.SQLrepository.mapper;

import unicauca.microsubject.domain.model.TeacherAssignment;
import unicauca.microsubject.infrastructure.SQLrepository.entity.TeacherAssignmentEntity;

/**
 * Mapper class to convert between TeacherAssignment and TeacherAssignmentEntity.
 * This class provides methods to map domain model objects to database entities
 * and vice versa.
 * 
 * CAUTION: This mapper does not handle the Term and Subject fields,
 * as they are not included in the TeacherAssignmentEntity.
 * If you need to handle these fields, you should implement additional logic
 * to fetch or set them as needed.
 */
public class TeacherAssignmentMapper {
    public static TeacherAssignmentEntity toTeacherAssignmentEntity(TeacherAssignment teacherAssignment) {
        return null == teacherAssignment ? null : 
        new TeacherAssignmentEntity(
                teacherAssignment.getId(),
                null,
                null,
                teacherAssignment.getTeacherUid()
        );
    }

    public static TeacherAssignment toTeacherAssignment(TeacherAssignmentEntity teacherAssignmentEntity) {
        return null == teacherAssignmentEntity ? null : 
        new TeacherAssignment(
                teacherAssignmentEntity.getId(),
                null,
                null,
                teacherAssignmentEntity.getTeacherUid()
        );
    }
}
