package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.Criterion;
import unicauca.coreservice.infrastructure.SQLrepository.entity.CriterionEntity;

public class CriterionMapper {
    public static CriterionEntity toCriterionEntity(Criterion criterion) {
        return null == criterion ? null : new CriterionEntity(
                criterion.getId(),
                criterion.getWeight(),
                criterion.getName(),
                null,
                criterion.getLevels().stream().map(LevelMapper::toLevelEntity).toList()
        );
    }
    
    public static Criterion toCriterion(CriterionEntity criterionEntity) {
        return null == criterionEntity ? null : new Criterion(
                criterionEntity.getId(),
                criterionEntity.getWeight(),
                criterionEntity.getName(),
               null,
                criterionEntity.getLevels().stream().map(LevelMapper::toLevel).toList()
        );
    }
}
