package pl.nurkowski.maintenance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nurkowski.maintenance.model.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByModel(String model);

    Optional<Car> findById(Long id);

}
