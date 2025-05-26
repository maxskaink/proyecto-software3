package unicauca.coreservice.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicauca.coreservice.application.in.SubjectOutcomeInt;
import unicauca.coreservice.application.out.IAuthenticationService;
import unicauca.coreservice.domain.model.SubjectOutcome;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/subject")
@AllArgsConstructor
public class SubjectOutcomeController {

    private final SubjectOutcomeInt serviceSubjectOutcome;
    private final IAuthenticationService authenticationService;

    @PostMapping("/{subjectId}/competency/{competencyId}/outcome")
    public ResponseEntity<SubjectOutcome> addSubjectOutcome(
            @PathVariable Integer competencyId,
            @PathVariable Integer subjectId,
            @RequestBody SubjectOutcome newSubjectOutcome,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        SubjectOutcome response = serviceSubjectOutcome.addSubjectOutcome(
                newSubjectOutcome, 
                competencyId, 
                subjectId,
                uid
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{subjectId}/outcome")
    public ResponseEntity<List<SubjectOutcome>> listAll(
            @PathVariable Integer subjectId,
            @RequestParam(defaultValue="true") boolean activeTerm,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        if(activeTerm)
            return ResponseEntity.ok(
                    serviceSubjectOutcome.listAllInCurrentTerm(subjectId,uid)
            );
        else
            return ResponseEntity.ok(
                    serviceSubjectOutcome.listAll(subjectId,uid)
            );
    }

    @GetMapping("/competency/{competencyId}/outcome")
    public ResponseEntity<List<SubjectOutcome>> listAllByCompetencyId(
            @PathVariable Integer competencyId,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        return ResponseEntity.ok(
                serviceSubjectOutcome.listAllByCompetencyId(competencyId, uid)
        );
    }

    @GetMapping("/outcome/{id}")
    public ResponseEntity<SubjectOutcome> getById(
            @PathVariable Integer id,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        return ResponseEntity.ok(
                serviceSubjectOutcome.getById(id,uid)
        );
    }

    @PutMapping("/outcome/{id}")
    public ResponseEntity<SubjectOutcome> update(
            @PathVariable Integer id,
            @RequestBody SubjectOutcome newSubjectOutcome,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        SubjectOutcome response = serviceSubjectOutcome.update(id, newSubjectOutcome, uid);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/outcome/{id}")
    public ResponseEntity<SubjectOutcome> remove(
            @PathVariable Integer id,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        SubjectOutcome response = serviceSubjectOutcome.remove(id,uid);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{subjectId}/outcome/{outcomeId}/copy-to/competency/{competencyId}")
    public ResponseEntity<SubjectOutcome> copy(
            @PathVariable Integer outcomeId,
            @PathVariable Integer competencyId,
            @PathVariable Integer subjectId,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        SubjectOutcome response = serviceSubjectOutcome.copy(outcomeId, competencyId, subjectId, uid);
        return ResponseEntity.ok(response);
    }
}
