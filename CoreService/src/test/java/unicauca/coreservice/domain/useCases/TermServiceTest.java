package unicauca.coreservice.domain.useCases;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import unicauca.coreservice.domain.model.Term;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPAConfigurationRepository;
import unicauca.coreservice.infrastructure.SQLrepository.Repository.TermRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.ConfigurationEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.TermEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Transactional
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TermServiceTest {

    @Autowired
    private TermRepository repositoryTerm;

    @Autowired
    private JPAConfigurationRepository jpaConfigurationRepository;

    private TermService termService;

    @BeforeAll
    void setUp() throws Exception {
        termService = new TermService(repositoryTerm);
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
    }

    @Test
    void add_shouldReturnAddedTerm() throws Exception {
        Term term = new Term(null, "2024-1");

        Term result = termService.add(term);

        assertNotNull(result);
        assertEquals(term.getDescription(), result.getDescription());
    }

    @Test
    void listAll_shouldReturnListOfTerms() throws Exception {
        List<Term> terms = Arrays.asList(
                new Term(null, "2024-1"),
                new Term(null, "2024-2")
        );

        for(Term term : terms)
            termService.add(term);

        List<Term> result = termService.listAll();

        assertEquals(3, result.size());
        assertEquals("2024-2", result.getLast().getDescription());
    }

    @Test
    void getActiveTerm_shouldReturnActiveTerm() throws Exception {
        Term activeTerm = new Term(1, "2024-1");

        Term responseAdd = termService.add(activeTerm);

        assertNotNull(responseAdd, "response add term must  be not null");

        responseAdd = termService.setActiveTerm(responseAdd.getId());

        assertNotNull(responseAdd, "response set active must  be not null");

        Term result = termService.getActiveTerm();

        assertEquals(responseAdd.getId(), result.getId(), "active term must be the same");
        assertEquals(responseAdd.getDescription(), result.getDescription(), "active term must be the same");
    }

}