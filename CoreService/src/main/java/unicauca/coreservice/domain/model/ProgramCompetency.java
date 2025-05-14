package unicauca.coreservice.domain.model;

import lombok.Getter;
import lombok.Setter;
import unicauca.coreservice.domain.exception.InvalidValue;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProgramCompetency {
    private Integer id;
    private String description;
    private String level;
    private ProgramOutcome programOutcome;
    private List<SubjectCompetency> subjectCompetency;

    public ProgramCompetency(Integer id, String description, String level, ProgramOutcome programOutcome, List<SubjectCompetency> subjectCompetency) {
        setId(id);
        setDescription(description);
        setLevel(level);
        setProgramOutcome(programOutcome);
        setSubjectCompetencies(subjectCompetency);
    }

    public void setDescription(String description) {
        if( description == null || description.trim().isEmpty())
            throw new InvalidValue("The description can not be null or empty");
        if(description.trim().length() > 500)
            throw new InvalidValue("The competency description can not be longer than 500 characters");
        this.description = description;
    }
    public void setLevel(String level) {
        if( level == null || level.trim().isEmpty())
            throw new InvalidValue("The competency level can not be null or empty");
        if(level.trim().length() > 100)
            throw new InvalidValue("The competency level can not be longer than 100 characters");
        this.level = level;
    }
    public void setSubjectCompetencies(List<SubjectCompetency> subjectCompetency) {
        if(subjectCompetency == null){
            this.subjectCompetency = new ArrayList<>();
            return;
        }
        this.subjectCompetency = subjectCompetency;
    }
}
