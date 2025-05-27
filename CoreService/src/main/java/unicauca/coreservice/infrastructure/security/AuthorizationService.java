package unicauca.coreservice.infrastructure.security;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import unicauca.coreservice.application.out.IAuthenticationService;
import unicauca.coreservice.application.out.IAuthorizationService;
import unicauca.coreservice.application.out.SubjectOutcomeRepositoryOutInt;
import unicauca.coreservice.application.out.TeacherAssignmentRepositoryOutInt;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.SubjectOutcome;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPAEvaluatorAssignmentRepository;

@Service
@AllArgsConstructor
public class AuthorizationService implements IAuthorizationService {

    private final TeacherAssignmentRepositoryOutInt teacherAssignmentRepository;
    private final SubjectOutcomeRepositoryOutInt subjectOutcomeRepository;
    private final JPAEvaluatorAssignmentRepository evaluatorAssignmentRepository;
    private final IAuthenticationService authenticationService;

    @Override
    public boolean canAccessSubject(String uid, Integer subjectId) throws Exception {

        // Coordinator has total access to all subjects
        if (authenticationService.isCoordinator(uid)) {
            return true;
        }
        // Get the assignment to the subject
        return teacherAssignmentRepository.listByActiveTerm().stream()
                .anyMatch(assignment ->
                        assignment.getTeacherUid().equals(uid) &&
                                assignment.getSubject().getId().equals(subjectId)
                );
    }

    @Override
    public boolean canAccessSubjectOutcome(String uid, Integer subjectOutcomeId) throws Exception {

        // 1. If it is a coordinator having total access to all outcomes
        if (authenticationService.isCoordinator(uid)) {
            return true;
        }

        // 2. Get the outcome and the subjectId associated
        SubjectOutcome subjectOutcome = subjectOutcomeRepository.getById(subjectOutcomeId).getValue()
                .orElseThrow(() -> new NotFound("Subject outcome not found with id " + subjectOutcomeId));
        // Get the id of the subject and validate y have accesses to the subject
        Integer subjectId = subjectOutcome.getIdCompetencyAssignment();

        if (subjectId != null && canAccessSubject(uid, subjectId)) {
            return true;
        }

        // 3. If it has assigment to the outcome
        return evaluatorAssignmentRepository.findAll().stream()
                .anyMatch(ea -> ea.getSubjectOutcome().getId().equals(subjectOutcomeId));
    }

}
