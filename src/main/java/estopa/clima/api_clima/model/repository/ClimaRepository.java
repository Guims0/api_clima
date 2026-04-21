package estopa.clima.api_clima.model.repository;

import estopa.clima.api_clima.model.entity.Clima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClimaRepository extends JpaRepository<Clima,Long> {
}
