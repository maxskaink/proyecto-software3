package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.SubjectCompetency;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectCompetencyEntity;

public class SubjectCompetencyMapper {
    public static SubjectCompetency toSubjectCompetency(SubjectCompetencyEntity entity){
        return null==entity?null:
                new SubjectCompetency(
                    entity.getId(),
                        entity.getDescription(),
                        entity.getLevel(),
                        entity.getProgramCompetency().getId()
                );
    }
    public static SubjectCompetencyEntity toSubjectCompetencyEntity(SubjectCompetency subjectCompetency){
        return null==subjectCompetency?null:
                new SubjectCompetencyEntity(
                        subjectCompetency.getId(),
                        subjectCompetency.getDescription(),
                        subjectCompetency.getLevel(),
                        null,
                        true
                );
    }
}
