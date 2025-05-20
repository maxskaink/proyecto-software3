package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.Criterion;
import unicauca.coreservice.infrastructure.SQLrepository.entity.CriterionEntity;

public class CriterionMapper {
    public static CriterionEntity toCriterionEntity(Criterion criterion) {
        return null == criterion ? null : new CriterionEntity(
                criterion.getId(),
                criterion.getWeight(),
                criterion.getName(),
                RubricMapper.toRubricEntity(criterion.getRubric()),
                null //TODO: Add the correct value fwhen level is implemented
        );
    }
    
    public static Criterion toCriterion(CriterionEntity criterionEntity) {
        return null == criterionEntity ? null : new Criterion(
                criterionEntity.getId(),
                criterionEntity.getWeight(),
                criterionEntity.getName(),
                RubricMapper.toRubric(criterionEntity.getRubric())
        );
    }
}
