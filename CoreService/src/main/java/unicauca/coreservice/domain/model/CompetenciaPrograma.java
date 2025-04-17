package unicauca.coreservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import unicauca.coreservice.domain.exception.InvalidValue;

@Getter
@Setter
public class CompetenciaPrograma {
    private Integer id;
    private String descripcion;
    private String nivel;
    private RAPrograma raPrograma;

    public CompetenciaPrograma(Integer id, String descripcion, String nivel, RAPrograma raPrograma) {
        setId(id);
        setDescripcion(descripcion);
        setNivel(nivel);
        setRaPrograma(raPrograma);
    }

    public void setDescripcion(String descripcion) {
        if( descripcion == null || descripcion.trim().isEmpty())
            throw new InvalidValue("La descripcion de la competencia no puede ser nula ni vacia");
        if(descripcion.trim().length() > 500)
            throw new InvalidValue("La descripcion de la competencia no puede ser mayor a 500 caracteres");
        this.descripcion = descripcion;
    }
    public void setNivel(String nivel) {
        if( nivel == null || nivel.trim().isEmpty())
            throw new InvalidValue("El nivel de la competencia no puede ser nulo ni vacia");
        if(nivel.trim().length() > 100)
            throw new InvalidValue("El nivel de la competencia no puede ser mayor a 100 caracteres");
        this.nivel = nivel;
    }
}
