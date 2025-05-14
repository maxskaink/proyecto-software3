package unicauca.coreservice.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicauca.coreservice.application.in.ProgramCompetencyAndOutcomeInt;
import unicauca.coreservice.domain.model.ProgramCompetency;

import java.util.List;

@RestController
@RequestMapping("/competencia/programa")
@AllArgsConstructor
public class CompetenciaProgramaController {
    private final ProgramCompetencyAndOutcomeInt service;

    @PostMapping
    public ResponseEntity<ProgramCompetency> createCompetenciaPrograma(
            @RequestBody ProgramCompetency dtoIN
    ) throws Exception {
        ProgramCompetency response = service.addProgramCompetency(dtoIN);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProgramCompetency>> listCompetenciaPrograma(){
        return ResponseEntity.ok(
                service.getProgramCompetencies()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramCompetency> getByIdCompetenciaPrograma(
            @PathVariable Integer id
    ) throws Exception {
        return ResponseEntity.ok(
                service.getCompetencyById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProgramCompetency> updateCompetenciaPrograma(
            @PathVariable Integer id,
            @RequestBody ProgramCompetency dtoIN
    ) throws Exception {
        ProgramCompetency response = service.updateProgramCompetency(id, dtoIN);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ProgramCompetency> deleteCompetenciaPrograma(
            @PathVariable Integer id
    ) throws Exception {
        ProgramCompetency response = service.deleteProgramCompetency(id);
        return ResponseEntity.ok(
                response
        );
    }
}
