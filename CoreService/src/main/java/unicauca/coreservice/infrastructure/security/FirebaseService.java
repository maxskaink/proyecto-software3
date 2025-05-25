package unicauca.coreservice.infrastructure.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.firebase.auth.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import unicauca.coreservice.application.out.ISecurityService;
import unicauca.coreservice.application.out.SubjectOutcomeRepositoryOutInt;
import unicauca.coreservice.application.out.TeacherAssignmentOutInt;
import unicauca.coreservice.application.out.TermRepositoryOutInt;
import unicauca.coreservice.domain.exception.InvalidValue;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.SubjectOutcome;
import unicauca.coreservice.domain.model.Term;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPAEvaluatorAssignmentRepository;
import unicauca.coreservice.infrastructure.SQLrepository.Repository.TeacherAssignmentRepository;

import javax.servlet.http.HttpServletRequest;

@Service
@AllArgsConstructor
public class FirebaseService implements ISecurityService {

    private final TeacherAssignmentOutInt teacherAssignmentRepository;
    private final SubjectOutcomeRepositoryOutInt subjectOutcomeRepository;
    private final JPAEvaluatorAssignmentRepository evaluatorAssignmentRepository;

    public void assignRole(String uid, String role) throws FirebaseAuthException {
        if (!List.of("Teacher", "Evaluator", "Coordinator").contains(role)) {
            throw new InvalidValue("Invalid role: " + role);
        }


        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        FirebaseAuth.getInstance().setCustomUserClaims(uid, claims);


        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("SERA").document(uid);
        Map<String, Object> updates = new HashMap<>();
        updates.put("role", role);
        docRef.update(updates);
    }


    public boolean userExists(String uid) {
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
            return userRecord != null;
        } catch (FirebaseAuthException e) {
            if (AuthErrorCode.USER_NOT_FOUND.name().equals(e.getErrorCode().name())) {
                return false;
            }
            throw new NotFound("User not found: " + uid + " (" + e.getErrorCode() + ")");
        }
    }

    @Override
    public boolean isCoordinator(String uid) throws Exception {
        try{
            if(!userExists(uid))
                throw new NotFound("User not found: " + uid);

            UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);

            Object roleClaim = userRecord.getCustomClaims().get("role");
            return roleClaim != null && "Coordinator".equals(roleClaim.toString());

        }catch (FirebaseAuthException e) {
            if (AuthErrorCode.USER_NOT_FOUND.name().equals(e.getErrorCode().name())) {
                return false;
            }
            throw new NotFound("User not found: " + uid + " (" + e.getErrorCode() + ")");
        }
    }

    @Override
    public boolean canAccessSubject(String uid, Integer subjectId) throws Exception {
        // Coordinator has total access to all subjects
        if (isCoordinator(uid)) {
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
        if (isCoordinator(uid)) {
            return true;
        }

        // 2. Get the outcome and the subjectId associated
        SubjectOutcome subjectOutcome = subjectOutcomeRepository.getById(subjectOutcomeId).getValue()
                .orElseThrow(() -> new NotFound("Subject outcome not found with id " + subjectOutcomeId));
        // Get the id of the subject and validate y have accesses to the subject
        Integer subjectId = subjectOutcome.getCompetencyAssignment().getSubject().getId();

        if (subjectId != null && canAccessSubject(uid, subjectId)) {
            return true;
        }

        // 3. If it has assigment to the outcome
        return evaluatorAssignmentRepository.findAll().stream()
                .anyMatch(ea -> ea.getSubjectOutcome().getId().equals(subjectOutcomeId));
    }

    @Override
    public String extractUidFromRequest(HttpServletRequest request) throws Exception {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new InvalidValue("No se encontró el token de autenticación");
        }
        String token = authorizationHeader.substring(7);
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
        return decodedToken.getUid();
    }
}
