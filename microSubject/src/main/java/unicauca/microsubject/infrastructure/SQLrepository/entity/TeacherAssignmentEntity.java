package unicauca.microsubject.infrastructure.SQLrepository.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "asignacion_docente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherAssignmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_periodo", referencedColumnName = "id")
    private TermEntity term;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_asignatura", referencedColumnName = "id")
    private SubjectEntity subject;

    @Column(nullable = false, name = "id_docente")
    private String teacherUid;
}
