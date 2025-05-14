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
public class AsignacionCompetenciaAsignaturaEntity {

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

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    private List<RAAsignaturaEntity> RAAsignaturas;

}