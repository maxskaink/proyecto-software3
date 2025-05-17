package unicauca.coreservice.domain.useCases;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import unicauca.coreservice.application.in.SubjectCompetencyInt;
import unicauca.coreservice.application.out.CompetencyToSubjectAssignmentRepositoryOutInt;
import unicauca.coreservice.application.out.SubjectCompetencyRepositoryOutInt;
import unicauca.coreservice.application.out.SubjectOutcomeRepositoryOutInt;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.*;
import unicauca.coreservice.infrastructure.SQLrepository.Repository.TermRepositoryInt;

import java.util.List;

@AllArgsConstructor
@Validated
public class SubjectCompetencyService implements SubjectCompetencyInt {

    private final SubjectCompetencyRepositoryOutInt competencyRepository;
    private final CompetencyToSubjectAssignmentRepositoryOutInt assignRepository;
    private final TermRepositoryInt termRepository;
    private final SubjectOutcomeRepositoryOutInt subjectOutcomeRepository;

    @Override
    @Transactional
    public SubjectCompetency add(
            @NotNull(message ="The new Subject competency can not be null")
            SubjectCompetency newSubjectCompetency,
            @NotNull(message = "The initial outcome can not be null")
            SubjectOutcome initialOutcome,
            @NotNull(message=" The subject id can not be null")
            Integer subjectId
    )throws Exception {

        Term activeTerm = termRepository.getActiveTerm().getValue()
                .orElseThrow(()->new RuntimeException("Active term doesnt exists"));

        //TODO FIX THIS LATER
        Subject subject = new Subject(subjectId, "name", "description");

        //Create competency in the main table
        OptionalWrapper<SubjectCompetency> responseCreate = competencyRepository.add(newSubjectCompetency);
        SubjectCompetency competency = responseCreate.getValue()
                .orElseThrow(responseCreate::getException);
        //Associate in the active term with the subject
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
    public List<SubjectCompetency> listAllBySubjectId(
            @NotNull(message="The id is not valid, it can not be null")
            Integer subjectId
    ) {
        return competencyRepository.listAllBySubjectId(subjectId);
    }

    @Override
    public SubjectCompetency getById(
            @NotNull(message="The id is not valid, it can not be null")
            Integer id
    ) {
        return competencyRepository.getById(id).getValue()
                .orElseThrow(()->new NotFound("The competency with the id " + id + " doesnt exists"));
    }

    @Override
    public SubjectCompetency update(
            @NotNull(message="The id is no valid, it can not be null")
            Integer id,
            @NotNull(message="Instance of competency is invalid, it can not be null")
            SubjectCompetency newSubjectCompetency
    ) throws Exception {
        OptionalWrapper<SubjectCompetency> response = competencyRepository.update(id, newSubjectCompetency);

        return response.getValue()
                .orElseThrow(response::getException);
    }

    @Transactional
    @Override
    public SubjectCompetency remove(
            @NotNull(message = "The id is invalid, it can not be null")
            Integer id
    ) throws Exception {
        //Get the assignation of the active term
        OptionalWrapper<CompetencyToSubjectAssignment> assignationWrapper =
                assignRepository.getByCompetencyId(id);

        CompetencyToSubjectAssignment assignation= assignationWrapper.getValue()
                .orElseThrow(assignationWrapper::getException);

        assignation.getSubjectOutcomes().forEach(ra -> subjectOutcomeRepository.remove(ra.getId()));
        competencyRepository.remove(id);

        OptionalWrapper<CompetencyToSubjectAssignment> response =
                assignRepository.remove(assignation.getId());

        return response.getValue().orElseThrow(response::getException).getCompetency();
    }

}
