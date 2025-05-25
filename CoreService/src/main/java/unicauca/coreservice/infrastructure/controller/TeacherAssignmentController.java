package unicauca.coreservice.infrastructure.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import unicauca.coreservice.application.in.TeacherAssignmentInt;
import unicauca.coreservice.domain.model.TeacherAssignment;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/teacher-assignment")
@AllArgsConstructor
public class TeacherAssignmentController {

    private final TeacherAssignmentInt teacherAssignmentService;

    @PostMapping("/teacher/{teacherUid}/subject/{subjectId}")
    public ResponseEntity<TeacherAssignment> add(
            @PathVariable String teacherUid,
            @PathVariable Integer subjectId
    ) throws Exception {
        TeacherAssignment response = teacherAssignmentService.add( 
            teacherUid, subjectId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{teacherAssignmentId}")
    public ResponseEntity<TeacherAssignment> getById(
            @PathVariable Integer teacherAssignmentId
    ) throws Exception {
        return ResponseEntity.ok(
                teacherAssignmentService.getById(teacherAssignmentId)
        );
    }

    @GetMapping("/teacher/{teacherUid}/subject/{subjectId}")
    public ResponseEntity<TeacherAssignment> getByTeacherAndSubjectInActiveTerm(
            @PathVariable String teacherUid,
            @PathVariable Integer subjectId
    ) throws Exception {
        return ResponseEntity.ok(
                teacherAssignmentService.getByTeacherAndSubjectInActiveTerm(teacherUid, subjectId)
        );
    }

    @GetMapping("")
    public ResponseEntity<List<TeacherAssignment>> listTeacherAssignmentsInActiveTerm() {
        return ResponseEntity.ok(
                teacherAssignmentService.listTeacherAssignmentsInActiveTerm()
        );
    }
    
    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<TeacherAssignment>> listBySubjectId(
            @PathVariable Integer subjectId
    )  {
        return ResponseEntity.ok(
                teacherAssignmentService.listBySubjectId(subjectId)
        );
    }

    @GetMapping("/teacher/{teacherUid}")
    public ResponseEntity<List<TeacherAssignment>> listByTeacherUid(
            @PathVariable String teacherUid
    ) {
        return ResponseEntity.ok(
                teacherAssignmentService.listByTeacherUid(teacherUid)
        );
    }

    @DeleteMapping("/{teacherAssignmentId}")
    public ResponseEntity<TeacherAssignment> remove(
            @PathVariable Integer teacherAssignmentId
    ) throws Exception {
        return ResponseEntity.ok(
                teacherAssignmentService.remove(teacherAssignmentId)
        );
    }

    @DeleteMapping("/teacher/{teacherUid}/subject/{subjectId}")
    public ResponseEntity<TeacherAssignment> removeByTeacherAndSubjectInActiveTerm(
            @PathVariable String teacherUid,
            @PathVariable Integer subjectId
    ) throws Exception {
        return ResponseEntity.ok(
                teacherAssignmentService.removeByTeacherAndSubjectInActiveTerm(teacherUid, subjectId)
        );
    }
    
    
}
