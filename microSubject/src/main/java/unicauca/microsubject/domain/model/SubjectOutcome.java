package unicauca.microsubject.domain.model;

import lombok.Getter;
import lombok.Setter;
import unicauca.microsubject.domain.exception.InvalidValue;

@Getter
@Setter
public class SubjectOutcome {
    private Integer id;
    private String description;

    public SubjectOutcome(Integer id, String description){
        setId(id);
        setDescription(description);
    }

    public void setDescription(String description){
        if(null==description)
            throw new InvalidValue("The subject description can not be null or empty");
        if(description.trim().isEmpty())
            throw new InvalidValue("The outcome description can not be empty");

        this.description = description;
    }
}
