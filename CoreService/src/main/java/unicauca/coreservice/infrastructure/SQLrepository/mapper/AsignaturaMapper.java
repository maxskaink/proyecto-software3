package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.Asignatura;
import unicauca.coreservice.infrastructure.SQLrepository.entity.AsignaturaEntity;

public class AsignaturaMapper {
    public static AsignaturaEntity toEntity(Asignatura asignatura) {
        if (asignatura == null) {
            return null;
        }
        return new AsignaturaEntity(
                asignatura.getId(),
                asignatura.getNombre(),
                asignatura.getDescripcion(),
                true);
    }

    public static Asignatura toAsignatura(AsignaturaEntity asignaturaEntity) {
        if (asignaturaEntity == null)
            return null;
        return new Asignatura(
                asignaturaEntity.getId(),
                asignaturaEntity.getNombre(),
                asignaturaEntity.getDescripcion()
        );
    }
}
