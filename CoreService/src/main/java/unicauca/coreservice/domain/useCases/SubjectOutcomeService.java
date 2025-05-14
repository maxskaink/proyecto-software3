package unicauca.coreservice.domain.useCases;

import unicauca.coreservice.application.in.SubjectOutcomeInt;
import unicauca.coreservice.domain.model.SubjectOutcome;

import java.util.List;

public class RaAsignaturaService implements SubjectOutcomeInt {
    @Override
    public SubjectOutcome addSubjectOutcome(SubjectOutcome newSubjectOutcome, Integer idCompetencia, Integer idAsignatura) {
        return null;
    }

    @Override
    public List<SubjectOutcome> listSubjectOutcomes(Integer idAsignatura) {
        return List.of();
    }

    @Override
    public List<SubjectOutcome> listSubjectOutcomesInCurrentPeriod(Integer idAsignatura) {
        return List.of();
    }

    @Override
    public List<SubjectOutcome> listSubjectOutcomeByCompetency(Integer idCompetencia) {
        return List.of();
    }

    @Override
    public SubjectOutcome getSubjectOutcomeById(Integer id) {
        return null;
    }

    @Override
    public SubjectOutcome updateSubjectOutcome(Integer id, SubjectOutcome newSubjectOutcome) {
        return null;
    }

    @Override
    public SubjectOutcome removeSubjectOutcome(Integer id) {
        return null;
    }

    @Override
    public SubjectOutcome copySubjectOutcome(Integer idRAAOriginal, Integer idCompetencia, Integer idAsignatura) {
        return null;
    }
}
