package unicauca.microsubject.infrastructure.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import unicauca.microsubject.application.in.EvaluatorAssignmentInt;
import unicauca.microsubject.domain.model.EvaluatorAssignment;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/evaluator-assignment")
@AllArgsConstructor
public class EvaluatorAssignmentController {

    private final EvaluatorAssignmentInt evaluatorAssignmentService;

    @PostMapping("/evaluator/{evaluatorUid}/subject/{subjectId}")
    public ResponseEntity<EvaluatorAssignment> addEvaluatorAssignment(
            @PathVariable String evaluatorUid,
            @PathVariable Integer subjectId) throws Exception {
        return ResponseEntity.ok(
                evaluatorAssignmentService.add(evaluatorUid, subjectId)
        );
    }

    @GetMapping("{evaluatorAssignmentId}")
    public ResponseEntity<EvaluatorAssignment>  getById(
            @PathVariable Integer evaluatorAssignmentId) throws Exception {
        return ResponseEntity.ok(
                evaluatorAssignmentService.getById(evaluatorAssignmentId)
        );
    }

    @GetMapping("/evaluator/{evaluatorUid}/subject/{subjectId}")
    public ResponseEntity<EvaluatorAssignment>  getByEvaluatorAndSubjectInActiveTerm(
            @PathVariable String evaluatorUid,
            @PathVariable Integer subjectId) throws Exception {
        return ResponseEntity.ok(
                evaluatorAssignmentService.getByEvaluatorAndSubjectInActiveTerm(evaluatorUid, subjectId)
        );
    }

    @GetMapping("")
    public ResponseEntity<List<EvaluatorAssignment>>  listEvaluatorAssignmentsInActiveTerm() {
        return ResponseEntity.ok(
                evaluatorAssignmentService.listEvaluatorAssignmentsInActiveTerm()
        );
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<EvaluatorAssignment>>  listBySubjectId(
            @PathVariable Integer subjectId) {
        return ResponseEntity.ok(
                evaluatorAssignmentService.listBySubjectId(subjectId)
        );
    }

    @GetMapping("/evaluator/{evaluatorUid}")
    public ResponseEntity<List<EvaluatorAssignment>>  listByEvaluatorUid(
            @PathVariable String evaluatorUid) {
        return ResponseEntity.ok(
                evaluatorAssignmentService.listByEvaluatorUid(evaluatorUid)
        );
    }

    @DeleteMapping("{evaluatorAssignmentId}")
    public ResponseEntity<Void> remove(
            @PathVariable Integer evaluatorAssignmentId) throws Exception {
        evaluatorAssignmentService.remove(evaluatorAssignmentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/evaluator/{evaluatorUid}/subject/{subjectId}")
    public ResponseEntity<EvaluatorAssignment> removeByEvaluatorAndSubject(
            @PathVariable String evaluatorUid,
            @PathVariable Integer subjectId) throws Exception {
        return ResponseEntity.ok(
                evaluatorAssignmentService.removeByEvaluatorAndSubjectInActiveTerm(evaluatorUid, subjectId)
        );
    }
    
    
}
