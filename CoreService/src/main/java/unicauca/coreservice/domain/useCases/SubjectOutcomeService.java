package unicauca.coreservice.domain.useCases;

import unicauca.coreservice.application.in.SubjectOutcomeInt;
import unicauca.coreservice.domain.model.SubjectOutcome;

import java.util.List;

public class SubjectOutcomeService implements SubjectOutcomeInt {
    @Override
    public SubjectOutcome addSubjectOutcome(SubjectOutcome newSubjectOutcome, Integer competencyId, Integer subjectId) {
        return null;
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
