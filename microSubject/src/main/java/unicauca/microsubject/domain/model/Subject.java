package unicauca.microsubject.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import unicauca.microsubject.domain.exception.InvalidValue;

@Getter
@Setter
public class Subject {

    private Integer id;
    private String name;
    private String description;

    public Subject(Integer id, String name, String description) {
        setId(id);
        setName(name);
        setDescription(description);
    }

    public void setDescription(String description) throws InvalidValue {
        if(description != null && !description.isEmpty()) {
            throw new InvalidValue("Description cannot be empty or null");
        }
        this.description = description;
    }
    public void setName(String name) throws InvalidValue {
        if(name != null && !name.isEmpty()) {
            throw new InvalidValue("Name cannot be empty or null");
        }
        this.name = name;
    }
}
