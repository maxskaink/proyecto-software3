package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import unicauca.coreservice.application.out.CompetencyToSubjectAssignmentRepositoryOutInt;
import unicauca.coreservice.domain.exception.DuplicateInformation;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.CompetencyToSubjectAssignment;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.*;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectCompetencyAssignmentEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectCompetencyEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.TermEntity;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.CompetencyToSubjectAssigmentMapper;

import java.util.Objects;

@Repository
@AllArgsConstructor
public class CompetencyToSubjectAssignmentRepositoryInt implements CompetencyToSubjectAssignmentRepositoryOutInt {

    private final JPASubjectCompetencyRepository subjectCompetencyRepository;
    private final JPASubjectCompetencyAssignmentRepository subjectCompetencyAssigmentRepository;
    private final JPASubjectRepository subjectRepository;
    private final JPATermRepository termRepository;


    @Override
    public OptionalWrapper<CompetencyToSubjectAssignment> add(CompetencyToSubjectAssignment newAssignment) {
        try{
            newAssignment.setId(null);

            // Verify if exists a competency with the same description for this subject in this term
            boolean exists = subjectCompetencyAssigmentRepository.findAllBySubjectId(newAssignment.getSubject().getId()).stream()
                    .filter(assignment -> assignment.getTerm().getId().equals(newAssignment.getTerm().getId()))
                    .filter(SubjectCompetencyAssignmentEntity::isActivated)
                    .anyMatch(assignment -> assignment.getCompetency().getDescription()
                            .equalsIgnoreCase(newAssignment.getCompetency().getDescription()));

            if (exists)
                return new OptionalWrapper<>(new DuplicateInformation("There is already a competency with this description for this subject in this term"));


            SubjectEntity subject =
                    subjectRepository.findActiveSubjectById(
                            newAssignment.getSubject().getId()
                    ).orElseThrow(()-> new NotFound("Subject with id " + newAssignment.getSubject().getId() + " not found"));

            SubjectCompetencyEntity competency =
                    subjectCompetencyRepository.findActiveSubjectCompetencyById(
                            newAssignment.getCompetency().getId()
                    ).orElseThrow(() -> new NotFound("Competency with id "+ newAssignment.getCompetency().getId()+ " doesn't exists"));

            TermEntity term =
                    termRepository.getReferenceById(
                            newAssignment.getTerm().getId()
                    );

            SubjectCompetencyAssignmentEntity finalAssignment =
                    SubjectCompetencyAssignmentEntity.builder()
                            .subject(subject)
                            .competency(competency)
                            .term(term)
                            .build();

            return new OptionalWrapper<>(
                    CompetencyToSubjectAssigmentMapper.toSubjectCompetencyAssignment(
                            subjectCompetencyAssigmentRepository.save(finalAssignment)
                    ));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<CompetencyToSubjectAssignment> getBySubjectId(Integer competencyId) {
        try{
            SubjectCompetencyAssignmentEntity assignment =
                    subjectCompetencyAssigmentRepository.findAllByCompetencyId(competencyId).stream()
                            .filter(a -> Objects.equals(a.getCompetency().getId(), competencyId))
                            .findFirst().orElseThrow(()-> new NotFound("Competency with the id" + competencyId + " doesn't exists"));
            return new OptionalWrapper<>(CompetencyToSubjectAssigmentMapper.toSubjectCompetencyAssignment(assignment));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<CompetencyToSubjectAssignment> remove(Integer assignmentId) {
        try{
            SubjectCompetencyAssignmentEntity activeEntity = subjectCompetencyAssigmentRepository.getReferenceById(assignmentId);

            if(!activeEntity.isActivated())
                throw new NotFound("Assignment with id " + assignmentId + " was not found");

            activeEntity.setActivated(false);

            return new OptionalWrapper<>(CompetencyToSubjectAssigmentMapper.toSubjectCompetencyAssignment(subjectCompetencyAssigmentRepository.save(activeEntity)));

        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

}
