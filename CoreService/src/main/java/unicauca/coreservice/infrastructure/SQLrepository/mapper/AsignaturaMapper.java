package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.Subject;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectEntity;

public class AsignaturaMapper {
    public static SubjectEntity toEntity(Subject subject) {
        if (subject == null) {
            return null;
        }
        return new SubjectEntity(
                subject.getId(),
                subject.getName(),
                subject.getDescription(),
                true);
    }

    public static Subject toAsignatura(SubjectEntity subjectEntity) {
        if (subjectEntity == null)
            return null;
        return new Subject(
                subjectEntity.getId(),
                subjectEntity.getName(),
                subjectEntity.getDescription()
        );
    }
}
