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
public class ProgramCompetencyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 500, unique = true)
    private String description;

    @Column(nullable = false, length = 100)
    private String level;

    @OneToMany(mappedBy = "programCompetency", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SubjectCompetencyEntity> subjectCompetencies = new ArrayList<>();

    @OneToOne(mappedBy = "competency", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProgramOutcomeEntity learningOutcomes;

    @Column(nullable = false)
    private boolean isActivated;
}