package unicauca.coreservice.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicauca.coreservice.application.in.CompAndRaProgramaUsesCase;
import unicauca.coreservice.domain.model.RAPrograma;

import java.util.List;

@RestController
@RequestMapping("/ra/programa")
@AllArgsConstructor
public class RAProgramaController {

    private final CompAndRaProgramaUsesCase service;

    @GetMapping
    public ResponseEntity<List<RAPrograma>> listRAPrograma(){
        return ResponseEntity.ok(
                service.getRAProgramas()
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<RAPrograma> getByIdRAPrograma(
            @PathVariable Integer id
    ) throws Exception {
        return ResponseEntity.ok(
                service.getRAById(id)
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<RAPrograma> updateRAPrograma(
            @PathVariable Integer id,
            @RequestBody RAPrograma dtoIN
    ) throws Exception {
        RAPrograma response = service.updateRAPrograma(id, dtoIN);
        return ResponseEntity.ok(response);
    }

}
