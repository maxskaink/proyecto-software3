package unicauca.coreservice.domain.useCases;


import lombok.AllArgsConstructor;
import unicauca.coreservice.application.in.LevelInt;
import unicauca.coreservice.application.out.CriterionRepositoryOutInt;
import unicauca.coreservice.application.out.LevelRepositoryOutInt;
import unicauca.coreservice.domain.model.Criterion;
import unicauca.coreservice.domain.model.Level;
import unicauca.coreservice.domain.model.OptionalWrapper;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class LevelService implements LevelInt {

    private final LevelRepositoryOutInt levelRepository;
    private final CriterionRepositoryOutInt criterionRepository;
    @Override
    public Level add(Level newLevel, Integer criterionId) throws Exception {
        OptionalWrapper<Criterion> criterionWrapper = criterionRepository.getById(criterionId);
        Optional<Criterion> criterionOpt = criterionWrapper.getValue();

        if (criterionOpt.isEmpty()) {
            throw criterionWrapper.getException();
        }

        newLevel.setCriterion(criterionOpt.get());

        OptionalWrapper<Level> response = levelRepository.add(newLevel);

        return response.getValue()
                .orElseThrow(response::getException);
    }

    @Override
    public Level getById(Integer id) throws Exception {
        return levelRepository.getById(id)
                .getValue()
                .orElseThrow(levelRepository.getById(id)::getException);
    }

    @Override
    public List<Level> listByCriterionId(Integer criterionId) {
        return levelRepository.listByCriterionId(criterionId);
    }

    @Override
    public Level update(Integer levelId, Level newLevel) throws Exception {
        OptionalWrapper<Level> response = levelRepository.update(levelId, newLevel);
        return response.getValue()
                .orElseThrow(response::getException);
    }

    @Override
    public Level remove(Integer id) throws Exception {
        OptionalWrapper<Level> response = levelRepository.remove(id);
        return response.getValue().orElseThrow(response::getException);
    }



}
