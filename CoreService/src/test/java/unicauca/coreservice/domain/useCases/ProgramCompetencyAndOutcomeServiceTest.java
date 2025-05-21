package unicauca.coreservice.domain.useCases;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import unicauca.coreservice.application.out.ProgramCompetencyAndOutcomeRepositoryOutInt;
import unicauca.coreservice.domain.exception.DuplicateInformation;
import unicauca.coreservice.domain.exception.InvalidValue;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.ProgramCompetency;
import unicauca.coreservice.domain.model.ProgramOutcome;
import unicauca.coreservice.domain.model.SubjectCompetency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProgramCompetencyAndOutcomeServiceTest {

    @Autowired
    private ProgramCompetencyAndOutcomeRepositoryOutInt repository;
    private ProgramCompetencyAndOutcomeService service;
    @BeforeAll
    public void init () {
        this.service = new ProgramCompetencyAndOutcomeService(repository);
    }

    @Test
    void add_shouldReturnAddedProgramCompetency() throws Exception {

        // Arrange
        ProgramOutcome outcome = new ProgramOutcome(12, "Test Outcome");
        ProgramCompetency competency = new ProgramCompetency(12, "Test Competency", "Basic", outcome, new ArrayList<>());

        // Act
        ProgramCompetency result = service.add(competency);

        // Assert
        assertNotNull(result);
        assertEquals(competency.getDescription(), result.getDescription());
        assertEquals(competency.getLevel(), result.getLevel());
    }

    @Test
    void add_shouldThrowExceptionWhenDuplicateDescription() throws Exception {
        // Arrange
        ProgramOutcome outcome = new ProgramOutcome(1, "Test Outcome");
        ProgramCompetency competency = new ProgramCompetency(null, "Duplicate Competency", "Basic", outcome, new ArrayList<>());

        // Act & Assert

        ProgramCompetency firstInsert =  service.add(competency);

        assertNotNull(firstInsert, "first competency must be not null");

        Exception exception = assertThrows(DuplicateInformation.class, () -> {
            service.add(competency);
        });
    }

    @Test
    void add_shouldThrowExceptionWhenMissingRequiredData() {
        // Arrange
        ProgramOutcome outcome = new ProgramOutcome(1, "Test Outcome");
        
        // Act & Assert - Missing description
        Exception exception1 = assertThrows(InvalidValue.class, () -> {
            new ProgramCompetency(null, null, "Basic", outcome, new ArrayList<>());
        });
        assertEquals("The description can not be null or empty", exception1.getMessage());
        
        // Act & Assert - Missing level
        Exception exception2 = assertThrows(InvalidValue.class, () -> {
            new ProgramCompetency(null, "Test Competency", null, outcome, new ArrayList<>());
        });
        assertEquals("The competency level can not be null or empty", exception2.getMessage());
    }

    @Test
    void listAllProgramCompetencies_shouldReturnAllCompetencies() throws Exception {
        // Arrange
        ProgramOutcome outcome = new ProgramOutcome(1, "Test Outcome");
        List<ProgramCompetency> expectedCompetencies = Arrays.asList(
                new ProgramCompetency(1, "Competency 1", "Basic", outcome, new ArrayList<>()),
                new ProgramCompetency(2, "Competency 2", "Intermediate", outcome, new ArrayList<>())
        );

        // Act

        for(ProgramCompetency cmp: expectedCompetencies)
            service.add(cmp);

        List<ProgramCompetency> result = service.listAllProgramCompetencies();
        System.out.println("por que esto da null? "+expectedCompetencies.get(1).getId());
        // Assert
        assertEquals(expectedCompetencies.size(), result.size());
        assertEquals(expectedCompetencies.get(0).getDescription(), result.get(0).getDescription());
        assertEquals(expectedCompetencies.get(1).getDescription(), result.get(1).getDescription());
    }

    @Test
    void getProgramCompetencyById_shouldReturnCompetencyWhenExists() throws Exception {
        // Arrange
        ProgramOutcome outcome = new ProgramOutcome(1, "Test Outcome");
        ProgramCompetency expectedCompetency = new ProgramCompetency(null, "Test Competency", "Basic", outcome, new ArrayList<>());

        // Act
        ProgramCompetency insert = service.add(expectedCompetency);
        ProgramCompetency result = service.getProgramCompetencyById(insert.getId());

        // Assert
        assertNotNull(result);
        assertEquals(insert.getId(), result.getId());
        assertEquals(expectedCompetency.getDescription(), result.getDescription());
    }

    @Test
    void getProgramCompetencyById_shouldThrowExceptionWhenNotExists() throws Exception {
        // Arrange
        Integer id = 999;

        // Act & Assert
        Exception exception = assertThrows(NotFound.class, () -> {
            service.getProgramCompetencyById(id);
        });
        
    }

    @Test
    void getProgramCompetencyById_shouldThrowExceptionWhenIdIsNull() {
        // Act & Assert
        Exception exception = assertThrows(InvalidValue.class, () -> {
            service.getProgramCompetencyById(null);
        });
        
        assertEquals("The id is not valid, it can not be null", exception.getMessage());
    }

    @Test
    void updateProgramCompetency_shouldReturnUpdatedCompetency() throws Exception {
        // Arrange
        ProgramOutcome outcome = new ProgramOutcome(null, "Test Outcome");
        ProgramCompetency competencyToUpdate = new ProgramCompetency(null, "Updated Competency", "Advanced", outcome, new ArrayList<>());
        ProgramCompetency competencyUpdated = new ProgramCompetency(null, "Updated Competency", "Advanced", outcome, new ArrayList<>());
        // Act
        ProgramCompetency originalCompetency = service.add(competencyToUpdate);
        ProgramCompetency result = service.updateProgramCompetency(originalCompetency.getId(), competencyUpdated);

        // Assert
        assertNotNull(result);
        assertEquals(competencyUpdated.getDescription(), result.getDescription());
        assertEquals(competencyUpdated.getLevel(), result.getLevel());
    }

    @Test
    void updateProgramCompetency_shouldThrowExceptionWhenIdIsNull() {
        // Arrange
        ProgramOutcome outcome = new ProgramOutcome(1, "Test Outcome");
        ProgramCompetency competency = new ProgramCompetency(1, "Test Competency", "Basic", outcome, new ArrayList<>());
        
        // Act & Assert
        Exception exception = assertThrows(InvalidValue.class, () -> {
            service.updateProgramCompetency(null, competency);
        });
        
        assertEquals("The id is not valid, it can not be null", exception.getMessage());
    }

    @Test
    void updateProgramCompetency_shouldThrowExceptionWhenCompetencyIsNull() {
        //Arrange

        ProgramOutcome newOutcome = new ProgramOutcome(1,"algo");

        // Act & Assert

        Exception exception = assertThrows(InvalidValue.class, () -> {
            service.updateProgramCompetency(1, null);
        });
        
        assertEquals("Instance of competency is invalid, it can not be null", exception.getMessage());
    }

    @Test
    void remove_shouldReturnRemovedCompetency() throws Exception {
        // Arrange
        ProgramOutcome outcome = new ProgramOutcome(1, "Test Outcome");
        ProgramCompetency expectedCompetency = new ProgramCompetency(null, "Test Competency", "Basic", outcome, new ArrayList<>());
        
        // Act
        ProgramCompetency compAdded= service.add(expectedCompetency);
        ProgramCompetency result = service.remove(compAdded.getId());

        assertThrows(NotFound.class, () -> {
            service.getProgramCompetencyById(compAdded.getId());
        });

        // Assert
        assertNotNull(result);
        assertEquals(expectedCompetency.getDescription(), result.getDescription());
    }

    @Test
    void remove_shouldThrowExceptionWhenIdIsNull() {
        // Act & Assert
        Exception exception = assertThrows(InvalidValue.class, () -> {
            service.remove(null);
        });
        
        assertEquals("The id is not valid, it can not be null", exception.getMessage());
    }

    @Test
    void listAllProgramOutcomes_shouldReturnAllOutcomes() throws Exception {
        // Arrange

        List<ProgramOutcome> expectedOutcomes = Arrays.asList(
                new ProgramOutcome(1, "Outcome 1"),
                new ProgramOutcome(2, "Outcome 2")
        );

        List<ProgramCompetency> exptectedCompetency = Arrays.asList(
            new ProgramCompetency(1, "Test Competency", "Basic", expectedOutcomes.get(0), new ArrayList<>()),
            new ProgramCompetency(1, "Test Competency 2", "Basic", expectedOutcomes.get(1), new ArrayList<>())
        );
        // Act
        for(ProgramCompetency cmp: exptectedCompetency)
            service.add(cmp);

        List<ProgramOutcome> result = service.listAllProgramOutcomes();

        // Assert
        assertEquals(exptectedCompetency.size(), result.size());
    }

    @Test
    void getProgramOutcomeById_shouldReturnOutcomeWhenExists() throws Exception {
        // Arrange
        ProgramOutcome outcome = new ProgramOutcome(1, "Test Outcome");
        ProgramCompetency competency = new ProgramCompetency(null, "Duplicate Competency", "Basic", outcome, new ArrayList<>());
        
        // Act
        ProgramCompetency competencyResult = service.add(competency);
        ProgramOutcome result = service.getProgramOutcomeById(competencyResult.getProgramOutcome().getId());

        // Assert
        assertNotNull(result);
        assertEquals(outcome.getDescription(), result.getDescription());
    }

    @Test
    void getProgramOutcomeById_shouldThrowExceptionWhenNotExists() throws Exception {
        // Arrange
        Integer id = 999;
        Exception expectedException = new Exception("Outcome not found");
        
        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            service.getProgramOutcomeById(id);
        });
    }

    @Test
    void getProgramOutcomeById_shouldThrowExceptionWhenIdIsNull() {
        // Act & Assert
        Exception exception = assertThrows(InvalidValue.class, () -> {
            service.getProgramOutcomeById(null);
        });
        
        assertEquals("The id is not valid, it can not be null", exception.getMessage());
    }

    @Test
    void updateProgramOutcome_shouldReturnUpdatedOutcome() throws Exception {
        // Arrange
        ProgramOutcome outcome = new ProgramOutcome(1, "Test Outcome");
        ProgramCompetency competency = new ProgramCompetency(1, "Test Competency", "Basic", outcome, new ArrayList<>());

        // Act
        ProgramCompetency compAdded = service.add(competency);
        List<ProgramOutcome> outcomes = service.listAllProgramOutcomes();
        ProgramOutcome firstOutcome = outcomes.getFirst();
        ProgramOutcome result = service.updateProgramOutcome(firstOutcome.getId(), outcome);

        // Assert
        assertNotNull(result);
        assertEquals(firstOutcome.getId(), result.getId());
        assertEquals(outcome.getDescription(), result.getDescription());
    }

    @Test
    void updateProgramOutcome_shouldThrowExceptionWhenIdIsNull() {
        // Arrange
        ProgramOutcome outcome = new ProgramOutcome(1, "Test Outcome");
        
        // Act & Assert
        Exception exception = assertThrows(InvalidValue.class, () -> {
            service.updateProgramOutcome(null, outcome);
        });
        
        assertEquals("The id is not valid, it can not be null", exception.getMessage());
    }

    @Test
    void updateProgramOutcome_shouldThrowExceptionWhenOutcomeIsNull() {
        // Act & Assert
        Exception exception = assertThrows(InvalidValue.class, () -> {
            service.updateProgramOutcome(1, null);
        });
        
        assertEquals("Instance of outcome is invalid, it can not be null", exception.getMessage());
    }
}