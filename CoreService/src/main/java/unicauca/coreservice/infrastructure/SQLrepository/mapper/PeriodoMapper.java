package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.Periodo;
import unicauca.coreservice.infrastructure.SQLrepository.entity.PeriodoEntity;

public class PeriodoMapper {
    public static PeriodoEntity toEntity(Periodo periodo){
        return null==periodo?null:
                new PeriodoEntity(
                        periodo.getId(),
                        periodo.getDescripcion()
                );
    }
    public static Periodo toPeriodo(PeriodoEntity entity){
        return null==entity?null:
            new Periodo(
                    entity.getId(),
                    entity.getPeriodo()
            );
    }
}
