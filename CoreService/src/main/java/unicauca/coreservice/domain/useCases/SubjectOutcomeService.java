package unicauca.coreservice.domain.useCases;

import lombok.AllArgsConstructor;
import unicauca.coreservice.application.in.SubjectOutcomeInt;
import unicauca.coreservice.application.out.SubjectOutcomeRepositoryOutInt;
import unicauca.coreservice.domain.exception.InvalidValue;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.SubjectOutcome;

import java.util.List;

@AllArgsConstructor
public class SubjectOutcomeService implements SubjectOutcomeInt {

    private final SubjectOutcomeRepositoryOutInt repositorySubjectOutcome;

    @Override
    public SubjectOutcome addSubjectOutcome(
            SubjectOutcome newSubjectOutcome,
            Integer competencyId,
            Integer subjectId
    ) throws Exception {
        List<SubjectOutcome> outcomes = repositorySubjectOutcome.listAllBySubjectId(subjectId, true);

        if(outcomes.size() >= 3){
            throw new InvalidValue("The subject has already 3 learning outcomes assigned int the active term1");
        }

        //Validate if the competency is associated with the subject in the actual temp and activated
        OptionalWrapper<SubjectOutcome> response = repositorySubjectOutcome.add(newSubjectOutcome,competencyId);

        //Add to subject competency
        return response.getValue().orElseThrow(response::getException);
    }

    @Override
    public List<SubjectOutcome> listAll(
            Integer subjectId
    ) {
        return repositorySubjectOutcome.listAllBySubjectId(subjectId, false);
    }

    @Override
    public List<SubjectOutcome> listAllInCurrentTerm(
            Integer subjectId
    ) {
        return repositorySubjectOutcome.listAllBySubjectId(subjectId, true);
    }

    @Override
    public List<SubjectOutcome> listAllByCompetencyId(
            Integer competencyId
    ) {
        return repositorySubjectOutcome.listAllByCompetencyId(competencyId);
    }

    @Override
    public SubjectOutcome getById(
            Integer id
    ) throws Exception {
        OptionalWrapper<SubjectOutcome> response = repositorySubjectOutcome.getById(id);

        return response.getValue().orElseThrow(response::getException);
    }

    @Override
    public SubjectOutcome update(
            Integer id,
            SubjectOutcome newSubjectOutcome
    ) throws Exception {

        OptionalWrapper<SubjectOutcome> response = repositorySubjectOutcome.update(id, newSubjectOutcome);
        return response.getValue().orElseThrow(response::getException);
    }


    @Override
    public SubjectOutcome remove(
            Integer id
    ) throws Exception {

        OptionalWrapper<SubjectOutcome> responseOutcome = repositorySubjectOutcome.remove(id);

        return responseOutcome.getValue()
                .orElseThrow(responseOutcome::getException);
    }

    @Override
    public SubjectOutcome copy(
            Integer idOutcomeOriginal,
            Integer competencyId,
            Integer subjectId
    ) throws Exception {

        OptionalWrapper<SubjectOutcome> responseOutcome =repositorySubjectOutcome.getById(idOutcomeOriginal);

        SubjectOutcome originalOutcome = responseOutcome.getValue()
                .orElseThrow(responseOutcome::getException);

        return addSubjectOutcome(originalOutcome,competencyId,subjectId);
    }
}
