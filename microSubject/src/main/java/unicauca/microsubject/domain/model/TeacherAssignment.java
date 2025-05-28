package unicauca.microsubject.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TeacherAssignment {
    private Integer id;
    private Term term;
    private Subject subject;
    private String teacherUid;
}
