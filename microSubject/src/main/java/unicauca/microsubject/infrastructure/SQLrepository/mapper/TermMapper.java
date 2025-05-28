package unicauca.microsubject.infrastructure.SQLrepository.mapper;

import unicauca.microsubject.domain.model.Term;
import unicauca.microsubject.infrastructure.SQLrepository.entity.TermEntity;

public class TermMapper {
    public static TermEntity toTermEntity(Term term){
        return null== term ?null:
                new TermEntity(
                        term.getId(),
                        term.getDescription()
                );
    }
    public static Term toTerm(TermEntity termEntity){
        return null==termEntity?null:
            new Term(
                    termEntity.getId(),
                    termEntity.getTerm()
            );
    }
}
