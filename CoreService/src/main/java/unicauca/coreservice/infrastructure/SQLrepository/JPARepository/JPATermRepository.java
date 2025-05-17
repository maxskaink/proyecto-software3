package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.TermEntity;

import java.util.Optional;

public interface JPATermRepository extends JpaRepository<TermEntity, Integer> {
    Optional<TermEntity> findActiveTermById(TermEntity term);
}
