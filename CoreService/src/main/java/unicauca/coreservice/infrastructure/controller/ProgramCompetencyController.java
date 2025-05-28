package unicauca.coreservice.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicauca.coreservice.application.in.ProgramCompetencyAndOutcomeInt;
import unicauca.coreservice.application.out.IAuthenticationService;
import unicauca.coreservice.domain.model.ProgramCompetency;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/program/competency")
@AllArgsConstructor
public class ProgramCompetencyController {
    private final ProgramCompetencyAndOutcomeInt service;
    private final IAuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<ProgramCompetency> add(
            @RequestBody ProgramCompetency dtoIN,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        ProgramCompetency response = service.add(dtoIN, uid);
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
            @RequestBody ProgramCompetency dtoIN,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        ProgramCompetency response = service.updateProgramCompetency(id, dtoIN, uid);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ProgramCompetency> remove(
            @PathVariable Integer id,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        ProgramCompetency response = service.remove(id, uid);
        return ResponseEntity.ok(
                response
        );
    }
}
