package unicauca.microsubject.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.microsubject.infrastructure.SQLrepository.entity.ConfigurationEntity;

public interface JPAConfigurationRepository extends JpaRepository<ConfigurationEntity, Integer> {
}
