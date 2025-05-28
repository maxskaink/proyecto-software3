package unicauca.coreservice.infrastructure.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import unicauca.coreservice.application.out.*;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.*;

import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class AuthorizationService implements IAuthorizationService {

    private final TeacherAssignmentRepositoryOutInt teacherAssignmentRepository;
    private final SubjectOutcomeRepositoryOutInt subjectOutcomeRepository;
    private final EvaluatorAssignmentRepositoryOutInt evaluatorAssignmentRepository;
    private final CompetencyToSubjectAssignmentRepositoryOutInt competencyToSubjectAssignment;
    private final IAuthenticationService authenticationService;
    private final RubricRepositoryOutInt rubricRepository;
    private final CriterionRepositoryOutInt criterionRepository;
    private final LevelRepositoryOutInt levelRepository;

    // cache for subject access
    private final Cache<String, Boolean> subjectAccessCache = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();

    // cache for subject outcome access
    private final Cache<String, Boolean> outcomeAccessCache = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();

    // cache for rubric access
    private final Cache<String, Boolean> rubricAccessCache = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();

    // cache for criterion access
    private final Cache<String, Boolean> criterionAccessCache = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();

    // cache for level access
    private final Cache<String, Boolean> levelAccessCache = Caffeine.newBuilder()
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

    @Override
    public boolean canAccessSubjectOutcome(String uid, Integer subjectOutcomeId) throws Exception {

        // verify in cache first
        String cacheKey = getCacheKey(uid, subjectOutcomeId);
        Boolean cachedResult = outcomeAccessCache.getIfPresent(cacheKey);
        if (cachedResult != null)
            return cachedResult;

        // coordinator has total access to all outcomes
        if (authenticationService.isCoordinator(uid)) {
            outcomeAccessCache.put(cacheKey, true);
            return true;
        }

        // 2. Get the outcome and the subjectId associated
        SubjectOutcome subjectOutcome = subjectOutcomeRepository.getById(subjectOutcomeId).getValue()
                .orElseThrow(() -> new NotFound("Subject outcome not found with id " + subjectOutcomeId));
        // Get the id of the subject and validate y have accesses to the subject
        Integer assignmentID = subjectOutcome.getIdCompetencyAssignment();
        Integer subjectId = competencyToSubjectAssignment.getById(assignmentID)
                .getValue().orElseThrow(()->new NotFound("Outcome no associated with a signature")).getSubject().getId();
        if(canAccessSubject(uid, subjectId)){
            outcomeAccessCache.put(cacheKey, true);
            return true;
        }

        boolean result = evaluatorAssignmentRepository.listByEvaluatorUid(uid).stream()
                .anyMatch(evaluatorAssignment ->
                        evaluatorAssignment.getSubjectOutcome().getId().equals(subjectOutcomeId));
        outcomeAccessCache.put(cacheKey, result);
        return result;
    }

    @Override
    public boolean canAccessRubric(String uid, Integer rubricId) throws Exception {
        //Verify in cache
        String cacheKey = getCacheKey(uid, rubricId);
        Boolean cachedResult = rubricAccessCache.getIfPresent(cacheKey);
        if (cachedResult != null) {
            return cachedResult;
        }

        // Coordinator has total access to all rubrics
        if (authenticationService.isCoordinator(uid)) {
            rubricAccessCache.put(cacheKey, true);
            return true;
        }

        Rubric rubric = rubricRepository.getById(rubricId).getValue()
                .orElseThrow(() -> new NotFound("Rubric not found with id " + rubricId));
        Integer assignmentID = rubric.getSubjectOutcome().getIdCompetencyAssignment();
        Integer subjectId = competencyToSubjectAssignment.getById(assignmentID)
                .getValue().orElseThrow(()->new NotFound("Outcome no associated with a signature")).getSubject().getId();

        boolean result = canAccessSubject(uid, subjectId) || canAccessSubjectOutcome(uid, rubric.getSubjectOutcome().getId());

        rubricAccessCache.put(cacheKey, result);
        return result;
    }

    @Override
    public boolean canAccessCriterion(String uid, Integer criterionId) throws Exception {

        //Verify cache first
        String cacheKey = getCacheKey(uid, criterionId);
        Boolean cachedResult = criterionAccessCache.getIfPresent(cacheKey);
        if (cachedResult != null)
            return cachedResult;


        // Coordinator has total access to all criteria
        if (authenticationService.isCoordinator(uid)) {
            criterionAccessCache.put(cacheKey, true);
            return true;
        }

        Criterion criterion = criterionRepository.getById(criterionId).getValue()
                .orElseThrow(() -> new NotFound("Criterion not found with id " + criterionId));
        boolean result= canAccessRubric(uid, criterion.getRubric().getId());
        criterionAccessCache.put(cacheKey, result);
        return result;
    }

    @Override
    public boolean canAccessLevel(String uid, Integer levelId) throws Exception {
        //Verify cache first
        String cacheKey = getCacheKey(uid, levelId);
        Boolean cachedResult = levelAccessCache.getIfPresent(cacheKey);
        if (cachedResult != null)
            return cachedResult;

        // Coordinator has total access to all criteria
        if (authenticationService.isCoordinator(uid)) {
            criterionAccessCache.put(cacheKey, true);
            return true;
        }
        Level level = levelRepository.getById(levelId).getValue()
                .orElseThrow(()->new NotFound("Level not found with id " + levelId));

        boolean result = canAccessCriterion(uid,level.getCriterion().getId());

        levelAccessCache.put(cacheKey, result);
        return result;

    }

}
