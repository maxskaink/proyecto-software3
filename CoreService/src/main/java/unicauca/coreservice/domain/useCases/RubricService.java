package unicauca.coreservice.domain.useCases;

import lombok.AllArgsConstructor;
import unicauca.coreservice.application.in.RubricInt;
import unicauca.coreservice.application.out.RubricRepositoryOutInt;
import unicauca.coreservice.domain.exception.DuplicateInformation;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.Rubric;
import unicauca.coreservice.domain.model.SubjectOutcome;
import unicauca.coreservice.infrastructure.SQLrepository.Repository.SubjectOutcomeRepository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class RubricService implements RubricInt{


    private final RubricRepositoryOutInt rubricRepository;
    private final SubjectOutcomeRepository subjectOutcomeRepository;


    @Override
    public Rubric add(Rubric newRubric, Integer subjectOutcomeId) throws Exception {
        OptionalWrapper<Rubric> optionalRubric = rubricRepository.getBySubjectOutcomeId(subjectOutcomeId);
        if (optionalRubric.getValue().isPresent()) {    
            throw new DuplicateInformation(
                "The subject outcome with id " + subjectOutcomeId + " already has a rubric associated with it."
            );
        }
        OptionalWrapper<SubjectOutcome> subjectOutcomeWrapper = subjectOutcomeRepository.getById(subjectOutcomeId);
        Optional<SubjectOutcome> subjectOutcomeOpt = subjectOutcomeWrapper.getValue();


        if (subjectOutcomeOpt.isEmpty()) {
            throw subjectOutcomeWrapper.getException();
        }

        newRubric.setSubjectOutcome(subjectOutcomeOpt.get());
        subjectOutcomeOpt.get().setRubric(newRubric);

        OptionalWrapper<Rubric> response = rubricRepository.add(newRubric);
        
        return response.getValue().orElseThrow(response::getException);
    }

    @Override
    public List<Rubric> listAllBySubjectId(Integer subjectId) {
        return rubricRepository.listAllBySubjectId(subjectId);
    }

    @Override
    public Rubric getById(Integer id) throws Exception {
        OptionalWrapper<Rubric> response = rubricRepository.getById(id);

        return response.getValue().orElseThrow(response::getException);
    }

    @Override
    public Rubric getBySubjectOutcomeId(Integer subjectOutcomeId) throws Exception {
        OptionalWrapper<Rubric> response = rubricRepository.getBySubjectOutcomeId(subjectOutcomeId);
        return response.getValue().orElseThrow(response::getException);
    }

    @Override
    public Rubric update(Integer id, Rubric newRubric) throws Exception {
        OptionalWrapper<Rubric> response = rubricRepository.update(id, newRubric);
        return response.getValue().orElseThrow(response::getException);
    }

    @Override
    public Rubric remove(Integer id) throws Exception {
        OptionalWrapper<Rubric> response = rubricRepository.remove(id);
        return response.getValue().orElseThrow(response::getException);
    }
}
