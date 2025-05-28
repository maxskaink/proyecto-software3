package unicauca.coreservice.application.out;

import jakarta.servlet.http.HttpServletRequest;

public interface IAuthenticationService {
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
     * Extract IUD from the request
     * @param request request to extract the token
     * @return the UID extracted
     * @throws Exception Depends on the technology to use.
     */
    String extractUidFromRequest(HttpServletRequest request) throws Exception;

}
