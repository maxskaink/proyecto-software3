package unicauca.coreservice.infrastructure.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import unicauca.coreservice.application.in.CriterionInt;
import unicauca.coreservice.application.out.IAuthenticationService;
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
    private final IAuthenticationService authenticationService;

    @GetMapping("/{criterionId}")
    public ResponseEntity<Criterion> getById(
            @PathVariable Integer criterionId,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        return ResponseEntity.ok(
                criterionService.getById(criterionId,uid)
        );
    }

    @GetMapping("/rubric/{rubricId}")
    public ResponseEntity<List<Criterion>> listAllByRubricId(
            @PathVariable Integer rubricId,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        return ResponseEntity.ok(
                criterionService.listAllByRubricId(rubricId,uid)
        );
    }
    
    @PostMapping("/rubric/{rubricId}")
    public ResponseEntity<Criterion> add(
            @RequestBody Criterion criterionIn,
            @PathVariable Integer rubricId,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        Criterion response = criterionService.add(criterionIn, rubricId,uid);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PutMapping("{criterionId}")
    public ResponseEntity<Criterion> update(
            @PathVariable Integer criterionId,
            @RequestBody Criterion criterionIn,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        return ResponseEntity.ok(
                criterionService.update(criterionId, criterionIn,uid)
        );
    }

    @DeleteMapping("{criterionId}")
    public ResponseEntity<Criterion> remove(
            @PathVariable Integer criterionId,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        return ResponseEntity.ok(
                criterionService.remove(criterionId,uid)
        );
    }
    
}
