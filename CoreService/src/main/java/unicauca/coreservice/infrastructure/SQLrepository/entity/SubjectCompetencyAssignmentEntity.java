package unicauca.coreservice.infrastructure.SQLrepository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "asignacion_competencia_asignatura")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectCompetencyAssignmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_competencia", referencedColumnName = "id")
    private SubjectCompetencyEntity competency;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_asignatura", referencedColumnName = "id")
    private SubjectEntity subject;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_periodo", referencedColumnName = "id")
    private TermEntity term;

    @Column(nullable = false)
    private boolean isActivated;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    private List<SubjectOutcomeEntity> subjectOutcomes;

}