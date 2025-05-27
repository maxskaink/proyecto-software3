package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import unicauca.coreservice.application.out.IAuthenticationService;
import unicauca.coreservice.application.out.TeacherAssignmentOutInt;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.TeacherAssignment;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPATeacherAssignmentRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.TeacherAssignmentEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.TermEntity;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.SubjectMapper;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.TeacherAssignmentMapper;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.TermMapper;

import java.util.List;
import java.util.Objects;

@Repository
@AllArgsConstructor
public class TeacherAssignmentRepository implements TeacherAssignmentOutInt {
    private final JPATeacherAssignmentRepository teacherAssignmentRepository;
    private final TermRepository termRepository;
    private final IAuthenticationService firebaseService;


    @Override
    public OptionalWrapper<TeacherAssignment> add(TeacherAssignment newTeacherAssignment) {
        try{
            
            newTeacherAssignment.setId(null);

            if(firebaseService.userExists(newTeacherAssignment.getTeacherUid()))
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
        return teacherAssignmentRepository.findAll().stream().
                filter(ta -> ta.getSubject() != null && ta.getSubject().getId().equals(subjectId))
                .map(entity -> {
                    TeacherAssignment teacherAssignment = TeacherAssignmentMapper.toTeacherAssignment(entity);
                    teacherAssignment.setTerm(TermMapper.toTerm(entity.getTerm()));
                    teacherAssignment.setSubject(SubjectMapper.toSubject(entity.getSubject()));
                    return teacherAssignment;
                }).toList();
    }

    @Override
    public List<TeacherAssignment> listByTeacherUid(String teacherUid) {
        return teacherAssignmentRepository.findAll().stream().
                filter(ta -> ta.getTeacherUid() != null && ta.getTeacherUid().equals(teacherUid))
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
