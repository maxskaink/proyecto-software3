package unicauca.coreservice.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicauca.coreservice.application.in.TermInt;
import unicauca.coreservice.domain.model.Term;

import java.util.List;

@RestController
@RequestMapping("/term")
@AllArgsConstructor
public class TermController {

    private final TermInt serviceTerm;

    @PostMapping
    public ResponseEntity<Term> add(@RequestBody Term newTerm) throws Exception {
        Term response = serviceTerm.add(newTerm);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Term>> listAll() {
        return ResponseEntity.ok(serviceTerm.listAll());
    }

    @GetMapping("/active")
    public ResponseEntity<Term> getActiveTerm() throws Exception {
        return ResponseEntity.ok(serviceTerm.getActiveTerm());
    }

    @PutMapping("/{termId}/activate")
    public ResponseEntity<Term> setActiveTerm(@PathVariable Integer termId) throws Exception {
        Term response = serviceTerm.setActiveTerm(termId);
        return ResponseEntity.ok(response);
    }
}
