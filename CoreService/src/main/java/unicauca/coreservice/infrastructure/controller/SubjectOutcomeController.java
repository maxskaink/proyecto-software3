package unicauca.coreservice.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicauca.coreservice.application.in.SubjectOutcomeInt;
import unicauca.coreservice.domain.model.SubjectOutcome;

import java.util.List;

@RestController
@RequestMapping("/subject")
@AllArgsConstructor
public class SubjectOutcomeController {

    private final SubjectOutcomeInt serviceSubjectOutcome;

    @PostMapping("/{subjectId}/competency/{competencyId}/outcome")
    public ResponseEntity<SubjectOutcome> addSubjectOutcome(
            @PathVariable Integer competencyId,
            @PathVariable Integer subjectId,
            @RequestBody SubjectOutcome newSubjectOutcome
    ) throws Exception {
        SubjectOutcome response = serviceSubjectOutcome.addSubjectOutcome(
                newSubjectOutcome, 
                competencyId, 
                subjectId
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{subjectId}/outcome")
    public ResponseEntity<List<SubjectOutcome>> listAll(
            @PathVariable Integer subjectId,
            @RequestParam(defaultValue="true") boolean activeTerm
    ) {
        if(activeTerm)
            return ResponseEntity.ok(
                    serviceSubjectOutcome.listAllInCurrentTerm(subjectId)
            );
        else
            return ResponseEntity.ok(
                    serviceSubjectOutcome.listAll(subjectId)
            );
    }

    @GetMapping("/competency/{competencyId}/outcome")
    public ResponseEntity<List<SubjectOutcome>> listAllByCompetencyId(
            @PathVariable Integer competencyId
    ) {
        return ResponseEntity.ok(
                serviceSubjectOutcome.listAllByCompetencyId(competencyId)
        );
    }

    @GetMapping("/outcome/{id}")
    public ResponseEntity<SubjectOutcome> getById(
            @PathVariable Integer id
    ) throws Exception {
        return ResponseEntity.ok(
                serviceSubjectOutcome.getById(id)
        );
    }

    @PutMapping("/outcome/{id}")
    public ResponseEntity<SubjectOutcome> update(
            @PathVariable Integer id,
            @RequestBody SubjectOutcome newSubjectOutcome
    ) throws Exception {
        SubjectOutcome response = serviceSubjectOutcome.update(id, newSubjectOutcome);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/outcome/{id}")
    public ResponseEntity<SubjectOutcome> remove(
            @PathVariable Integer id
    ) throws Exception {
        SubjectOutcome response = serviceSubjectOutcome.remove(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{subjectId}/outcome/{outcomeId}/copy-to/competency/{competencyId}")
    public ResponseEntity<SubjectOutcome> copy(
            @PathVariable Integer outcomeId,
            @PathVariable Integer competencyId,
            @PathVariable Integer subjectId
    ) throws Exception {
        SubjectOutcome response = serviceSubjectOutcome.copy(outcomeId, competencyId, subjectId);
        return ResponseEntity.ok(response);
    }
}
