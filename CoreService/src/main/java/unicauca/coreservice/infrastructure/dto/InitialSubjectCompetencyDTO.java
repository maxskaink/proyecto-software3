package unicauca.coreservice.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import unicauca.coreservice.domain.model.SubjectCompetency;
import unicauca.coreservice.domain.model.SubjectOutcome;

@AllArgsConstructor
@Data
public class InitialSubjectCompetencyDTO {
    private SubjectCompetency competency;
    private SubjectOutcome outcome;
}
