package unicauca.coreservice.domain.model;

import lombok.Getter;
import lombok.Setter;
import unicauca.coreservice.domain.exception.InvalidValue;

@Getter
@Setter
public class RAAsignatura {
    private Integer id;
    private String descripcion;

    public RAAsignatura(
            Integer id,
            String descripcion
    ){
        setId(id);
        setDescripcion(descripcion);
    }

    public void setDescripcion(String descripcion){
        if(null==descripcion)
            throw new InvalidValue("La descripcion de un RA de asignatura no puede ser nulo");
        if(descripcion.trim().isEmpty())
            throw new InvalidValue("La descripcion de un RA no puede estar vacia");

        this.descripcion = descripcion;
    }
}
