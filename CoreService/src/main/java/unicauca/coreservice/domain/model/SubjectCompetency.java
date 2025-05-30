package unicauca.coreservice.domain.model;

import lombok.Getter;
import lombok.Setter;
import unicauca.coreservice.domain.exception.InvalidValue;

@Getter
@Setter
public class SubjectCompetency {
    private Integer id;
    private String description;
    private Integer programCompetencyId;

    public SubjectCompetency(
            Integer id,
            String description,
            Integer programCompetencyId
    ){
        setId(id);
        setDescription(description);
        setProgramCompetencyId(programCompetencyId);
    }

    public void setDescription(String description){
        if(description == null)
            throw new InvalidValue("The competency description can not be null");
        if(description.trim().isEmpty())
            throw new InvalidValue("The competency description can not be empty");
        this.description = description;
    }

    public void setProgramCompetencyId(Integer programCompetencyId){
        if(null== programCompetencyId)
            throw new InvalidValue("The program competency Id can not be null");
        this.programCompetencyId = programCompetencyId;
    }
}
