package unicauca.microsubject.infrastructure.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import unicauca.microsubject.application.out.IAuthenticationService;
import unicauca.microsubject.application.out.IAuthorizationService;
import unicauca.microsubject.application.out.TeacherAssignmentRepositoryOutInt;

import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class AuthorizationService implements IAuthorizationService {

    private final TeacherAssignmentRepositoryOutInt teacherAssignmentRepository;
    private final IAuthenticationService authenticationService;

    // cache for subject access
    private final Cache<String, Boolean> subjectAccessCache = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();

    private String getCacheKey(String uid, Integer resourceId) {
        return uid + ":" + resourceId;
    }

    @Override
    public boolean canAccessSubject(String uid, Integer subjectId) throws Exception {
        //Verify in cache
        String cacheKey = getCacheKey(uid, subjectId);
        Boolean cachedResult = subjectAccessCache.getIfPresent(cacheKey);
        if (cachedResult != null)
            return cachedResult;

        // how it is not in cache, verify in database
        boolean result;

        // Coordinator has total access to all subjects
        if (authenticationService.isCoordinator(uid)) {
            subjectAccessCache.put(cacheKey, true);
            return true;
        }
        // Get the assignment to the subject
        result = teacherAssignmentRepository.listByActiveTerm().stream()
                .anyMatch(assignment ->
                        assignment.getTeacherUid().equals(uid) &&
                        assignment.getSubject().getId().equals(subjectId)
                );
        subjectAccessCache.put(cacheKey, result);
        return result;
    }


}
