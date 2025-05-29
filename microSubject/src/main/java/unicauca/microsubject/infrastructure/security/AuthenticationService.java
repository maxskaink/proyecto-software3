package unicauca.microsubject.infrastructure.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.firebase.auth.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import unicauca.microsubject.application.out.IAuthenticationService;
import unicauca.microsubject.domain.exception.NotFound;
import unicauca.microsubject.domain.exception.Unauthorized;

import jakarta.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class AuthenticationService implements IAuthenticationService {

    private final Cache<String, UserRecord> userRecordCache;
    private final Cache<String, Boolean> coordinatorCache;
    private final Cache<String, String> tokenToUidCache;

    public AuthenticationService() {
        // Setup the time for cache
        this.userRecordCache = Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();

        this.coordinatorCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();

        this.tokenToUidCache = Caffeine.newBuilder()
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .maximumSize(2000)
                .build();
    }

    private UserRecord getUserRecord(String uid) throws FirebaseAuthException {

        UserRecord cachedRecord = userRecordCache.getIfPresent(uid);
        if (cachedRecord != null) {
            return cachedRecord;
        }

        UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);

        //Saved it in a cache
        userRecordCache.put(uid, userRecord);
        return userRecord;
    }

    @Override
    public boolean userExists(String uid) {
        try {
            getUserRecord(uid);
            return true;
        } catch (FirebaseAuthException e) {
            System.out.println("FirebaseAuthException: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Unexpected error checking user: " + e.getMessage());
            return false;
        }
    }


    @Override
    public boolean isCoordinator(String uid) throws Exception {
        // Verify in a cache
        Boolean isCoord = coordinatorCache.getIfPresent(uid);
        if (isCoord != null)
            return isCoord;

        try{
            UserRecord userRecord = getUserRecord(uid);

            Object roleClaim = userRecord.getCustomClaims().get("role");

            boolean result = roleClaim != null && "Coordinator".equals(roleClaim.toString());
            // Saved it in a cache
            coordinatorCache.put(uid, result);
            return result;

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

        // Verify in a cache
        String cachedUid = tokenToUidCache.getIfPresent(token);
        if (cachedUid != null)
            return cachedUid;

        try{
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String uid =  decodedToken.getUid();

            tokenToUidCache.put(token, uid);
            return uid;
        } catch (FirebaseAuthException e) {
            throw new Unauthorized("Your token is invalid: " + e.getErrorCode() + " (" + e.getMessage() + ")");
        }
    }
}
