package unicauca.coreservice.application.out;

public interface IAuthorizationService {
    /**
     * Returns true if the user identified by its unique identifier has the role "Teacher" and
     * has access to the subject, or it is "Coordinator"
     * @param uid uid of the user to be validated.
     * @param subjectId id of the subject to be validated.
     * @return true if the user has the role "Teacher" and has access to the subject, or it is "Coordinator", false otherwise.
     * @throws Exception Depends on the technology to use.
     */
    boolean canAccessSubject(String uid, Integer subjectId) throws Exception;

    /**
     * Returns true if the user identified by its unique identifier has the role "Teacher" and has access to the subject outcome.
     * @param uid uid of the user to be validated.
     * @param subjectOutcomeId id of the subject outcome to be validated.
     * @return true if the user has the role "Teacher" and has access to the subject outcome, false otherwise.
     * @throws Exception Depends on the technology to use.
     */
    boolean canAccessSubjectOutcome(String uid, Integer subjectOutcomeId) throws Exception;

    /**
     * Returns true if the user identified by its unique identifier has the role "Teacher" and has access to the rubric.
     * @param uid uid of the user to be validated.
     * @param rubricId rubric id to be validated.
     * @return true if the user has the role "Teacher" and has access to the rubric, false otherwise.
     * @throws Exception Depends on the technology to use.
     */
    boolean canAccessRubric(String uid, Integer rubricId) throws Exception;

    /**
     * Returns true if the user identified by its unique identifier has the role "Teacher" and has access to the criterion.
     * @param uid uid of the user to be validated.
     * @param criterionId criterion id to be validated.
     * @return true if the user has the role "Teacher" and has access to the criterion, false otherwise.
     * @throws Exception Depends on the technology to use.
     */
    boolean canAccessCriterion(String uid, Integer criterionId) throws Exception;

    /**
     * Returns true if the user identified by its unique identifier has the role "Teacher" and has access to the level.
     * @param uid uid of the user to be validated.
     * @param levelId level id to be validated.
     * @return true if the user has the role "Teacher" and has access to the level, false otherwise.
     * @throws Exception Depends on the technology to use.
     */
    boolean canAccessLevel(String uid, Integer levelId) throws Exception;

}
