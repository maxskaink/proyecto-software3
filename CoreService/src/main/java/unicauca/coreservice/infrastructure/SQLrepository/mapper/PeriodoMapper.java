package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.Term;
import unicauca.coreservice.infrastructure.SQLrepository.entity.PeriodoEntity;

public class PeriodoMapper {
    public static PeriodoEntity toEntity(Term term){
        return null== term ?null:
                new PeriodoEntity(
                        term.getId(),
                        term.getDescription()
                );
    }
    public static Term toPeriodo(PeriodoEntity entity){
        return null==entity?null:
            new Term(
                    entity.getId(),
                    entity.getPeriodo()
            );
    }
}
