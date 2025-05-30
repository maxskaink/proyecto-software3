package unicauca.coreservice.infrastructure.SQLrepository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "competencia_asignatura")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectCompetencyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 500)
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_competencia_peograma", nullable = false)
    private ProgramCompetencyEntity programCompetency;

    @Column(nullable = false)
    private boolean isActivated;
}