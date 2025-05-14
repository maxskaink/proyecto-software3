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

    @Column(name = "id_evaluador", nullable = false)
    private Integer evaluatorId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_rubrica", referencedColumnName = "id")
    private RubricEntity rubric;
}