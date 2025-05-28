package unicauca.coreservice.domain.useCases;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import unicauca.coreservice.application.in.CriterionInt;
import unicauca.coreservice.application.out.CriterionRepositoryOutInt;
import unicauca.coreservice.application.out.IAuthorizationService;
import unicauca.coreservice.application.out.RubricRepositoryOutInt;
import unicauca.coreservice.domain.exception.Unauthorized;
import unicauca.coreservice.domain.model.Criterion;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.Rubric;


@AllArgsConstructor
public class CriterionService implements CriterionInt {

    private final CriterionRepositoryOutInt criterionRepository;
    private final RubricRepositoryOutInt rubricRepository;
    private final IAuthorizationService authorizationService;


    @Override
    public Criterion add(Criterion newCriterion, Integer rubricId, String uid) throws Exception {

        if(!authorizationService.canAccessRubric(uid, rubricId))
            throw new Unauthorized("you have not access to the rubric with id " + rubricId + " to add a criterion to it. ");

        OptionalWrapper<Rubric> rubricWrapper = rubricRepository.getById(rubricId);
        Optional<Rubric> rubricOpt = rubricWrapper.getValue();

        //TODO check if the rubric has the maximum number of criteria
        if(rubricOpt.isEmpty())
        {
            throw rubricWrapper.getException();
        }

        newCriterion.setRubric(rubricOpt.get());
        Rubric rubric = rubricOpt.get();

        if (!(rubric.getCriteria() instanceof ArrayList)) {
            rubric.setCriteria(new ArrayList<>(rubric.getCriteria()));
        }

        rubric.getCriteria().add(newCriterion);


        OptionalWrapper<Criterion> response = criterionRepository.add(newCriterion);
        return response.getValue().orElseThrow(response::getException);
    }

    @Override
    public List<Criterion> listAllByRubricId(Integer rubricId, String uid) throws Exception {
        if(!authorizationService.canAccessRubric(uid,rubricId))
            throw new Unauthorized("you have not access to the rubric with id " + rubricId + " to list its criteria.");
        return criterionRepository.listByRubricId(rubricId);
    }

    @Override
    public Criterion getById(Integer id, String uid) throws Exception {
        if(!authorizationService.canAccessCriterion(uid, id))
            throw new Unauthorized("you have not access to the rubric with id " + id + " to access its criteria. ");
        OptionalWrapper<Criterion> response = criterionRepository.getById(id);
        return response.getValue().orElseThrow(response::getException);
    }

    @Override
    public Criterion update(Integer id, Criterion newCriterion, String uid) throws Exception {
        if(!authorizationService.canAccessCriterion(uid, id))
            throw new Unauthorized("you have not access to the rubric with id " + id + " to update its criteria. ");
        OptionalWrapper<Criterion> response = criterionRepository.update(id, newCriterion);
        return response.getValue().orElseThrow(response::getException);    
    }

    @Override
    public Criterion remove(Integer id, String uid) throws Exception {
        if(!authorizationService.canAccessCriterion(uid, id))
            throw new Unauthorized("you have not access to the rubric with id " + id + " to remove its criteria. ");
        OptionalWrapper<Criterion> response = criterionRepository.remove(id);
        return response.getValue().orElseThrow(response::getException);
    }
}
