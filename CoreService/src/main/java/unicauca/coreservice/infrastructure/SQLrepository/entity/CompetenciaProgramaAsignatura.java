package unicauca.coreservice.infrastructure.SQLrepository.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "competencia_programa_asignatura")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetenciaProgramaAsignatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_competencia", referencedColumnName = "id")
    private CompetenciaAsignatura competencia;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_asignatura", referencedColumnName = "id")
    private Asignatura asignatura;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_periodo", referencedColumnName = "id")
    private Periodo periodo;

    @Column(nullable = false)
    private boolean activado;
}