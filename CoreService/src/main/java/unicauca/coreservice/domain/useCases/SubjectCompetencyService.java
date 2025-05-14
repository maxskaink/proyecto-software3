package unicauca.coreservice.domain.useCases;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import unicauca.coreservice.application.in.SubjectCompetencyInt;
import unicauca.coreservice.application.out.CompetencyToSubjectAssignmentRepositoryOutInt;
import unicauca.coreservice.application.out.SubjectCompetencyRepositoryOutInt;
import unicauca.coreservice.application.out.SubjectOutcomeRepositoryOutInt;
import unicauca.coreservice.domain.exception.InvalidValue;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.*;
import unicauca.coreservice.infrastructure.SQLrepository.Repository.TermRepositoryInt;

import java.util.List;

@AllArgsConstructor
public class SubjectCompetencyService implements SubjectCompetencyInt {

    private final SubjectCompetencyRepositoryOutInt competencyRepository;
    private final CompetencyToSubjectAssignmentRepositoryOutInt assignRepository;
    private final TermRepositoryInt termRepository;
    private final SubjectOutcomeRepositoryOutInt subjectOutcomeRepository;

    @Override
    @Transactional
    public SubjectCompetency add(SubjectCompetency newSubjectCompetency, SubjectOutcome initialOutcome, Integer subjectId) throws Exception {
        //TODO Mirar como hacer para que valide que ya tenga una RA
        //Validate some information
        if(null==newSubjectCompetency || null == subjectId)
            throw new InvalidValue("Instance of competency is not valid, it can not be null");
        if(null== initialOutcome)
            throw new InvalidValue("Instance of Outcome is not valid, it can not be null");

        Term activeTerm = termRepository.getActiveTerm().getValue()
                .orElseThrow(()->new RuntimeException("Active term doesnt exists"));

        //TODO FIX THIS LATER
        Subject subject = new Subject(subjectId, "name", "description");

        //Create competency in the main table
        OptionalWrapper<SubjectCompetency> responseCreate = competencyRepository.add(newSubjectCompetency);
        SubjectCompetency competency = responseCreate.getValue()
                .orElseThrow(responseCreate::getException);
        //Asociate in the active term with subject
        CompetencyToSubjectAssignment assignation = new CompetencyToSubjectAssignment(
                null,
                competency,
                subject,
                activeTerm,
                null
        );
        OptionalWrapper<CompetencyToSubjectAssignment> responseAssignation =
                assignRepository.add(assignation);

        SubjectCompetency response = responseAssignation.getValue()
                .orElseThrow(responseAssignation::getException).getCompetency();

        //Add First RA to the competency
        OptionalWrapper<SubjectOutcome> responseAddFirstOutcome =
                subjectOutcomeRepository.add(initialOutcome, response.getId());
        responseAddFirstOutcome.getValue().orElseThrow(responseAddFirstOutcome::getException);

        return response;
    }

    @Override
    public List<SubjectCompetency> listAllBySubjectId(Integer subjectId) {
        if(null==subjectId)
            throw new InvalidValue("The id is not valid, it can not be null");
        return competencyRepository.listAllBySubjectId(subjectId);
    }

    @Override
    public SubjectCompetency getById(Integer id) {
        if(null==id)
            throw new InvalidValue("The id is not valid, it can not be null");
        return competencyRepository.getById(id).getValue()
                .orElseThrow(()->new NotFound("The competency with the id " + id + " doesnt exists"));
    }

    @Override
    public SubjectCompetency update(Integer id, SubjectCompetency newSubjectCompetency) throws Exception {
        if(null == id)
            throw new InvalidValue("The id is not valid, it can not be null");
        if(null == newSubjectCompetency)
            throw new InvalidValue("Instance of competency is invalid, it can not be null");
        OptionalWrapper<SubjectCompetency> response = competencyRepository.update(id, newSubjectCompetency);

        return response.getValue()
                .orElseThrow(response::getException);
    }

    @Transactional
    @Override
    public SubjectCompetency remove(Integer id) throws Exception {
        //Get the assignation of the active term
        OptionalWrapper<CompetencyToSubjectAssignment> assignationWrapper =
                assignRepository.getBySubjectId(id);

        CompetencyToSubjectAssignment assignation= assignationWrapper.getValue()
                .orElseThrow(assignationWrapper::getException);

        assignation.getSubjectOutcomes().forEach(ra -> {
            subjectOutcomeRepository.remove(ra.getId());
        });
        competencyRepository.remove(id);

        OptionalWrapper<CompetencyToSubjectAssignment> response =
                assignRepository.remove(assignation.getId());

        return response.getValue().orElseThrow(response::getException).getCompetency();
    }

}
