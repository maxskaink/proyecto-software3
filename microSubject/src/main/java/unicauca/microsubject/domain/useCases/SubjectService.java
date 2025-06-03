package unicauca.microsubject.domain.useCases;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import unicauca.microsubject.application.in.SubjectInt;
import unicauca.microsubject.application.out.IAuthenticationService;
import unicauca.microsubject.application.out.IAuthorizationService;
import unicauca.microsubject.application.out.SubjectRepositoryOutInt;
import unicauca.microsubject.application.out.TeacherAssignmentRepositoryOutInt;
import unicauca.microsubject.domain.exception.DuplicateInformation;
import unicauca.microsubject.domain.exception.Unauthorized;
import unicauca.microsubject.domain.model.OptionalWrapper;
import unicauca.microsubject.domain.model.Subject;
import unicauca.microsubject.domain.model.TeacherAssignment;

import java.util.List;
import java.util.Objects;

@Validated
@AllArgsConstructor
public class SubjectService implements SubjectInt {

    private final IAuthenticationService authenticationService;
    private final IAuthorizationService authorizationService;
    private final SubjectRepositoryOutInt subjectRepository;
    private final TeacherAssignmentRepositoryOutInt assignmentRepository;

    @Override
    public Subject add(Subject subject, String uid) throws Exception {

        //Validate authorization
        if(!authenticationService.isCoordinator(uid))
            throw new Unauthorized("You have not permission to create a subject");

        //Validate duplicates
        String name =subject.getName();
        if(subjectRepository.getByName(name).getValue().isPresent())
            throw new DuplicateInformation("subject with name " + name + " already exist");

        OptionalWrapper<Subject> response = subjectRepository.add(subject);

        return response.getValue().orElseThrow(response::getException);
    }

    @Override
    public List<Subject> listAll(String uid) throws Exception {

        //Validate authorization
        if(!authenticationService.isCoordinator(uid))
            throw new Unauthorized("You are have no access to this content");

        return subjectRepository.listAll();
    }

    @Override
    public List<Subject> listAssigned(String uid) throws Exception {

        //Validate permission
        if(!authenticationService.userExists(uid))
            throw new Unauthorized("You have not permission");

        //If the user is a coordinator, it will have access to all subjects
        if(authenticationService.isCoordinator(uid))
            return subjectRepository.listAll();

        return assignmentRepository.listByTeacherUid(uid).stream()
                .map(TeacherAssignment::getSubject).toList();
    }

    @Override
    public Subject getById(Integer id, String uid) throws Exception {
        //Validate authorization
        if(!authenticationService.userExists(uid))
            throw new Unauthorized("You are have no access to this content");

        OptionalWrapper<Subject> response = subjectRepository.getById(id);
        return response.getValue().orElseThrow(response::getException);

    }

    @Override
    public Subject update(Integer id, Subject subject, String uid) throws Exception {
        //Validate authorization
        if(!authenticationService.isCoordinator(uid))
            throw new Unauthorized("You have not permission to update a subject");

        //Validate duplication
        Subject subjectSameName = subjectRepository.getByName(subject.getName()).getValue().orElse(null);
        if(subjectSameName != null && !Objects.equals(subjectSameName.getId(), id))
            throw new DuplicateInformation("This name already exist");

        OptionalWrapper<Subject> response = subjectRepository.update(id, subject);
        return response.getValue().orElseThrow(response::getException);
    }

    @Override
    public Subject remove(Integer id, String uid) throws Exception {
        //Validate authorization
        if(!authenticationService.isCoordinator(uid))
            throw new Unauthorized("You have not permission to update a subject");

        OptionalWrapper<Subject> response = subjectRepository.remove(id);
        return response.getValue().orElseThrow(response::getException);
    }
}
