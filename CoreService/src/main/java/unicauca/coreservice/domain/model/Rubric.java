package unicauca.coreservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import unicauca.coreservice.domain.exception.InvalidValue;

@Getter
@Setter
@AllArgsConstructor
public class Rubric {
    private Integer id;
    private String description;
    private SubjectOutcome subjectOutcome;

    public void setDescription(String description) {
        if(null==description)
            throw new InvalidValue("The rubric description can not be null ");
        if(description.trim().isEmpty())
            throw new InvalidValue("The rubric description can not be empty");

        this.description = description;
    }
}
