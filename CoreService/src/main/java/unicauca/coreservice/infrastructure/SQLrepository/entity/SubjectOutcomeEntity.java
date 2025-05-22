package unicauca.coreservice.infrastructure.SQLrepository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "ra_asignatura")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectOutcomeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 500)
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_competencia", nullable = false)
    private SubjectCompetencyAssignmentEntity competencyAssignment;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "rubric_id")
    @JsonIgnore
    private RubricEntity rubric;


    @Column(nullable = false)
    private boolean isActivated;
}