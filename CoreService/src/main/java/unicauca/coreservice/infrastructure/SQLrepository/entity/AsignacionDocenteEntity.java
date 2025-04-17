package unicauca.coreservice.infrastructure.SQLrepository.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "asignacion_docente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsignacionDocenteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_periodo", referencedColumnName = "id")
    private PeriodoEntity periodo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_asignatura", referencedColumnName = "id")
    private AsignaturaEntity asignatura;

    @Column(nullable = false, name = "id_docente")
    private Integer idDocente;
}
