package unicauca.coreservice.infrastructure.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import unicauca.coreservice.application.in.LevelInt;
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

    @GetMapping("/{levelId}")
    public ResponseEntity<Level> getById(
            @PathVariable Integer levelId
    ) throws Exception {
        return ResponseEntity.ok(
                levelService.getById(levelId)
        );
    }

    @GetMapping("/criterion/{criterionId}")
    public ResponseEntity<List<Level>> listByCriterionId(
            @PathVariable Integer criterionId
    ) throws Exception {
        return ResponseEntity.ok(
                levelService.listByCriterionId(criterionId)
        );
    }

    @PostMapping("/criterion/{criterionId}")
    public ResponseEntity<Level> add(@RequestBody Level levelIn, @PathVariable Integer criterionId ) throws Exception {
        Level response = levelService.add(levelIn, criterionId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("{levelId}")
    public ResponseEntity<Level> update(
            @PathVariable Integer levelId,
            @RequestBody Level levelIn
    ) throws Exception {
        return ResponseEntity.ok(
                levelService.update(levelId, levelIn)
        );
    }

    @DeleteMapping("{levelId}")
    public ResponseEntity<Void> remove(@PathVariable Integer levelId) throws Exception {
        levelService.remove(levelId);
        return ResponseEntity.noContent().build();
    }

    
    
}
