package unicauca.microsubject.application.out;

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

}
