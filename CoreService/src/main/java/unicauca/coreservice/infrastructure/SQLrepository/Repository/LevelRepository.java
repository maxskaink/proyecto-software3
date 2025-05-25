package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import unicauca.coreservice.application.out.LevelRepositoryOutInt;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.Level;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPACriterionRepository;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPALevelRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.CriterionEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.LevelEntity;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.CriterionMapper;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.LevelMapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Repository
public class LevelRepository implements LevelRepositoryOutInt {
    private final JPALevelRepository levelRepository;
    private final JPACriterionRepository criterionRepository;
    @Override
    public OptionalWrapper<Level> add(Level newLevel) {
        try {
            newLevel.setId(null);

            LevelEntity levelEntity = LevelMapper.toLevelEntity(newLevel);

            CriterionEntity criterionEntity = criterionRepository.getReferenceById(newLevel.getCriterion().getId());

            levelEntity.setCriterion(criterionEntity);

            LevelEntity saved = levelRepository.save(levelEntity);

            Level level = LevelMapper.toLevel(saved);
            level.setCriterion(CriterionMapper.toCriterion(criterionEntity));

            return new OptionalWrapper<>(level);
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }


    @Override
    public OptionalWrapper<Level> getById(Integer id) {
        try {
            LevelEntity entity = levelRepository.findById(id)
                    .orElseThrow(() -> new NotFound("Level with id " + id + " was not found"));
    
            Level level = LevelMapper.toLevel(entity);
            level.setCriterion(CriterionMapper.toCriterion(entity.getCriterion())); 
    
            return new OptionalWrapper<>(level);
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }
    
    @Override
    public List<Level> listByCriterionId(Integer criterionId) {
        return levelRepository.findAll().stream()
                .filter(levelEntity -> levelEntity.getCriterion().getId().equals(criterionId))
                .map(entity -> {
                    Level level = LevelMapper.toLevel(entity);
                    level.setCriterion(CriterionMapper.toCriterion(entity.getCriterion())); 
                    return level;
                })
                .toList();
    }
    
    @Override
    public OptionalWrapper<Level> update(Integer levelId, Level newLevel) {
        try {
            LevelEntity levelEntity = levelRepository.findById(levelId)
                    .orElseThrow(() -> new NotFound("Level with id " + levelId + " was not found"));
    
            levelEntity.setDescription(newLevel.getDescription());
            levelEntity.setCategory(newLevel.getCategory());
            levelEntity.setPercentage(newLevel.getPercentage());
    
            Level updated = LevelMapper.toLevel(levelRepository.save(levelEntity));
            updated.setCriterion(CriterionMapper.toCriterion(levelEntity.getCriterion())); 
    
            return new OptionalWrapper<>(updated);
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }
    
    @Override
    public OptionalWrapper<Level> remove(Integer id) {
        try {
            LevelEntity levelEntity = levelRepository.findById(id)
                    .orElseThrow(() -> new NotFound("Level with id " + id + " was not found"));

            CriterionEntity criterion = levelEntity.getCriterion();
            criterion.getLevels().remove(levelEntity);

            Level level = LevelMapper.toLevel(levelEntity);
            level.setCriterion(CriterionMapper.toCriterion(levelEntity.getCriterion()));
    
            levelRepository.delete(levelEntity);
            return new OptionalWrapper<>(level);
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }
    
    @Override
    public List<Level> listAll() {
        return levelRepository.findAll().stream()
                .map(entity -> {
                    Level level = LevelMapper.toLevel(entity);
                    level.setCriterion(CriterionMapper.toCriterion(entity.getCriterion()));
                    return level;
                })
                .toList();
    }
    
}
