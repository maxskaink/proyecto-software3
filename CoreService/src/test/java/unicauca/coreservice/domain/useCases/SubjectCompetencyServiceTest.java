package unicauca.coreservice.domain.useCases;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import unicauca.coreservice.application.in.SubjectCompetencyInt;
import unicauca.coreservice.application.out.CompetencyToSubjectAssignmentRepositoryOutInt;
import unicauca.coreservice.application.out.ProgramCompetencyAndOutcomeRepositoryOutInt;
import unicauca.coreservice.application.out.SubjectOutcomeRepositoryOutInt;
import unicauca.coreservice.domain.exception.DuplicateInformation;
import unicauca.coreservice.domain.exception.InvalidValue;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.*;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPAConfigurationRepository;
import unicauca.coreservice.infrastructure.SQLrepository.Repository.SubjectCompetencyRepository;
import unicauca.coreservice.infrastructure.SQLrepository.Repository.SubjectRepository;
import unicauca.coreservice.infrastructure.SQLrepository.Repository.TermRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.ConfigurationEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.TermEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SubjectCompetencyServiceTest {

    @Autowired
    private SubjectCompetencyRepository repository;
    @Autowired
    private ProgramCompetencyAndOutcomeRepositoryOutInt repository_prog;
    @Autowired
    private CompetencyToSubjectAssignmentRepositoryOutInt assignRepository;
    @Autowired
    private TermRepository termRepository;
    @Autowired
    private SubjectOutcomeRepositoryOutInt subjectOutcomeRepository;
    @Autowired
    private TermRepository repositoryTerm;
    @Autowired
    private JPAConfigurationRepository jpaConfigurationRepository;
    @Autowired
    private SubjectRepository subjectRepository;


    private SubjectCompetencyInt service;

    private ProgramCompetency programCompetency1;

    @BeforeAll
    public void init () throws Exception {
        TermService termService = new TermService(repositoryTerm);
        termService.add(
                new Term(null, "first-term")
        );
        ConfigurationEntity configurationEntity = new ConfigurationEntity();
        configurationEntity.setActiveTerm(
                new TermEntity(1, "first-term")
        );
        jpaConfigurationRepository.save(
                configurationEntity
        );

        subjectRepository.add(new Subject(null, "Unique subject", "Super pro subject"));

        this.service = new SubjectCompetencyService(repository, assignRepository,termRepository,subjectOutcomeRepository);
        ProgramCompetencyAndOutcomeService service_prog = new ProgramCompetencyAndOutcomeService(repository_prog);

        ProgramOutcome outcome_1 = new ProgramOutcome(null, "Test Outcome");
        ProgramCompetency competency_1 = new ProgramCompetency(null, "Test Competency", "Basic", outcome_1, new ArrayList<>());

        this.programCompetency1 = service_prog.add(competency_1);
    }

    @Test
    void add_shouldReturnAddedSubjectCompetency() throws Exception {
        // Arrange
        SubjectCompetency competency = new SubjectCompetency(null, "Some description", "easy", programCompetency1.getId());
        SubjectOutcome firstOutCome = new SubjectOutcome(null, "Test Outcome",null);
        // Act
        SubjectCompetency result = service.add(competency, firstOutCome, 1);

        // Assert
        assertNotNull(result);
        assertEquals(competency.getDescription(), result.getDescription());
        assertEquals(competency.getLevel(), result.getLevel());
    }

    @Test
    void add_shouldThrowExceptionWhenDuplicateDescription() throws Exception {
        // Arrange
        SubjectCompetency competency = new SubjectCompetency(null, "Some description", "easy", programCompetency1.getId());
        SubjectOutcome firstOutCome = new SubjectOutcome(null, "Test Outcome",null);

        // Act & Assert

        SubjectCompetency firstInsert =  service.add(competency, firstOutCome, 1);

        assertNotNull(firstInsert, "first competency must be not null");

        assertThrows(DuplicateInformation.class, () -> service.add(competency, firstOutCome, 1));
    }

    @Test
    void add_shouldThrowExceptionWhenMissingRequiredData() {
        // Arrange
        // Act & Assert - Missing description
        assertThrows(InvalidValue.class, () -> new SubjectCompetency(null, null, "Basic", programCompetency1.getId()));

        // Act & Assert - Missing level
        assertThrows(InvalidValue.class, () -> new SubjectCompetency(null, "some description", null, programCompetency1.getId()));
    }

    @Test
    void listAllProgramCompetencies_shouldReturnAllCompetencies() throws Exception {
        // Arrange
        SubjectOutcome firstOutCome = new SubjectOutcome(null, "Test Outcome",null);

        List<SubjectCompetency> expectedCompetencies = Arrays.asList(
                new SubjectCompetency(null, "Some description", "easy", programCompetency1.getId()),
                new SubjectCompetency(null, "Some description 2", "medium", programCompetency1.getId())
        );

        // Act

        for(SubjectCompetency cmp: expectedCompetencies)
            service.add(cmp, firstOutCome, 1);

        List<SubjectCompetency> result = service.listAllBySubjectId(1);
        // Assert
        assertEquals(expectedCompetencies.size(), result.size());
        assertEquals(expectedCompetencies.get(0).getDescription(), result.get(0).getDescription());
        assertEquals(expectedCompetencies.get(1).getDescription(), result.get(1).getDescription());
    }

    @Test
    void getProgramCompetencyById_shouldReturnCompetencyWhenExists() throws Exception {
        // Arrange
        SubjectCompetency competency = new SubjectCompetency(null, "Some description", "easy", programCompetency1.getId());
        SubjectOutcome firstOutCome = new SubjectOutcome(null, "Test Outcome",null);

        // Act
        SubjectCompetency insert = service.add(competency, firstOutCome, 1);
        SubjectCompetency result = service.getById(insert.getId());

        // Assert
        assertNotNull(result);
        assertEquals(insert.getId(), result.getId());
        assertEquals(insert.getDescription(), result.getDescription());
    }

    @Test
    void getProgramCompetencyById_shouldThrowExceptionWhenNotExists() {
        // Arrange
        Integer id = 999;

        // Act & Assert
        assertThrows(NotFound.class, () -> service.getById(id));
        
    }

    @Test
    void updateProgramCompetency_shouldReturnUpdatedCompetency() throws Exception {
        // Arrange
        SubjectCompetency competency = new SubjectCompetency(null, "Some description", "easy", programCompetency1.getId());
        SubjectCompetency competencyUpdated = new SubjectCompetency(null, "Some description updated", "easy updated", programCompetency1.getId());
        SubjectOutcome firstOutCome = new SubjectOutcome(null, "Test Outcome",null);

        // Act
        SubjectCompetency originalCompetency = service.add(competency, firstOutCome, 1);
        SubjectCompetency result = service.update(originalCompetency.getId(), competencyUpdated);

        // Assert
        assertNotNull(result);
        assertEquals(competencyUpdated.getDescription(), result.getDescription());
        assertEquals(competencyUpdated.getLevel(), result.getLevel());
    }

    @Test
    void remove_shouldReturnRemovedCompetency() throws Exception {
        // Arrange
        SubjectCompetency competency = new SubjectCompetency(null, "Some description", "easy", programCompetency1.getId());
        SubjectOutcome firstOutCome = new SubjectOutcome(null, "Test Outcome",null);
        
        // Act
        SubjectCompetency compAdded= service.add(competency, firstOutCome, 1);
        SubjectCompetency result = service.remove(compAdded.getId());

        assertThrows(NotFound.class, () -> service.getById(compAdded.getId()));

        // Assert
        assertNotNull(result);
        assertEquals(competency.getDescription(), result.getDescription());
    }

}