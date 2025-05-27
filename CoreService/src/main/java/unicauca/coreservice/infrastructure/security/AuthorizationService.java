package unicauca.coreservice.infrastructure.security;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import unicauca.coreservice.application.out.*;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.*;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPAEvaluatorAssignmentRepository;

@Service
@AllArgsConstructor
public class AuthorizationService implements IAuthorizationService {

    private final TeacherAssignmentRepositoryOutInt teacherAssignmentRepository;
    private final SubjectOutcomeRepositoryOutInt subjectOutcomeRepository;
    private final JPAEvaluatorAssignmentRepository evaluatorAssignmentRepository;
    private final CompetencyToSubjectAssignmentRepositoryOutInt competencyToSubjectAssignment;
    private final IAuthenticationService authenticationService;
    private final RubricRepositoryOutInt rubricRepository;
    private final TermRepositoryOutInt termRepository;
    private final CriterionRepositoryOutInt criterionRepository;
    private final LevelRepositoryOutInt levelRepository;

    @Override
    public boolean canAccessSubject(String uid, Integer subjectId) throws Exception {

        // Coordinator has total access to all subjects
        if (authenticationService.isCoordinator(uid)) {
            return true;
        }

        Term activeTerm = termRepository.getActiveTerm().getValue()
                .orElseThrow(() -> new RuntimeException("No active term found"));
        // Get the assignment to the subject
        return teacherAssignmentRepository.listByActiveTerm().stream()
                .anyMatch(assignment ->
                        assignment.getTeacherUid().equals(uid) &&
                        assignment.getSubject().getId().equals(subjectId) &&
                        assignment.getTerm().getId().equals(activeTerm.getId())
                );
    }

    @Override
    public boolean canAccessSubjectOutcome(String uid, Integer subjectOutcomeId) throws Exception {

        // 1. If it is a coordinator having total access to all outcomes
        if (authenticationService.isCoordinator(uid)) {
            return false;
        }

        // 2. Get the outcome and the subjectId associated
        SubjectOutcome subjectOutcome = subjectOutcomeRepository.getById(subjectOutcomeId).getValue()
                .orElseThrow(() -> new NotFound("Subject outcome not found with id " + subjectOutcomeId));
        // Get the id of the subject and validate y have accesses to the subject
        Integer assignmentID = subjectOutcome.getIdCompetencyAssignment();
        Integer subjectId = competencyToSubjectAssignment.getById(assignmentID)
                .getValue().orElseThrow(()->new NotFound("Outcome no associated with a signature")).getSubject().getId();
        if(canAccessSubject(uid, subjectId))
            return true;

        //TODO Validate if is an evaluator

        return false;
    }

    @Override
    public boolean canAccessRubric(String uid, Integer rubricId) throws Exception {

        Rubric rubric = rubricRepository.getById(rubricId).getValue()
                .orElseThrow(() -> new NotFound("Rubric not found with id " + rubricId));
        Integer assignmentID = rubric.getSubjectOutcome().getIdCompetencyAssignment();
        Integer subjectId = competencyToSubjectAssignment.getById(assignmentID)
                .getValue().orElseThrow(()->new NotFound("Outcome no associated with a signature")).getSubject().getId();

        return !canAccessSubject(uid, subjectId);
    }

    @Override
    public boolean canAccessCriterion(String uid, Integer criterionId) throws Exception {

        Criterion criterion = criterionRepository.getById(criterionId).getValue()
                .orElseThrow(() -> new NotFound("Criterion not found with id " + criterionId));

        Integer assignmentID = criterion.getRubric().getSubjectOutcome().getIdCompetencyAssignment();
        Integer subjectId = competencyToSubjectAssignment.getById(assignmentID)
                .getValue().orElseThrow(()->new NotFound("Outcome no associated with a signature")).getSubject().getId();
        return canAccessSubject(uid, subjectId);
    }

    @Override
    public boolean canAccessLevel(String uid, Integer levelId) throws Exception {

        Level level = levelRepository.getById(levelId).getValue()
                .orElseThrow(()->new NotFound("Level not found with id " + levelId));

        Integer assignmentID = level.getCriterion().getRubric().getSubjectOutcome().getIdCompetencyAssignment();

        Integer subjectId = competencyToSubjectAssignment.getById(assignmentID)
                .getValue().orElseThrow(()->new NotFound("Outcome no associated with a signature")).getSubject().getId();
        return canAccessSubject(uid, subjectId);
    }

}
