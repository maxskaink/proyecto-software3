package unicauca.coreservice.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicauca.coreservice.application.in.SubjectCompetencyInt;
import unicauca.coreservice.domain.model.SubjectCompetency;
import unicauca.coreservice.infrastructure.dto.InitialSubjectCompetencyDTO;

import java.util.List;

@RestController
@RequestMapping("/subject")
@AllArgsConstructor
public class SubjectCompetencyController {

    private final SubjectCompetencyInt serviceSubjectComp;

    @PostMapping("/{subjectId}/competency")
    public ResponseEntity<SubjectCompetency> add(
            @PathVariable Integer subjectId,
            @RequestBody InitialSubjectCompetencyDTO initialSubjectCompetencyDTO
    ) throws Exception {
        SubjectCompetency response = serviceSubjectComp.add(
                initialSubjectCompetencyDTO.getCompetency(),
                initialSubjectCompetencyDTO.getOutcome(),
                subjectId
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{subjectId}/competency")
    public ResponseEntity<List<SubjectCompetency>> listAllBySubjectId(
            @PathVariable Integer subjectId
    ) {
        return ResponseEntity.ok(
                serviceSubjectComp.listAllBySubjectId(subjectId)
        );
    }

    @GetMapping("/competency/{id}")
    public ResponseEntity<SubjectCompetency> getById(
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(
                serviceSubjectComp.getById(id)
        );
    }

    @PutMapping("/competency/{id}")
    public ResponseEntity<SubjectCompetency> update(
            @PathVariable Integer id,
            @RequestBody SubjectCompetency dtoIN
    ) throws Exception {
        SubjectCompetency response = serviceSubjectComp.update(id, dtoIN);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/competency/{id}")
    public ResponseEntity<SubjectCompetency> remove(
            @PathVariable Integer id
    ) throws Exception {
        SubjectCompetency response = serviceSubjectComp.remove(id);
        return ResponseEntity.ok(response);
    }
}
