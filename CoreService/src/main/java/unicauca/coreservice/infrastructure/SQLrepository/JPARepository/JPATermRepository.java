package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.TermEntity;

public interface JPATermRepository extends JpaRepository<TermEntity, Integer> {

}
