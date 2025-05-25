package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import unicauca.coreservice.application.out.TeacherAssignmentOutInt;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.TeacherAssignment;
import unicauca.coreservice.infrastructure.security.FirebaseService;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPATeacherAssignmentRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.TeacherAssignmentEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.TermEntity;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.SubjectMapper;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.TeacherAssignmentMapper;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.TermMapper;

import java.util.List;

@Repository
@AllArgsConstructor
public class TeacherAssignmentRepository implements TeacherAssignmentOutInt {
    private final JPATeacherAssignmentRepository teacherAssignmentRepository;
    //TODO Consultarlo con julian despues
    //private final FirebaseService firebaseService;


    @Override
    public OptionalWrapper<TeacherAssignment> add(TeacherAssignment newTeacherAssignment) {
        try{
            
            newTeacherAssignment.setId(null);

//            if(!firebaseService.userExists(newTeacherAssignment.getTeacherUid()))
//                throw new NotFound("Teacher does not exist");

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
    public OptionalWrapper<TeacherAssignment> getById(Integer id) {
        try{
            TeacherAssignmentEntity entity = teacherAssignmentRepository.findById(id)
                    .orElseThrow(() -> new Exception("TeacherAssignment not found with id: " + id));

            TeacherAssignment teacherAssignment = TeacherAssignmentMapper.toTeacherAssignment(entity);
            teacherAssignment.setTerm(TermMapper.toTerm(entity.getTerm()));
            teacherAssignment.setSubject(SubjectMapper.toSubject(entity.getSubject()));
            return new OptionalWrapper<>(teacherAssignment);
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public List<TeacherAssignment> listByTermId(Integer termId) {
            return teacherAssignmentRepository.findAll().stream().
                    filter(ta -> ta.getTerm() != null && ta.getTerm().getId().equals(termId))
                    .map(entity -> {
                        TeacherAssignment teacherAssignment = TeacherAssignmentMapper.toTeacherAssignment(entity);
                        teacherAssignment.setTerm(TermMapper.toTerm(entity.getTerm()));
                        teacherAssignment.setSubject(SubjectMapper.toSubject(entity.getSubject()));
                        return teacherAssignment;
                    }).toList();
    }

    @Override
    public List<TeacherAssignment> listBySubjectId(Integer subjectId) {
        return List.of();
    }


    @Override
    public OptionalWrapper<TeacherAssignment> remove(Integer id) {
        return null;
    }
}
