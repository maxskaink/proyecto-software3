package unicauca.coreservice.domain.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Criterion {
    private Integer id;
    private Double weight;
    private String name;
    @JsonIgnore
    private Rubric rubric;
    private List<Level> levels;
}
