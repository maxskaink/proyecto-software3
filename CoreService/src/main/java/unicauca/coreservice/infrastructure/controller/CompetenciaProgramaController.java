package unicauca.coreservice.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicauca.coreservice.application.in.CompAndRaProgramaUsesCase;
import unicauca.coreservice.domain.model.CompetenciaPrograma;

import java.util.List;

@RestController
@RequestMapping("/competencia/programa")
@AllArgsConstructor
public class CompetenciaProgramaController {
    private final CompAndRaProgramaUsesCase service;

    @PostMapping
    public ResponseEntity<CompetenciaPrograma> createCompetenciaPrograma(
            @RequestBody CompetenciaPrograma dtoIN
    ) throws Exception {
        CompetenciaPrograma response = service.addCompPrograma(dtoIN);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CompetenciaPrograma>> listCompetenciaPrograma(){
        return ResponseEntity.ok(
                service.getCompetenciaProgramas()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetenciaPrograma> getByIdCompetenciaPrograma(
            @PathVariable Integer id
    ) throws Exception {
        return ResponseEntity.ok(
                service.getCompById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompetenciaPrograma> updateCompetenciaPrograma(
            @PathVariable Integer id,
            @RequestBody CompetenciaPrograma dtoIN
    ) throws Exception {
        CompetenciaPrograma response = service.updateCompPrograma(id, dtoIN);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CompetenciaPrograma> deleteCompetenciaPrograma(
            @PathVariable Integer id
    ) throws Exception {
        CompetenciaPrograma response = service.deleteCompPrograma(id);
        return ResponseEntity.ok(
                response
        );
    }
}
