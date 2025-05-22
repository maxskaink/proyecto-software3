package unicauca.coreservice.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import unicauca.coreservice.application.in.CriterionInt;
import unicauca.coreservice.domain.model.Criterion;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/criterion")
@AllArgsConstructor
public class CriterionController {
    private final CriterionInt criterionService;

    @GetMapping("/{criterionId}")
    public ResponseEntity<Criterion> getById(
            @PathVariable Integer criterionId
    ) throws Exception {
        return ResponseEntity.ok(
                criterionService.getById(criterionId)
        );
    }

    @GetMapping("/rubric/{rubricId}")
    public ResponseEntity<List<Criterion>> listAllByRubricId(
            @PathVariable Integer rubricId
    ) throws Exception {
        return ResponseEntity.ok(
                criterionService.listAllByRubricId(rubricId)
        );
    }
    
    @PostMapping("/rubric/{rubricId}")
    public ResponseEntity<Criterion> add(@RequestBody Criterion criterionIn, @PathVariable Integer rubricId ) throws Exception {
        Criterion response = criterionService.add(criterionIn, rubricId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("{criterionId}")
    public ResponseEntity<Criterion> update(
            @PathVariable Integer criterionId,
            @RequestBody Criterion criterionIn
    ) throws Exception {
        return ResponseEntity.ok(
                criterionService.update(criterionId, criterionIn)
        );
    }

    @DeleteMapping("{criterionId}")
    public ResponseEntity<Criterion> remove(
            @PathVariable Integer criterionId
    ) throws Exception {
        return ResponseEntity.ok(
                criterionService.remove(criterionId)
        );
    }
    
}
