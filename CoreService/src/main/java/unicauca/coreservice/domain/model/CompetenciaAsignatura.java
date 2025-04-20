package unicauca.coreservice.domain.model;

import lombok.Getter;
import lombok.Setter;
import unicauca.coreservice.domain.exception.InvalidValue;

import java.util.List;

@Getter
@Setter
public class CompetenciaAsignatura {
    private Integer id;
    private String descripcion;
    private String nivel;
    private Integer idCompetenciaPrograma;

    public CompetenciaAsignatura(
            Integer id,
            String descripcion,
            String nivel,
            Integer idCompetenciaPrograma
    ){
        setId(id);
        setDescripcion(descripcion);
        setNivel(nivel);
        setIdCompetenciaPrograma(idCompetenciaPrograma);
    }

    public void setDescripcion(String descripcion){
        if(descripcion == null)
            throw new InvalidValue("La descripcion de una competencia de asignatura no puede ser nulo");
        if(descripcion.trim().isEmpty())
            throw new InvalidValue("La descripcion de una competencia no puede estar vacia");
        this.descripcion = descripcion;
    }
    public void setNivel(String nivel){
        if(null==nivel)
            throw new InvalidValue("La nivel de una competencia no puede ser nulo");
        if(nivel.trim().isEmpty())
            throw new InvalidValue("El nivel no puede estar vacio");

        this.nivel = nivel;
    }

    public void setIdCompetenciaPrograma(Integer idCompetenciaPrograma){
        if(null==idCompetenciaPrograma)
            throw new InvalidValue("El id de la competencia no puede ser nulo");
        this.idCompetenciaPrograma = idCompetenciaPrograma;
    }
}
