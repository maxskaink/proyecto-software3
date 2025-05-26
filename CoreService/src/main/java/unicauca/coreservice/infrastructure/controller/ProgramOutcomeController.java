package unicauca.coreservice.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicauca.coreservice.application.in.ProgramCompetencyAndOutcomeInt;
import unicauca.coreservice.application.out.IAuthenticationService;
import unicauca.coreservice.domain.model.ProgramOutcome;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/program/outcome")
@AllArgsConstructor
public class ProgramOutcomeController {

    private final ProgramCompetencyAndOutcomeInt service;
    private final IAuthenticationService authenticationService;

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
            @RequestBody ProgramOutcome dtoIN,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        ProgramOutcome response = service.updateProgramOutcome(id, dtoIN, uid);
        return ResponseEntity.ok(response);
    }

}
