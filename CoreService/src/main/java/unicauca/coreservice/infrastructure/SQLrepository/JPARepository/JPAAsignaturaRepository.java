package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectEntity;

import java.util.Optional;

public interface JPAAsignaturaRepository extends JpaRepository<SubjectEntity, Integer> {
    Optional<SubjectEntity> findByIdAndActivadoTrue(Integer id);
}
