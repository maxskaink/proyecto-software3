package unicauca.coreservice.infrastructure.SQLrepository.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "competencia_programa_asignatura")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetenciaProgramaAsignaturaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_competencia", referencedColumnName = "id")
    private CompetenciaAsignaturaEntity competencia;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_asignatura", referencedColumnName = "id")
    private AsignaturaEntity asignatura;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_periodo", referencedColumnName = "id")
    private PeriodoEntity periodo;

    @Column(nullable = false)
    private boolean activado;
}