package unicauca.coreservice.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicauca.coreservice.application.in.SubjectCompetencyInt;
import unicauca.coreservice.application.out.IAuthenticationService;
import unicauca.coreservice.domain.model.SubjectCompetency;
import unicauca.coreservice.infrastructure.dto.InitialSubjectCompetencyDTO;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/subject")
@AllArgsConstructor
public class SubjectCompetencyController {

    private final SubjectCompetencyInt serviceSubjectComp;
    private final IAuthenticationService authenticationService;

    @PostMapping("/{subjectId}/competency")
    public ResponseEntity<SubjectCompetency> add(
            @PathVariable Integer subjectId,
            @RequestBody InitialSubjectCompetencyDTO initialSubjectCompetencyDTO,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        SubjectCompetency response = serviceSubjectComp.add(
                initialSubjectCompetencyDTO.getCompetency(),
                initialSubjectCompetencyDTO.getOutcomes(),
                subjectId,uid
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{subjectId}/competency")
    public ResponseEntity<List<SubjectCompetency>> listAllBySubjectId(
            @PathVariable Integer subjectId,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        return ResponseEntity.ok(
                serviceSubjectComp.listAllBySubjectId(subjectId, uid)
        );
    }

    @GetMapping("/competency/{id}")
    public ResponseEntity<SubjectCompetency> getById(
            @PathVariable Integer id,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        return ResponseEntity.ok(
                serviceSubjectComp.getById(id, uid)
        );
    }

    @PutMapping("/competency/{id}")
    public ResponseEntity<SubjectCompetency> update(
            @PathVariable Integer id,
            @RequestBody SubjectCompetency dtoIN,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        SubjectCompetency response = serviceSubjectComp.update(id, dtoIN, uid);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/competency/{id}")
    public ResponseEntity<SubjectCompetency> remove(
            @PathVariable Integer id,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        SubjectCompetency response = serviceSubjectComp.remove(id, uid);
        return ResponseEntity.ok(response);
    }
}
