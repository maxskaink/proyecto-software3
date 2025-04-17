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
public class CompetenciaAsignatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(nullable = false, length = 100)
    private String nivel;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_competencia_peograma", referencedColumnName = "id")
    private CompetenciaPrograma competenciaPrograma;

    @Column(nullable = false)
    private boolean activado;
}