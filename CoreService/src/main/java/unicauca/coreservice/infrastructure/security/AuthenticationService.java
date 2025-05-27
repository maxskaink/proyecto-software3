package unicauca.coreservice.infrastructure.security;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import unicauca.coreservice.application.out.IAuthenticationService;
import unicauca.coreservice.domain.exception.InvalidValue;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.exception.Unauthorized;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthenticationService implements IAuthenticationService {

    private UserRecord getUserRecord(String uid) throws FirebaseAuthException {
        UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
        if (userRecord == null) {
            throw new NotFound("User not found: " + uid);
        }
        return userRecord;
    }

    @Override
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

    @Override
    public boolean userExists(String uid) {
        try{
            getUserRecord(uid);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean isCoordinator(String uid) throws Exception {
        try{
            UserRecord userRecord = getUserRecord(uid);

            Object roleClaim = userRecord.getCustomClaims().get("role");
            return roleClaim != null && "Coordinator".equals(roleClaim.toString());

        }catch (FirebaseAuthException e) {
            throw new NotFound("User not found: " + uid + " (" + e.getErrorCode() + ")");
        }
    }
    @Override
    public String extractUidFromRequest(HttpServletRequest request) throws Exception {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new Unauthorized("No se encontró el token de autenticación, no tiene permisos");
        }
        String token = authorizationHeader.substring(7);
        try{
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            return decodedToken.getUid();
        } catch (FirebaseAuthException e) {
            throw new Unauthorized("Your token is invalid: " + e.getErrorCode() + " (" + e.getMessage() + ")");
        }
    }
}
