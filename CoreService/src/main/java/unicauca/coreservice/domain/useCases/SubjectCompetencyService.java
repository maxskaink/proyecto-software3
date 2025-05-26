package unicauca.coreservice.domain.useCases;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import unicauca.coreservice.application.in.SubjectCompetencyInt;
import unicauca.coreservice.application.out.*;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.exception.Unauthorized;
import unicauca.coreservice.domain.model.*;
import unicauca.coreservice.infrastructure.SQLrepository.Repository.TermRepository;

import java.util.List;

@AllArgsConstructor
public class SubjectCompetencyService implements SubjectCompetencyInt {

    private final SubjectCompetencyRepositoryOutInt competencyRepository;
    private final CompetencyToSubjectAssignmentRepositoryOutInt assignRepository;
    private final TermRepository termRepository;
    private final SubjectOutcomeRepositoryOutInt subjectOutcomeRepository;
    private final IAuthenticationService authenticationService;
    private final IAuthorizationService authorizationService;

    @Override
    @Transactional
    public SubjectCompetency add(
            SubjectCompetency newSubjectCompetency,
            SubjectOutcome initialOutcome,
            Integer subjectId,
            String uid)throws Exception {

        Term activeTerm = termRepository.getActiveTerm().getValue()
                .orElseThrow(()->new RuntimeException("Active term doesnt exists"));
        
        //Validate authentication and authorization
        if(!authenticationService.isCoordinator(uid))
            if(!authorizationService.canAccessSubject(uid, subjectId))
                throw new Unauthorized("You are not authorized to add a new subject competency");

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

        CompetencyToSubjectAssignment response = responseAssignation.getValue()
                .orElseThrow(responseAssignation::getException);

        //Add First RA to the competency
        OptionalWrapper<SubjectOutcome> responseAddFirstOutcome =
                subjectOutcomeRepository.add(initialOutcome, response.getId());
        responseAddFirstOutcome.getValue().orElseThrow(responseAddFirstOutcome::getException);

        return response.getCompetency();
    }

    @Override
    public List<SubjectCompetency> listAllBySubjectId(
            Integer subjectId,
            String uid) throws Exception {
        //Validate authentication and authorization
        if(!authenticationService.isCoordinator(uid))
            if(!authorizationService.canAccessSubject(uid, subjectId))
                throw new Unauthorized("You are not authorized to add a new subject competency");

        return competencyRepository.listAllBySubjectId(subjectId);
    }

    @Override
    public SubjectCompetency getById(
            Integer id,
            String uid) throws Exception {
        OptionalWrapper<CompetencyToSubjectAssignment> assignationWrapper = assignRepository.getByCompetencyId(id);
        CompetencyToSubjectAssignment assignation = assignationWrapper.getValue()
                .orElseThrow(assignationWrapper::getException);

        Integer subjectId = assignation.getSubject().getId();

        if(!authenticationService.isCoordinator(uid))
            if(!authorizationService.canAccessSubject(uid, subjectId))
                throw new Unauthorized("You are not authorized to access this subject competency");

        //Validate authentication and authorization
        if(!authenticationService.isCoordinator(uid))
            if(!authorizationService.canAccessSubject(uid, subjectId))
                throw new Unauthorized("You are not authorized to add a new subject competency");

        return competencyRepository.getById(id).getValue()
                .orElseThrow(()->new NotFound("The competency with the id " + id + " doesnt exists"));
    }

    @Override
    public SubjectCompetency update(
            Integer id,
            SubjectCompetency newSubjectCompetency,
            String uid) throws Exception {

        OptionalWrapper<CompetencyToSubjectAssignment> assignationWrapper = assignRepository.getByCompetencyId(id);
        CompetencyToSubjectAssignment assignation = assignationWrapper.getValue()
                .orElseThrow(assignationWrapper::getException);

        Integer subjectId = assignation.getSubject().getId();

        if(!authenticationService.isCoordinator(uid))
            if(!authorizationService.canAccessSubject(uid, subjectId))
                throw new Unauthorized("You are not authorized to access this subject competency");

        OptionalWrapper<SubjectCompetency> response = competencyRepository.update(id, newSubjectCompetency);

        return response.getValue()
                .orElseThrow(response::getException);
    }

    @Transactional
    @Override
    public SubjectCompetency remove(
            Integer id,
            String uid) throws Exception {
        OptionalWrapper<CompetencyToSubjectAssignment> assignationWrapper = assignRepository.getByCompetencyId(id);
        CompetencyToSubjectAssignment assignation = assignationWrapper.getValue()
                .orElseThrow(assignationWrapper::getException);

        Integer subjectId = assignation.getSubject().getId();

        if(!authenticationService.isCoordinator(uid))
            if(!authorizationService.canAccessSubject(uid, subjectId))
                throw new Unauthorized("You are not authorized to access this subject competency");


        //Get the assignation of the active term

        assignation.getSubjectOutcomes().forEach(ra -> subjectOutcomeRepository.remove(ra.getId()));
        competencyRepository.remove(id);

        OptionalWrapper<CompetencyToSubjectAssignment> response =
                assignRepository.remove(assignation.getId());

        return response.getValue().orElseThrow(response::getException).getCompetency();
    }

}
