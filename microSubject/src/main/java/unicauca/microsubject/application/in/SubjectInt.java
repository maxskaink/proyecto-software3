package unicauca.microsubject.application.in;

import jakarta.validation.constraints.NotNull;
import unicauca.microsubject.domain.model.Subject;

import java.util.List;

public interface SubjectInt {
    /**
     * Add a subject in the system
     * @param subject instance of the subject
     * @param uid uid of the user to validate authorization
     * @return the subject added to the system
     */
    Subject add(
            @NotNull(message = "instance of subject can not be null")
            Subject subject,
            @NotNull(message = "the uid can not be null")
            String uid
    ) throws Exception;

    /**
     * list all the subjects in the system
     * @param uid uid of the user to validate authorization
     * @return list of all the subjects
     */
    List<Subject> listAll(
            @NotNull(message = "the uid can not be null")
            String uid
    ) throws Exception;

    /**
     * list all the assigned subjects with the uid
     * @param uid uid of the user to validate authorization
     * @return list of assigned subjets
     */
    List<Subject> listAssigned(
            @NotNull(message = "the uid can not be null")
            String uid
    ) throws Exception;

    /**
     * list all the subject in the system
     * @param id id of the subject to find
     * @param uid uid of the user to validate authorization
     * @return instance of the subject
     */
    Subject getById(
            @NotNull(message ="id can not be null")
            Integer id,
            @NotNull(message = "the uid can not be null")
            String uid
    ) throws Exception;

    /**
     * update a subject in the system
     * @param id id of the subject to update
     * @param subject instance of the subject to update
     * @param uid uid of the user to validate authorization
     * @return instance of the new subject
     */
    Subject update(
            @NotNull(message ="id can not be null")
            Integer id,
            @NotNull(message =  "instance of subject can not be null")
            Subject subject,
            @NotNull(message = "the uid can not be null")
            String uid
    ) throws Exception;

    /**
     * remove a subject of the system
     * @param id id of the subject to remove
     * @param uid uid of the user to validate authorization
     * @return an instance of the removed subject
     */
    Subject remove(
            @NotNull(message = "id can not be null")
            Integer id,
            @NotNull(message = "the uid can not be null")
            String uid
    ) throws Exception;
}
