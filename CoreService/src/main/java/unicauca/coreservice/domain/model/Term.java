package unicauca.coreservice.domain.model;

import lombok.Getter;
import lombok.Setter;
import unicauca.coreservice.domain.exception.InvalidValue;

@Getter
@Setter
public class Term {
    private Integer id;
    private String description;

    public Term(Integer id, String description){
        setId(id);
        setDescription(description);
    }

    public void setDescription(String description){
        if(null==description)
            throw new InvalidValue("The term description can not be null");
        if(description.trim().isEmpty())
            throw new InvalidValue("The term description can not be empty");
        this.description = description;
    }

}
