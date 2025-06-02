package unicauca.microsubject.infrastructure.SQLrepository.Repository;

import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import unicauca.microsubject.application.out.IAuthenticationService;
import unicauca.microsubject.application.out.TeacherAssignmentRepositoryOutInt;
import unicauca.microsubject.domain.exception.NotFound;
import unicauca.microsubject.domain.model.OptionalWrapper;
import unicauca.microsubject.domain.model.TeacherAssignment;
import unicauca.microsubject.domain.model.Term;
import unicauca.microsubject.infrastructure.SQLrepository.JPARepository.JPATeacherAssignmentRepository;
import unicauca.microsubject.infrastructure.SQLrepository.entity.SubjectEntity;
import unicauca.microsubject.infrastructure.SQLrepository.entity.TeacherAssignmentEntity;
import unicauca.microsubject.infrastructure.SQLrepository.entity.TermEntity;
import unicauca.microsubject.infrastructure.SQLrepository.mapper.SubjectMapper;
import unicauca.microsubject.infrastructure.SQLrepository.mapper.TeacherAssignmentMapper;
import unicauca.microsubject.infrastructure.SQLrepository.mapper.TermMapper;

import java.util.List;
import java.util.Objects;

@Repository
@AllArgsConstructor
public class TeacherAssignmentRepository implements TeacherAssignmentRepositoryOutInt {
    private final JPATeacherAssignmentRepository teacherAssignmentRepository;
    private final TermRepository termRepository;
    private final IAuthenticationService authenticationService;


    @Override
    public OptionalWrapper<TeacherAssignment> add(TeacherAssignment newTeacherAssignment) {
        try{
            
            newTeacherAssignment.setId(null);

            if(!authenticationService.userExists(newTeacherAssignment.getTeacherUid()))
                throw new NotFound("Teacher does not exist");

            TeacherAssignmentEntity entity = TeacherAssignmentMapper.toTeacherAssignmentEntity(newTeacherAssignment);

            TermEntity TermEntity = TermMapper.toTermEntity(newTeacherAssignment.getTerm());
            SubjectEntity subjectEntity = SubjectMapper.toSubjectEntity(newTeacherAssignment.getSubject());

            entity.setTerm(TermEntity);
            entity.setSubject(subjectEntity);

            TeacherAssignmentEntity savedEntity = teacherAssignmentRepository.save(entity);
            TeacherAssignment savedTeacherAssignment = TeacherAssignmentMapper.toTeacherAssignment(savedEntity);

            savedTeacherAssignment.setTerm(newTeacherAssignment.getTerm());
            savedTeacherAssignment.setSubject(newTeacherAssignment.getSubject());
            return new OptionalWrapper<>(savedTeacherAssignment);

        }catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public List<TeacherAssignment> listBySubjectId(Integer subjectId) {
        Integer idActiveTerm = termRepository.getActiveTerm().getValue()
                .orElseThrow(()->new RuntimeException("No active term")).getId();
        return teacherAssignmentRepository.findAll().stream().
                filter(ta -> ta.getSubject() != null && ta.getSubject().getId().equals(subjectId) && ta.getTerm() != null && ta.getTerm().getId().equals(idActiveTerm))
                .map(entity -> {
                    TeacherAssignment teacherAssignment = TeacherAssignmentMapper.toTeacherAssignment(entity);
                    teacherAssignment.setTerm(TermMapper.toTerm(entity.getTerm()));
                    teacherAssignment.setSubject(SubjectMapper.toSubject(entity.getSubject()));
                    return teacherAssignment;
                }).toList();
    }

    @Override
    public List<TeacherAssignment> listByTeacherUid(String teacherUid) {
        Integer idActiveTerm = termRepository.getActiveTerm().getValue()
                .orElseThrow(()->new RuntimeException("No active term")).getId();
        return teacherAssignmentRepository.findAll().stream().
                filter(ta -> ta.getTeacherUid().equals(teacherUid) && ta.getTerm().getId().equals(idActiveTerm))
                .map(entity -> {
                    TeacherAssignment teacherAssignment = TeacherAssignmentMapper.toTeacherAssignment(entity);
                    teacherAssignment.setTerm(TermMapper.toTerm(entity.getTerm()));
                    teacherAssignment.setSubject(SubjectMapper.toSubject(entity.getSubject()));
                    return teacherAssignment;
                }).toList();
    }

    @Override
    public List<TeacherAssignment> listByTeacherUidActiveTerm(String teacherUid) throws Exception {
        OptionalWrapper<Term> term = this.termRepository.getActiveTerm();
        Integer idActualterm = term.getValue().orElseThrow(term::getException).getId();

        return teacherAssignmentRepository.findAll().stream().
                filter(ta -> ta.getTeacherUid().equals(teacherUid) && ta.getTerm().getId().equals(idActualterm))
                .map(entity -> {
                    TeacherAssignment teacherAssignment = TeacherAssignmentMapper.toTeacherAssignment(entity);
                    teacherAssignment.setTerm(TermMapper.toTerm(entity.getTerm()));
                    teacherAssignment.setSubject(SubjectMapper.toSubject(entity.getSubject()));
                    return teacherAssignment;
                }).toList();
    }


    @Override
    public OptionalWrapper<TeacherAssignment> remove(Integer id) {
        try{
            TeacherAssignmentEntity entity = teacherAssignmentRepository.findById(id)
                    .orElseThrow(() -> new NotFound("TeacherAssignment not found with id: " + id));

            teacherAssignmentRepository.delete(entity);
            TeacherAssignment teacherAssignment = TeacherAssignmentMapper.toTeacherAssignment(entity);
            teacherAssignment.setTerm(TermMapper.toTerm(entity.getTerm()));
            teacherAssignment.setSubject(SubjectMapper.toSubject(entity.getSubject()));
            return new OptionalWrapper<>(teacherAssignment);
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<TeacherAssignment> removeByTeacherAndSubjectInActiveTerm(String teacherUid, Integer subjectId) {
        try{
            TeacherAssignmentEntity entity = teacherAssignmentRepository.findAll().stream()
                    .filter(ta -> ta.getTeacherUid() != null && ta.getTeacherUid().equals(teacherUid))
                    .filter(ta -> ta.getSubject() != null && ta.getSubject().getId().equals(subjectId))
                    .filter(ta -> ta.getTerm() != null && termRepository.getActiveTerm().getValue().isPresent()
                            && ta.getTerm().getId().equals(termRepository.getActiveTerm().getValue().get().getId()))
                    .findFirst()
                    .orElseThrow(() -> new NotFound("TeacherAssignment not found with teacherUid: " + teacherUid + " and subjectId: " + subjectId));

            teacherAssignmentRepository.delete(entity);
            TeacherAssignment teacherAssignment = TeacherAssignmentMapper.toTeacherAssignment(entity);
            teacherAssignment.setTerm(TermMapper.toTerm(entity.getTerm()));
            teacherAssignment.setSubject(SubjectMapper.toSubject(entity.getSubject()));
            return new OptionalWrapper<>(teacherAssignment);
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }


    @Override
    public OptionalWrapper<TeacherAssignment> getById(Integer id) {
        try{
            TeacherAssignmentEntity entity = teacherAssignmentRepository.findById(id)
                    .orElseThrow(() -> new NotFound("TeacherAssignment not found with id: " + id));

            TeacherAssignment teacherAssignment = TeacherAssignmentMapper.toTeacherAssignment(entity);
            teacherAssignment.setTerm(TermMapper.toTerm(entity.getTerm()));
            teacherAssignment.setSubject(SubjectMapper.toSubject(entity.getSubject()));
            return new OptionalWrapper<>(teacherAssignment);
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<TeacherAssignment> getByTeacherAndSubjectInActiveTerm(String teacherUid, Integer subjectId) {
        try{
            TeacherAssignmentEntity entity = teacherAssignmentRepository.findAll().stream()
                    .filter(ta -> ta.getTeacherUid() != null && ta.getTeacherUid().equals(teacherUid))
                    .filter(ta -> ta.getSubject() != null && ta.getSubject().getId().equals(subjectId))
                    .filter(ta -> ta.getTerm() != null && termRepository.getActiveTerm().getValue().isPresent()
                            && ta.getTerm().getId().equals(termRepository.getActiveTerm().getValue().get().getId()))
                    .findFirst()
                    .orElseThrow(() -> new NotFound("TeacherAssignment not found with teacherUid: " + teacherUid + " and subjectId: " + subjectId));

            TeacherAssignment teacherAssignment = TeacherAssignmentMapper.toTeacherAssignment(entity);
            teacherAssignment.setTerm(TermMapper.toTerm(entity.getTerm()));
            teacherAssignment.setSubject(SubjectMapper.toSubject(entity.getSubject()));
            return new OptionalWrapper<>(teacherAssignment);
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public List<TeacherAssignment> listByActiveTerm() {
        return teacherAssignmentRepository.findAll().stream().
                filter(ta -> ta.getTerm() != null && termRepository.getActiveTerm().getValue().isPresent()
                        && Objects.equals(ta.getTerm().getId(), termRepository.getActiveTerm().getValue().get().getId()))
                .map(entity -> {
                    TeacherAssignment teacherAssignment = TeacherAssignmentMapper.toTeacherAssignment(entity);
                    teacherAssignment.setTerm(TermMapper.toTerm(entity.getTerm()));
                    teacherAssignment.setSubject(SubjectMapper.toSubject(entity.getSubject()));
                    return teacherAssignment;
                }).toList();
    }


}
