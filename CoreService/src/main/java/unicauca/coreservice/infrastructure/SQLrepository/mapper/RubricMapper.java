package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.Rubric;
import unicauca.coreservice.infrastructure.SQLrepository.entity.RubricEntity;


public class RubricMapper {

        

    public static Rubric toRubric(RubricEntity rubricEntity)  {
        return null==rubricEntity?null:
                new Rubric(
                        rubricEntity.getId(),
                        rubricEntity.getDescription(),
                        null,
                        rubricEntity.getCriteria().stream()
                                .map(CriterionMapper::toCriterion)
                                .toList()
                );
    }

    public static RubricEntity toRubricEntity(Rubric rubric) {
        return null==rubric?null:new RubricEntity(
                rubric.getId(),
                rubric.getDescription(),
                rubric.getSubjectOutcome().getId(),
                rubric.getCriteria().stream()
                        .map(CriterionMapper::toCriterionEntity)
                        .toList(),
                true
        );
    }

}
