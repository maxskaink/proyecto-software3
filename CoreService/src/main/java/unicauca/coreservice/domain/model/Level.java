package unicauca.coreservice.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Level {
    private Integer id;
    private String category;
    private String description;
    private Integer percentage;
    @JsonIgnore
    private Criterion criterion;
}
