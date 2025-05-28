package unicauca.coreservice.infrastructure.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import unicauca.coreservice.application.in.LevelInt;
import unicauca.coreservice.application.out.IAuthenticationService;
import unicauca.coreservice.domain.model.Level;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/level")
@AllArgsConstructor
public class LevelController {
    private final LevelInt levelService;
    private final IAuthenticationService authenticationService;

    @GetMapping("/{levelId}")
    public ResponseEntity<Level> getById(
            @PathVariable Integer levelId,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        return ResponseEntity.ok(
                levelService.getById(levelId, uid)
        );
    }

    @GetMapping("/criterion/{criterionId}")
    public ResponseEntity<List<Level>> listByCriterionId(
            @PathVariable Integer criterionId,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        return ResponseEntity.ok(
                levelService.listByCriterionId(criterionId, uid)
        );
    }

    @PostMapping("/criterion/{criterionId}")
    public ResponseEntity<Level> add(
            @RequestBody Level levelIn,
            @PathVariable Integer criterionId,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        Level response = levelService.add(levelIn, criterionId, uid);
        return ResponseEntity.ok(response);
    }

    @PutMapping("{levelId}")
    public ResponseEntity<Level> update(
            @PathVariable Integer levelId,
            @RequestBody Level levelIn,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        return ResponseEntity.ok(
                levelService.update(levelId, levelIn, uid)
        );
    }

    @DeleteMapping("{levelId}")
    public ResponseEntity<Void> remove(
            @PathVariable Integer levelId,
            HttpServletRequest request
    ) throws Exception {
        String uid = authenticationService.extractUidFromRequest(request);
        levelService.remove(levelId, uid);
        return ResponseEntity.noContent().build();
    }

    
    
}
