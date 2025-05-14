package unicauca.coreservice.domain.model;

import lombok.Getter;
import lombok.Setter;
import unicauca.coreservice.domain.exception.InvalidValue;

@Getter
@Setter
public class SubjectCompetency {
    private Integer id;
    private String description;
    private String level;
    private Integer subjectCompetencyId;

    public SubjectCompetency(
            Integer id,
            String description,
            String level,
            Integer subjectCompetencyId
    ){
        setId(id);
        setDescription(description);
        setLevel(level);
        setSubjectCompetencyId(subjectCompetencyId);
    }

    public void setDescription(String description){
        if(description == null)
            throw new InvalidValue("The competency description can not be null");
        if(description.trim().isEmpty())
            throw new InvalidValue("The competency description can not be empty");
        this.description = description;
    }
    public void setLevel(String level){
        if(null==level)
            throw new InvalidValue("The competency level can not be null");
        if(level.trim().isEmpty())
            throw new InvalidValue("The competency level can not be empty");

        this.level = level;
    }

    public void setSubjectCompetencyId(Integer subjectCompetencyId){
        if(null==subjectCompetencyId)
            throw new InvalidValue("El id de la competencia no puede ser nulo");
        this.subjectCompetencyId = subjectCompetencyId;
    }
}
