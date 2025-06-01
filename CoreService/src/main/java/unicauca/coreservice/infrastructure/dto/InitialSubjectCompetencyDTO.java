package unicauca.coreservice.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import unicauca.coreservice.domain.model.SubjectCompetency;
import unicauca.coreservice.domain.model.SubjectOutcome;

import java.util.List;

@AllArgsConstructor
@Data
public class InitialSubjectCompetencyDTO {
    private SubjectCompetency competency;
    private List<SubjectOutcome> outcomes;
}
