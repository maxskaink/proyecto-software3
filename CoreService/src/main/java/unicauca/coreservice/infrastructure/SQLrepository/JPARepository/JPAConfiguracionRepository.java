package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.ConfigurationEntity;

public interface JPAConfiguracionRepository extends JpaRepository<ConfigurationEntity, Integer> {
}
