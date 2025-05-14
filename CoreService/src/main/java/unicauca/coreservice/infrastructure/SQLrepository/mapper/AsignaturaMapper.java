package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.Subject;
import unicauca.coreservice.infrastructure.SQLrepository.entity.AsignaturaEntity;

public class AsignaturaMapper {
    public static AsignaturaEntity toEntity(Subject subject) {
        if (subject == null) {
            return null;
        }
        return new AsignaturaEntity(
                subject.getId(),
                subject.getName(),
                subject.getDescription(),
                true);
    }

    public static Subject toAsignatura(AsignaturaEntity asignaturaEntity) {
        if (asignaturaEntity == null)
            return null;
        return new Subject(
                asignaturaEntity.getId(),
                asignaturaEntity.getNombre(),
                asignaturaEntity.getDescripcion()
        );
    }
}
