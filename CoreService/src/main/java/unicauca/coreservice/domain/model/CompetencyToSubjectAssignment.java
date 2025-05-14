package unicauca.coreservice.domain.model;

import lombok.Getter;
import lombok.Setter;
import unicauca.coreservice.domain.exception.InvalidValue;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class CompetencyToSubjectAssignment {
    private Integer id;
    private SubjectCompetency competency;
    private Subject subject;
    private Term term;
    private List<SubjectOutcome> SubjectOutcomes;

    public CompetencyToSubjectAssignment(
            Integer id,
            SubjectCompetency competency,
            Subject subject,
            Term term,
            List<SubjectOutcome> SubjectOutcomes
    ){
        setId(id);
        setCompetency(competency);
        setSubject(subject);
        setTerm(term);
        setSubjectOutcomes(SubjectOutcomes);
    }

    public void setCompetency(SubjectCompetency competency){
        if(null== competency)
            throw new InvalidValue("The competency can not be null");
        this.competency = competency;
    }

    public void setSubject(Subject subject){
        if(null== subject)
            throw new InvalidValue("The subject can not be null");
        this.subject = subject;
    }
    public void setTerm(Term term){
        if(null== term)
            throw new InvalidValue("The term can not be null");
        this.term = term;
    }
    public void setSubjectOutcomes(List<SubjectOutcome> SubjectOutcomes){
        this.SubjectOutcomes = Objects.requireNonNullElseGet(SubjectOutcomes, List::of);
    }
}
