package unicauca.coreservice.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicauca.coreservice.application.in.ProgramCompetencyAndOutcomeInt;
import unicauca.coreservice.domain.model.ProgramOutcome;

import java.util.List;

@RestController
@RequestMapping("/outcome/program")
@AllArgsConstructor
public class ProgramOutcomeController {

    private final ProgramCompetencyAndOutcomeInt service;

    @GetMapping
    public ResponseEntity<List<ProgramOutcome>> listAllProgramOutcomes(){
        return ResponseEntity.ok(
                service.listAllProgramOutcomes()
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProgramOutcome> getProgramOutcomeById(
            @PathVariable Integer id
    ) throws Exception {
        return ResponseEntity.ok(
                service.getProgramOutcomeById(id)
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<ProgramOutcome> updateProgramOutcome(
            @PathVariable Integer id,
            @RequestBody ProgramOutcome dtoIN
    ) throws Exception {
        ProgramOutcome response = service.updateProgramOutcome(id, dtoIN);
        return ResponseEntity.ok(response);
    }

}
