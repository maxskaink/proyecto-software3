package unicauca.microsubject.infrastructure.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicauca.microsubject.application.in.SubjectInt;
import unicauca.microsubject.application.out.IAuthenticationService;
import unicauca.microsubject.domain.model.Subject;

import java.util.List;

@RestController
@RequestMapping("/subject")
@AllArgsConstructor
public class SubjectController {
    private SubjectInt subjectService;
    private IAuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<Subject> createSubject(
        @RequestBody Subject newSubject,
        HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        return ResponseEntity.ok(subjectService.add(newSubject, uid));
    }

    @GetMapping
    public ResponseEntity<List<Subject>> listALlSubjects(HttpServletRequest request) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        return ResponseEntity.ok(subjectService.listAll(uid));
    }

    @GetMapping("/assigned")
    public ResponseEntity<List<Subject>> listAssignedSubjects(HttpServletRequest request) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        return ResponseEntity.ok(subjectService.listAssigned(uid));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subject> getById(
            @PathVariable Integer id,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        return ResponseEntity.ok(subjectService.getById(id, uid));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subject> updateById(
            @PathVariable Integer id,
            @RequestBody Subject newSubject,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        return ResponseEntity.ok(subjectService.update(id, newSubject,uid));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Subject> removeById(
            @PathVariable Integer id,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        return ResponseEntity.ok(subjectService.remove(id, uid));
    }
}
