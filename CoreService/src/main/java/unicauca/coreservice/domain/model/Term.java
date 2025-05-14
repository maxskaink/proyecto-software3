package unicauca.coreservice.domain.model;

import lombok.Getter;
import lombok.Setter;
import unicauca.coreservice.domain.exception.InvalidValue;

@Getter
@Setter
public class Periodo {
    private Integer id;
    private String descripcion;

    public Periodo(Integer id, String descripcion){
        setId(id);
        setDescripcion(descripcion);
    }

    public void setDescripcion(String descripcion){
        if(null==descripcion)
            throw new InvalidValue("La descripcion de un periodo no puede ser nula");
        if(descripcion.trim().isEmpty())
            throw new InvalidValue("La descripcion de un periodo no puede estar vacia");
    }
}
