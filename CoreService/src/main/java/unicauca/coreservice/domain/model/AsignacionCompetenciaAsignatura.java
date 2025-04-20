package unicauca.coreservice.domain.model;

import lombok.Getter;
import lombok.Setter;
import unicauca.coreservice.domain.exception.InvalidValue;

@Getter
@Setter
public class AsignacionCompetenciaAsignatura {
    private Integer id;
    private CompetenciaAsignatura competencia;
    private Asignatura asignatura;
    private Periodo periodo;

    public AsignacionCompetenciaAsignatura(
            Integer id,
            CompetenciaAsignatura competencia,
            Asignatura asignatura,
            Periodo periodo
    ){
        setId(id);
        setCompetencia(competencia);
        setAsignatura(asignatura);
        setPeriodo(periodo);
    }

    public void setCompetencia(CompetenciaAsignatura competencia){
        if(null==competencia)
            throw new InvalidValue("La competencia no puede ser nula");
        this.competencia = competencia;
    }

    public void setAsignatura(Asignatura asignatura){
        if(null==asignatura)
            throw new InvalidValue("La asignatura no puede ser nula");
        this.asignatura = asignatura;
    }
    public void setPeriodo(Periodo periodo){
        if(null==periodo)
            throw new InvalidValue("El periodo no puede ser nulo");
        this.periodo = periodo;
    }
}
