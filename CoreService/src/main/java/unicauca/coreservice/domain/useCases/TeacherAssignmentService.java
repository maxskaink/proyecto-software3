package unicauca.coreservice.domain.useCases;

import lombok.AllArgsConstructor;
import unicauca.coreservice.application.in.TeacherAssignmentInt;
import unicauca.coreservice.application.out.SubjectRepositoryOutInt;
import unicauca.coreservice.application.out.TeacherAssignmentRepositoryOutInt;
import unicauca.coreservice.application.out.TermRepositoryOutInt;
import unicauca.coreservice.domain.exception.DuplicateInformation;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.Subject;
import unicauca.coreservice.domain.model.TeacherAssignment;
import unicauca.coreservice.domain.model.Term;

import java.util.List;
import java.util.Optional;




@AllArgsConstructor
public class TeacherAssignmentService implements TeacherAssignmentInt{
    private final TeacherAssignmentRepositoryOutInt teacherAssignmentRepository;
    private final TermRepositoryOutInt termRepository;
    private final SubjectRepositoryOutInt subjectRepository;

    @Override
    public TeacherAssignment add(String teacherUid, Integer subjectId) throws Exception {
        OptionalWrapper<TeacherAssignment> teacherAssignmentWrapper = teacherAssignmentRepository.getByTeacherAndSubjectInActiveTerm(teacherUid, subjectId);
        Optional<TeacherAssignment> teacherAssignmentOpt = teacherAssignmentWrapper.getValue();
        if(teacherAssignmentOpt.isPresent())
        {
            throw new DuplicateInformation("Teacher already assigned to this subject in the active term");
        }

        Optional<Term> term = termRepository.getActiveTerm().getValue();
        if(term.isEmpty())
        {
            throw termRepository.getActiveTerm().getException();
        }

        Optional<Subject> subject = subjectRepository.getById(subjectId).getValue();
        if(subject.isEmpty())
        {
            throw subjectRepository.getById(subjectId).getException();
        }

        TeacherAssignment teacherAssignment = new TeacherAssignment(null, term.get(), subject.get(), teacherUid);

        OptionalWrapper<TeacherAssignment> response = teacherAssignmentRepository.add(teacherAssignment);
        return response.getValue().orElseThrow(response::getException);
    }

    @Override
    public TeacherAssignment getById(Integer id) throws Exception {
        OptionalWrapper<TeacherAssignment> response = teacherAssignmentRepository.getById(id);
        return response.getValue().orElseThrow(response::getException);
    }


    @Override
    public TeacherAssignment getByTeacherAndSubjectInActiveTerm(String teacherUid, Integer subjectId) throws Exception {
        OptionalWrapper<TeacherAssignment> response = teacherAssignmentRepository.getByTeacherAndSubjectInActiveTerm(teacherUid, subjectId);
        return response.getValue().orElseThrow(response::getException);
    }

    @Override
    public List<TeacherAssignment> listTeacherAssignmentsInActiveTerm() {
        return teacherAssignmentRepository.listByActiveTerm();
    }

    @Override
    public List<TeacherAssignment> listBySubjectId(Integer subjectId) {
        return teacherAssignmentRepository.listBySubjectId(subjectId);
    }

    @Override
    public List<TeacherAssignment> listByTeacherUid(String teacherUid) {
        return teacherAssignmentRepository.listByTeacherUid(teacherUid);
    }

    @Override
    public TeacherAssignment remove(Integer id) throws Exception {
        OptionalWrapper<TeacherAssignment> response = teacherAssignmentRepository.remove(id);
        return response.getValue().orElseThrow(response::getException);
    }

    @Override
    public TeacherAssignment removeByTeacherAndSubjectInActiveTerm(String teacherUid, Integer subjectId) throws Exception {
        OptionalWrapper<TeacherAssignment> response = teacherAssignmentRepository.removeByTeacherAndSubjectInActiveTerm(teacherUid, subjectId);
        return response.getValue().orElseThrow(response::getException);
    }


}
