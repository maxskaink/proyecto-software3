package unicauca.coreservice.infrastructure.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.auth.AuthErrorCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import unicauca.coreservice.domain.exception.InvalidValue;
import unicauca.coreservice.domain.exception.NotFound;

@Service
public class FirebaseService {

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
    }}
