package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.RAAsignatura;
import unicauca.coreservice.infrastructure.SQLrepository.entity.RAAsignaturaEntity;

public class RAAsignaturaMapper {
    public static RAAsignaturaEntity toEntity(RAAsignatura ra) {
        return null==ra? null:
                new RAAsignaturaEntity(
                        ra.getId(),
                        ra.getDescripcion(),
                        null,
                        true
                );
    }

    public static RAAsignatura toRAAsignatura(RAAsignaturaEntity entity){
        return null==entity?null:
                new RAAsignatura(
                        entity.getId(),
                        entity.getDescripcion()
                );
    }
}
