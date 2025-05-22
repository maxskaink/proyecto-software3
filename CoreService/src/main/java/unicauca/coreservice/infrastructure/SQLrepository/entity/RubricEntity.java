package unicauca.coreservice.infrastructure.SQLrepository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rubrica")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RubricEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(name = "subject_outcome_id")
    private Integer subjectOutcomeId;


    @OneToMany(mappedBy = "rubric", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private List<CriterionEntity> criteria = new ArrayList<>();

    @Column(nullable = false)
    private boolean isActivated;
}