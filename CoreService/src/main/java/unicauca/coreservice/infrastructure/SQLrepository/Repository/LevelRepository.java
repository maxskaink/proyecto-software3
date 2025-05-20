package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import unicauca.coreservice.application.out.LevelRepositoryOutInt;
import unicauca.coreservice.domain.model.Level;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPALevelRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.LevelEntity;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.LevelMapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Repository
public class LevelRepository implements LevelRepositoryOutInt {
    private final JPALevelRepository levelRepository;

    @Override
    public OptionalWrapper<Level> add(Level newLevel) {
        try{
            newLevel.setId(null);
            LevelEntity levelEntity = LevelMapper.toLevelEntity(newLevel);
            return new OptionalWrapper<>(
                    LevelMapper.toLevel(levelRepository.save(levelEntity))
            );
        }catch (Exception e){
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<Level> getById(Integer id) {
        try{
            return new OptionalWrapper<>(
                    LevelMapper.toLevel(levelRepository.findById(id).orElseThrow(
                            () -> new RuntimeException("Level with id " + id + " was not found")
                    ))
            );
        }catch (Exception e){
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<Level> getByCriterionId(Integer criterionId) {
        try{
            return new OptionalWrapper<>(
                    (levelRepository.findAll().stream()
                            .filter(levelEntity -> levelEntity.getCriterion().getId().equals(criterionId))
                            .findFirst()
                            .map(LevelMapper::toLevel)
                            .orElseThrow(() -> new RuntimeException("Level with criterion id " + criterionId + " was not found"))
                    )
            );
        }catch (Exception e){
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<Level> remove(Integer id) {
        try{
            LevelEntity levelEntity = levelRepository.findById(id).orElseThrow(
                    () -> new RuntimeException("Level with id " + id + " was not found")
            );
            levelRepository.delete(levelEntity);
            return new OptionalWrapper<>(LevelMapper.toLevel(levelEntity));
        }catch (Exception e){
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public List<Level> listAll() {
        return levelRepository.findAll().stream()
                .map(LevelMapper::toLevel)
                .toList();
    }
}
