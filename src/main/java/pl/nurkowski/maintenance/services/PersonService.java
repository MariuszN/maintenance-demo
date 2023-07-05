package pl.nurkowski.maintenance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import pl.nurkowski.maintenance.model.Person;
import pl.nurkowski.maintenance.repository.CarRepository;
import pl.nurkowski.maintenance.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PersonService {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private CarRepository carRepository;

    public PersonService(PersonRepository personRepository, CarRepository carRepository) {
        this.personRepository = personRepository;
        this.carRepository = carRepository;
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Optional<Person> getPersonById(Long id) {
        return personRepository.findById(id);
    }

    public Person createPerson(Person person) {
        return personRepository.save(person);
    }

    public void updatePerson(Long id, Person updatedPerson) {
        Optional<Person> person = personRepository.findById(id);
        if (person.isPresent()) {
            Person currentPerson = person.get();
            currentPerson.setName(updatedPerson.getName());
            currentPerson.setSurname(updatedPerson.getSurname());
            personRepository.save(currentPerson);
        }
    }

    public void deletePersonById(Long id) {
        personRepository.deleteById(id);
    }

}
