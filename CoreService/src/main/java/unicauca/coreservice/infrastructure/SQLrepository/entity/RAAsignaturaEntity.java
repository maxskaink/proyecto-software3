package unicauca.coreservice.infrastructure.SQLrepository.entity;

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
public class RAAsignaturaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_competencia", referencedColumnName = "id")
    private CompetenciaAsignaturaEntity competencia;

    @Column(nullable = false)
    private boolean activado;
}