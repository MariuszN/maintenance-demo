package pl.nurkowski.maintenance.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nurkowski.maintenance.config.CarBrandConfigReader;
import pl.nurkowski.maintenance.exception.InvalidCarBrandException;
import pl.nurkowski.maintenance.model.Car;
import pl.nurkowski.maintenance.model.CarBrandProvider;
import pl.nurkowski.maintenance.model.Person;
import pl.nurkowski.maintenance.model.User;
import pl.nurkowski.maintenance.repository.CarRepository;
import pl.nurkowski.maintenance.repository.PersonRepository;
import pl.nurkowski.maintenance.repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CarService {
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CarBrandProvider carBrandProvider;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private ObjectMapper objectMapper;

    public CarService() {
    }

    public CarService(CarRepository carRepository, PersonRepository personRepository, CarBrandProvider carBrandProvider) {
        this.carRepository = carRepository;
        this.personRepository = personRepository;
        this.carBrandProvider = carBrandProvider;
    }

    public void init() {
        Car.Builder bilder1 = new Car.Builder();
        bilder1.model("A3")
                .brand("Audi")
                .engineCapacity(2.0)
                .enginePower(140.0);

        Car car1 = bilder1.build();
        Car.Builder bilder2 = new Car.Builder();
        bilder2.model("500")
                .brand("Fiat")
                .engineCapacity(1.6)
                .enginePower(80.0);
        Car car2 = bilder2.build();

        Person person1 = new Person("Frank", "Boss");
        Person person2 = new Person("Erik", "Koch");
        Person person3 = new Person("Piotr", "Wasilewski");

        //car1.addObserver(person1);
        car1.getObservers().add(person1);
        //car2.addObserver(person2);
        car2.getObservers().add(person3);
        car2.getObservers().add(person2);
        //carRepository.save(car2);
        car2.addObserver(person3);
        carRepository.save(car1);
        carRepository.save(car2);

        person1.getCars().add(car1);
        person2.getCars().add(car2);
        person3.getCars().add(car2);
        personRepository.save(person1);
        personRepository.save(person2);
        personRepository.save(person3);
        User user = new User("user", passwordEncoder.encode("password"), "ROLE_USER");
        userRepository.save(user);
        User admin = new User("admin", passwordEncoder.encode("password"), "ROLE_ADMIN");
        userRepository.save(admin);
    }


    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    public Car createCar(Car car) throws IOException {
        CarBrandConfigReader carBrandConfigReader = new CarBrandConfigReader(resourceLoader, objectMapper);
        String brand = car.getBrand();
        List<String> validBrands = carBrandConfigReader.getValidBrands();

        for (String validBrand : validBrands) {
            if (validBrand.equalsIgnoreCase(brand)) {
                return carRepository.save(car);
            }
        }
        throw new InvalidCarBrandException("Invalid car brand: " + brand);
    }

    public void updateCar(Long id, Car updatedCar) {
        Optional<Car> optionalCar = carRepository.findById(id);
        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();
            car.setModel(updatedCar.getModel());
            car.setBrand(updatedCar.getBrand());
            car.setEngineCapacity(updatedCar.getEngineCapacity());
            car.setEnginePower(updatedCar.getEnginePower());
            carRepository.save(car);
        }
    }

    public void deleteCarById(Long id) {
        Optional<Car> optionalCar = carRepository.findById(id);
        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();
            car.getObservers().forEach(carObserver -> carObserver.getCars().remove(car));
            carRepository.deleteById(id);
        }
    }

    public void addObserverToCar(Long carId, Long personId) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        Optional<Person> optionalPerson = personRepository.findById(personId);
        if (optionalCar.isPresent() && optionalPerson.isPresent()) {
            Car car = optionalCar.get();
            Person person = optionalPerson.get();
            car.getObservers().add(person);
            person.getCars().add(car);
            carRepository.save(car);
            personRepository.save(person);
        }
    }

    public void removeObserverFromCar(Long carId, Long personId) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        Optional<Person> optionalPerson = personRepository.findById(personId);
        if (optionalCar.isPresent() && optionalPerson.isPresent()) {
            Car car = optionalCar.get();
            Person person = optionalPerson.get();
            car.getObservers().remove(person);
            person.getCars().remove(car);
            carRepository.save(car);
            personRepository.save(person);
        }
    }
}
