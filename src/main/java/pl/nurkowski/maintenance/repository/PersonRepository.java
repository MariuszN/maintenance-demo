package pl.nurkowski.maintenance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import pl.nurkowski.maintenance.model.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findByName(String model);

    Optional<Person> findById(Long id);

}
