package unicauca.coreservice.application.out;

import javax.servlet.http.HttpServletRequest;

public interface ISecurityService {
    /**
     * Assigns a role to a user identified by its unique identifier.
     * @param uid uid of the user to be assigned a role.
     * @param role it can be "Teacher", "Evaluator" or "Coordinator"
     * @throws Exception Depends on the technology to use
     */
    void assignRole(String uid, String role) throws Exception;

    /**
     * Validate if a user exists in the security service.
     * @param uid uid of the user to be validated.
     * @return true if the user exists, false otherwise.
     * @throws Exception Depends on the technology to use.
     */
    boolean userExists(String uid) throws Exception;

    /**
     * Returns true if the user identified by its unique identifier has the role "Coordinator".
     * @param uid uid of the user to be validated.
     * @return true if the user has the role "Coordinator", false otherwise.
     * @throws Exception Depends on the technology to use.
     */
    boolean isCoordinator(String uid) throws Exception;

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
     * Returns true if the user identified by its unique identifier has the role "Teacher"
     * @param uid uid of the user to be validated.
     * @param subjectOutcomeId id of the subject outcome to be validated.
     * @return true if the user has the role "Teacher" and has access to the subject outcome, false otherwise.
     * @throws Exception Depends on the technology to use.
     */
    boolean canAccessSubjectOutcome(String uid, Integer subjectOutcomeId) throws Exception;

    /**
     * Extract IUD from the request
     * @param request request to extract the token
     * @return the UID extracted
     * @throws Exception Depends on the technology to use.
     */
    String extractUidFromRequest(HttpServletRequest request) throws Exception;
}
