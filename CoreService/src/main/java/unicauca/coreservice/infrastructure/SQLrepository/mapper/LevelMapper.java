package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.Level;
import unicauca.coreservice.infrastructure.SQLrepository.entity.LevelEntity;

public class LevelMapper {

    public static Level toLevel(LevelEntity levelEntity) {
        return null == levelEntity ? null : new Level(
                levelEntity.getId(),
                levelEntity.getCategory(),
                levelEntity.getDescription(),
                levelEntity.getPercentage(),
                CriterionMapper.toCriterion(levelEntity.getCriterion())
        );
    }

    public static LevelEntity toLevelEntity(Level level) {
        return null == level ? null : new LevelEntity(
                level.getId(),
                level.getCategory(),
                level.getDescription(),
                level.getPercentage(),
                CriterionMapper.toCriterionEntity(level.getCriterion())
        );
    }
}
