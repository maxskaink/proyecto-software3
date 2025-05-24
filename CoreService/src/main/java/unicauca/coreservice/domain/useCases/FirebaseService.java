package unicauca.coreservice.domain.useCases;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import unicauca.coreservice.domain.exception.InvalidValue;

@Service
public class FirebaseService {

public void assignRole(String uid, String role) throws FirebaseAuthException {
    if (!List.of("Teacher", "Evaluator", "Coordinator").contains(role)) {
        throw new InvalidValue("Invalid role: " + role);
    }

    // Asigna claim personalizado en Authentication
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", role);
    FirebaseAuth.getInstance().setCustomUserClaims(uid, claims);

    // También actualiza Firestore
    Firestore db = FirestoreClient.getFirestore();
    DocumentReference docRef = db.collection("SERA").document(uid); // Usa el mismo uid que en Firestore
    Map<String, Object> updates = new HashMap<>();
    updates.put("role", role);
    docRef.update(updates);
}
}
