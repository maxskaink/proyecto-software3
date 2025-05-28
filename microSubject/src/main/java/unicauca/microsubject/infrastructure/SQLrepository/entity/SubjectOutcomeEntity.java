package unicauca.microsubject.infrastructure.SQLrepository.entity;

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
public class SubjectOutcomeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private boolean isActivated;
}