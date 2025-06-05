package unicauca.coreservice.infrastructure.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import unicauca.coreservice.application.in.RubricInt;
import unicauca.coreservice.application.out.IAuthenticationService;
import unicauca.coreservice.domain.model.Rubric;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/rubric")
@AllArgsConstructor
public class RubricController {

    private final RubricInt serviceRubric;
    private final IAuthenticationService authenticationService;
    
    @GetMapping("/{rubricId}")
    public ResponseEntity<Rubric> getById(
            @PathVariable Integer rubricId,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        return ResponseEntity.ok(
                serviceRubric.getById(rubricId, uid)
        );
    }

    @GetMapping("/outcome/{outcomeId}")
    public ResponseEntity<Rubric> getOutcome(
            @PathVariable Integer outcomeId,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        return ResponseEntity.ok(
                serviceRubric.getBySubjectOutcomeId(outcomeId,  uid)
        );
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<Rubric>> listAllBySubjectId(
            @PathVariable Integer subjectId,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        return ResponseEntity.ok(
                serviceRubric.listAllBySubjectId(subjectId, uid)
        );
    }

    @PostMapping("/outcome/{outcomeId}")
    public ResponseEntity<Rubric> add(
            @RequestBody
            Rubric rubricIn,
            @PathVariable
            Integer outcomeId,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        Rubric response = serviceRubric.add(rubricIn, outcomeId, uid);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("{rubricId}")
    public ResponseEntity<Rubric> update(
            @PathVariable
            Integer rubricId,
            @RequestBody
            Rubric rubricIn,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        Rubric response = serviceRubric.update(rubricId, rubricIn, uid);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{rubricId}")
    public ResponseEntity<Rubric> remove(@PathVariable Integer rubricId, HttpServletRequest request) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        Rubric response = serviceRubric.remove(rubricId, uid);
        return ResponseEntity.ok(response);
    }

}
