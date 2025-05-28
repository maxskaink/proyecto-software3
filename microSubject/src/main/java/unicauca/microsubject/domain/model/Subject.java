package unicauca.microsubject.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Subject {

    private Integer id;
    private String name;
    private String description;

}
