package unicauca.coreservice.domain.useCases;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import unicauca.coreservice.application.in.CriterionInt;
import unicauca.coreservice.application.out.CriterionRepositoryOutInt;
import unicauca.coreservice.application.out.RubricRepositoryOutInt;
import unicauca.coreservice.domain.model.Criterion;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.Rubric;


@AllArgsConstructor
public class CriterionService implements CriterionInt {

    private final CriterionRepositoryOutInt criterionRepository;
    private final RubricRepositoryOutInt rubricRepository;


    @Override
    public Criterion add(Criterion newCriterion, Integer rubricId) throws Exception {
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
    public List<Criterion> listAllByRubricId(Integer rubricId) {
        return criterionRepository.listByRubricId(rubricId);
    }

    @Override
    public Criterion getById(Integer id) throws Exception {
        OptionalWrapper<Criterion> response = criterionRepository.getById(id);
        return response.getValue().orElseThrow(response::getException);
    }

    @Override
    public Criterion update(Integer id, Criterion newCriterion) throws Exception {
        OptionalWrapper<Criterion> response = criterionRepository.update(id, newCriterion);
        return response.getValue().orElseThrow(response::getException);    
    }

    @Override
    public Criterion remove(Integer id) throws Exception {
        OptionalWrapper<Criterion> response = criterionRepository.remove(id);
        return response.getValue().orElseThrow(response::getException);
    }
}
