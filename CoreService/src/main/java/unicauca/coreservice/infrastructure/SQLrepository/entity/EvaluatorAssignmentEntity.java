package unicauca.coreservice.infrastructure.SQLrepository.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "asignacion_evaluador")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluatorAssignmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_ra", referencedColumnName = "id")
    private SubjectOutcomeEntity subjectOutcome;

    @Column(name = "id_evaluador", nullable = false)
    private String evaluatorUid;


}