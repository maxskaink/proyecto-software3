package unicauca.coreservice.domain.useCases;


import lombok.AllArgsConstructor;
import unicauca.coreservice.application.in.LevelInt;
import unicauca.coreservice.application.out.CriterionRepositoryOutInt;
import unicauca.coreservice.application.out.IAuthorizationService;
import unicauca.coreservice.application.out.LevelRepositoryOutInt;
import unicauca.coreservice.domain.exception.Unauthorized;
import unicauca.coreservice.domain.model.Criterion;
import unicauca.coreservice.domain.model.Level;
import unicauca.coreservice.domain.model.OptionalWrapper;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class LevelService implements LevelInt {

    private final LevelRepositoryOutInt levelRepository;
    private final CriterionRepositoryOutInt criterionRepository;
    private final IAuthorizationService authorizationService;

    @Override
    public Level add(Level newLevel, Integer criterionId, String uid) throws Exception {

        if(!authorizationService.canAccessCriterion(uid, criterionId))
            throw new Unauthorized("You have not access to the criterion with id " + criterionId + " to add a level to it. ");

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
    public Level getById(Integer id, String uid) throws Exception {

        if(!authorizationService.canAccessLevel(uid, id))
            throw new Unauthorized("You have not access to the level with id " + id + " to access it. ");
        return levelRepository.getById(id)
                .getValue()
                .orElseThrow(levelRepository.getById(id)::getException);
    }

    @Override
    public List<Level> listByCriterionId(Integer criterionId, String uid) throws Exception {
        if(!authorizationService.canAccessLevel(uid,criterionId))
            throw new Unauthorized("You have not access to the criterion with id " + criterionId + " to list its levels. ");
        return levelRepository.listByCriterionId(criterionId);
    }

    @Override
    public Level update(Integer levelId, Level newLevel, String uid) throws Exception {
        if(!authorizationService.canAccessLevel(uid, levelId))
            throw new Unauthorized("You have not access to the level with id " + levelId + " to update it. ");
        OptionalWrapper<Level> response = levelRepository.update(levelId, newLevel);
        return response.getValue()
                .orElseThrow(response::getException);
    }

    @Override
    public Level remove(Integer id, String uid) throws Exception {
        if(!authorizationService.canAccessLevel(uid, id))
            throw new Unauthorized("You have not access to the level with id " + id + " to remove it. ");
        OptionalWrapper<Level> response = levelRepository.remove(id);
        return response.getValue().orElseThrow(response::getException);
    }



}
