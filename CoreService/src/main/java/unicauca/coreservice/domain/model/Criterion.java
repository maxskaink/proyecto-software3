package unicauca.coreservice.domain.model;

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
    private Rubric rubric;

}
