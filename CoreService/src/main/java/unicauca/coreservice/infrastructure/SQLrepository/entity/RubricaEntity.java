package unicauca.coreservice.infrastructure.SQLrepository.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rubrica")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RubricaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_ra", referencedColumnName = "id")
    private RAAsignaturaEntity resultadoAprendizaje;

    @Column(nullable = false)
    private boolean activado;
}