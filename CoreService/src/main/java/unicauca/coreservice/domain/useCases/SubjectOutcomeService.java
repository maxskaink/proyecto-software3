package unicauca.coreservice.domain.useCases;

import lombok.AllArgsConstructor;
import unicauca.coreservice.application.in.SubjectOutcomeInt;
import unicauca.coreservice.application.out.CompetencyToSubjectAssignmentRepositoryOutInt;
import unicauca.coreservice.application.out.IAuthorizationService;
import unicauca.coreservice.application.out.SubjectOutcomeRepositoryOutInt;
import unicauca.coreservice.domain.exception.InvalidValue;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.exception.Unauthorized;
import unicauca.coreservice.domain.model.CompetencyToSubjectAssignment;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.SubjectOutcome;

import java.util.List;

@AllArgsConstructor
public class SubjectOutcomeService implements SubjectOutcomeInt {

    private final SubjectOutcomeRepositoryOutInt repositorySubjectOutcome;
    private final CompetencyToSubjectAssignmentRepositoryOutInt assignmentCompetencyRepository;
    private final IAuthorizationService authorizationService;

    /**
     * Checks if the user is authorized to add a learning outcome to the specified subject
     * @param uid uid of the user trying to add a learning outcome to the subject. It must be a coordinator or a teacher of the subject.
     * @param subjectId id of the subject to which the learning outcome will be added.
     * @throws InvalidValue if the user is not authorized to add a learning outcome to the subject.
     */
    private void canAccessSubject(String uid, Integer subjectId) throws Exception {
        //Validate authorization of the user
        if(!authorizationService.canAccessSubject(uid, subjectId))
            throw new InvalidValue("You are not authorized to add a learning outcome to this subject");
    }


    @Override
    public SubjectOutcome addSubjectOutcome(
            SubjectOutcome newSubjectOutcome,
            Integer competencyId,
            Integer subjectId,
            String uid
    ) throws Exception {
        canAccessSubject(uid, subjectId);
        List<SubjectOutcome> outcomes = repositorySubjectOutcome.listAllByCompetencyId(competencyId);
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
            Integer subjectId,
            String uid
    ) throws Exception {
        canAccessSubject(uid,subjectId);
        return repositorySubjectOutcome.listAllBySubjectId(subjectId, false);
    }

    @Override
    public List<SubjectOutcome> listAllInCurrentTerm(
            Integer subjectId,
            String uid
    ) throws Exception {
        canAccessSubject(uid, subjectId);
        return repositorySubjectOutcome.listAllBySubjectId(subjectId, true);
    }

    @Override
    public List<SubjectOutcome> listAllByCompetencyId(
            Integer competencyId,
            String uid
    ) throws Exception {
        CompetencyToSubjectAssignment assignment = this.assignmentCompetencyRepository.getByCompetencyId(competencyId)
                .getValue().orElseThrow(()->new NotFound("Competency with id " + competencyId + " not found in the system. Please check the competency id and try again."));

        canAccessSubject(uid,assignment.getSubject().getId());

        return repositorySubjectOutcome.listAllByCompetencyId(competencyId);
    }

    @Override
    public SubjectOutcome getById(
            Integer id,
            String uid
    ) throws Exception {
        if(!authorizationService.canAccessSubjectOutcome(uid, id))
            throw new Unauthorized("You are not authorized to access this learning outcome");
        OptionalWrapper<SubjectOutcome> response = repositorySubjectOutcome.getById(id);
        return response.getValue().orElseThrow(response::getException);
    }

    @Override
    public SubjectOutcome update(
            Integer id,
            SubjectOutcome newSubjectOutcome,
            String uid
    ) throws Exception {
        if(!authorizationService.canAccessSubjectOutcome(uid, id))
            throw new Unauthorized("You are not authorized to access this learning outcome");
        OptionalWrapper<SubjectOutcome> response = repositorySubjectOutcome.update(id, newSubjectOutcome);
        return response.getValue().orElseThrow(response::getException);
    }


    @Override
    public SubjectOutcome remove(
            Integer id,
            String uid
    ) throws Exception {
        if(!authorizationService.canAccessSubjectOutcome(uid, id))
            throw new Unauthorized("You are not authorized to access this learning outcome");
        OptionalWrapper<SubjectOutcome> responseOutcome = repositorySubjectOutcome.remove(id);
        return responseOutcome.getValue()
                .orElseThrow(responseOutcome::getException);
    }

    @Override
    public SubjectOutcome copy(
            Integer idOutcomeOriginal,
            Integer competencyId,
            Integer subjectId,
            String uid
    ) throws Exception {
        canAccessSubject(uid, subjectId);
        OptionalWrapper<SubjectOutcome> responseOutcome =repositorySubjectOutcome.getById(idOutcomeOriginal);

        SubjectOutcome originalOutcome = responseOutcome.getValue()
                .orElseThrow(responseOutcome::getException);

        return addSubjectOutcome(originalOutcome,competencyId,subjectId, uid);
    }
}
