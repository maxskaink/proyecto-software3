package unicauca.coreservice.infrastructure.SQLrepository.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "criterio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CriterioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Double ponderacion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_rubrica", referencedColumnName = "id")
    private RubricaEntity rubrica;
}
