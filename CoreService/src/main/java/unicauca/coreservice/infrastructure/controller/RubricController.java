package unicauca.coreservice.infrastructure.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import unicauca.coreservice.application.in.RubricInt;
import unicauca.coreservice.domain.model.Rubric;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/rubric")
@AllArgsConstructor
public class RubricController {

    private final RubricInt serviceRubric;
    
    @GetMapping("/{rubricId}")
    public ResponseEntity<Rubric> getById(
            @PathVariable Integer rubricId
    ) throws Exception {
        return ResponseEntity.ok(
                serviceRubric.getById(rubricId)
        );
    }

    @GetMapping("/outcome/{outcomeId}")
    public ResponseEntity<Rubric> getOutcome(
            @PathVariable Integer outcomeId
    ) throws Exception {
        return ResponseEntity.ok(
                serviceRubric.getBySubjectOutcomeId(outcomeId)
        );
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<Rubric>> listAllBySubjectId(
            @PathVariable Integer subjectId
    ) throws Exception {
        return ResponseEntity.ok(
                serviceRubric.listAllBySubjectId(subjectId)
        );
    }

    @PostMapping("/outcome/{outcomeId}")
    public ResponseEntity<Rubric> add(@RequestBody Rubric rubricIn, @PathVariable Integer outcomeId ) throws Exception {
        Rubric response = serviceRubric.add(rubricIn, outcomeId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("{rubricId}")
    public ResponseEntity<Rubric> update(@PathVariable Integer rubricId, @RequestBody Rubric rubricIn) throws Exception {
        Rubric response = serviceRubric.update(rubricId, rubricIn);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{rubricId}")
    public ResponseEntity<Rubric> remove(@PathVariable Integer rubricId) throws Exception {
        Rubric response = serviceRubric.remove(rubricId);
        return ResponseEntity.ok(response);
    }

}
