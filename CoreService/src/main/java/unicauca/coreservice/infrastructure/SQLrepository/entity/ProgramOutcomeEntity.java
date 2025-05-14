package unicauca.coreservice.infrastructure.SQLrepository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "ra_programa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramOutcomeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 500)
    private String description;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_competencia", referencedColumnName = "id")
    private ProgramCompetencyEntity competency;

    @Column(nullable = false)
    private boolean isActivated;
}