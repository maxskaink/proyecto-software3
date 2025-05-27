package unicauca.coreservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EvaluatorAssignment {
    private Integer id;
    private Term term;
    private SubjectOutcome subjectOutcome;
    private String evaluatorUid;
}
