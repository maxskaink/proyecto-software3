package unicauca.coreservice.infrastructure.SQLrepository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "competencia_programa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetenciaProgramaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 500, unique = true)
    private String descripcion;

    @Column(nullable = false, length = 100)
    private String nivel;

    @OneToMany(mappedBy = "competenciaPrograma", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompetenciaAsignaturaEntity> competenciasAsignatura = new ArrayList<>();

    @OneToOne(mappedBy = "competencia", cascade = CascadeType.ALL, orphanRemoval = true)
    private RAProgramaEntity resultadosAprendizaje;

    @Column(nullable = false)
    private boolean activado;
}