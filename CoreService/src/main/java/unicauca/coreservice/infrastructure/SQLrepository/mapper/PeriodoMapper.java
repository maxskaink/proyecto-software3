package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.Term;
import unicauca.coreservice.infrastructure.SQLrepository.entity.TermEntity;

public class PeriodoMapper {
    public static TermEntity toEntity(Term term){
        return null== term ?null:
                new TermEntity(
                        term.getId(),
                        term.getDescription()
                );
    }
    public static Term toPeriodo(TermEntity entity){
        return null==entity?null:
            new Term(
                    entity.getId(),
                    entity.getTerm()
            );
    }
}
