package unicauca.coreservice.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import unicauca.coreservice.domain.exception.InvalidValue;

import java.util.List;

@Getter
@Setter
public class Rubric {
    private Integer id;
    private String description;
    @JsonIgnore
    private SubjectOutcome subjectOutcome;
    private List<Criterion> criteria;

    public Rubric(Integer id, String description, SubjectOutcome subjectOutcome, List<Criterion> criteria) {
        setId(id);
        setDescription(description);
        setSubjectOutcome(subjectOutcome);
        setCriteria(criteria);
    }

    public void setDescription(String description) {
        if(null==description)
            throw new InvalidValue("The rubric description can not be null ");
        if(description.trim().isEmpty())
            throw new InvalidValue("The rubric description can not be empty");

        this.description = description;
    }
}
