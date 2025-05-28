package unicauca.microsubject.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.microsubject.infrastructure.SQLrepository.entity.TermEntity;

import java.util.Optional;

public interface JPATermRepository extends JpaRepository<TermEntity, Integer> {
    Optional<TermEntity> findActiveTermById(TermEntity term);
}
