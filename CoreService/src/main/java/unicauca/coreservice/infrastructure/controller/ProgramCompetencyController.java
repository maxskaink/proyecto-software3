package unicauca.coreservice.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicauca.coreservice.application.in.ProgramCompetencyAndOutcomeInt;
import unicauca.coreservice.domain.model.ProgramCompetency;

import java.util.List;

@RestController
@RequestMapping("/program/competency")
@AllArgsConstructor
public class ProgramCompetencyController {
    private final ProgramCompetencyAndOutcomeInt service;

    @PostMapping
    public ResponseEntity<ProgramCompetency> add(
            @RequestBody ProgramCompetency dtoIN
    ) throws Exception {
        ProgramCompetency response = service.add(dtoIN);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProgramCompetency>> listAllProgramCompetencies(){
        return ResponseEntity.ok(
                service.listAllProgramCompetencies()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramCompetency> getProgramCompetencyById(
            @PathVariable Integer id
    ) throws Exception {
        return ResponseEntity.ok(
                service.getProgramCompetencyById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProgramCompetency> updateProgramCompetency(
            @PathVariable Integer id,
            @RequestBody ProgramCompetency dtoIN
    ) throws Exception {
        ProgramCompetency response = service.updateProgramCompetency(id, dtoIN);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ProgramCompetency> remove(
            @PathVariable Integer id
    ) throws Exception {
        ProgramCompetency response = service.remove(id);
        return ResponseEntity.ok(
                response
        );
    }
}
