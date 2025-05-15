package unicauca.coreservice.domain.useCases;

import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import unicauca.coreservice.application.in.ProgramCompetencyAndOutcomeInt;
import unicauca.coreservice.application.in.SubjectOutcomeInt;
import unicauca.coreservice.application.out.CompetencyToSubjectAssignmentRepositoryOutInt;
import unicauca.coreservice.application.out.SubjectOutcomeRepositoryOutInt;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.CompetencyToSubjectAssignment;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.SubjectOutcome;

import java.util.List;

@AllArgsConstructor
public class SubjectOutcomeService implements SubjectOutcomeInt {

    private final SubjectOutcomeRepositoryOutInt repositorySubjectOutcome;
    private final CompetencyToSubjectAssignmentRepositoryOutInt assignRepository;
    private final ProgramCompetencyAndOutcomeInt programService;

    @Override
    public SubjectOutcome addSubjectOutcome(SubjectOutcome newSubjectOutcome, Integer competencyId, Integer subjectId) throws Exception {
        //Validate nulls
        if(null==newSubjectOutcome || null==competencyId || null==subjectId)
            throw new IllegalArgumentException("The parameters are not valid, they can not be null");
        //Validate if the competency is associated with the subject in the actual temp and activated
        OptionalWrapper<SubjectOutcome> response = repositorySubjectOutcome.add(newSubjectOutcome,competencyId);
        return response.getValue().orElseThrow(response::getException);
    }

    @Override
    public List<SubjectOutcome> listAll(Integer subjectId) {
        return List.of();
    }

    @Override
    public List<SubjectOutcome> listAllInCurrentPeriod(Integer subjectId) {
        return List.of();
    }

    @Override
    public List<SubjectOutcome> listAllByCompetencyId(Integer competencyId) {
        return List.of();
    }

    @Override
    public SubjectOutcome getById(Integer id) {
        return null;
    }

    @Override
    public SubjectOutcome update(Integer id, SubjectOutcome newSubjectOutcome) {
        return null;
    }

    @Override
    public SubjectOutcome remove(Integer id) {
        return null;
    }

    @Override
    public SubjectOutcome copy(Integer idRAAOriginal, Integer competencyId, Integer subjectId) {
        return null;
    }
}
