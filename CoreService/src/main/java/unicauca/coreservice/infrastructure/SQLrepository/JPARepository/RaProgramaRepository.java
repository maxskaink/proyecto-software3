package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.RAPrograma;

public interface RaProgramaRepository extends JpaRepository<RAPrograma, Integer> {
}
