package unicauca.coreservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Asignatura {

    private Integer id;
    private String nombre;
    private String descripcion;

}
