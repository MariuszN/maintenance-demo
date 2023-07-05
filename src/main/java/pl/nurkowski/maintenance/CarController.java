package pl.nurkowski.maintenance;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.nurkowski.maintenance.model.Car;
import pl.nurkowski.maintenance.model.Person;
import pl.nurkowski.maintenance.repository.CarRepository;
import pl.nurkowski.maintenance.repository.PersonRepository;
import pl.nurkowski.maintenance.services.ApiService;
import pl.nurkowski.maintenance.services.CarService;
import pl.nurkowski.maintenance.services.PersonService;
import pl.nurkowski.maintenance.model.VoivodeshipName;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping
public class CarController {
    private CarService carService;
    private PersonService personService;
    private ApiService apiService;
    private Car car;
    private Map<String, Integer> carCountMap = new HashMap<>();
    private final CarRepository carRepository;
    private final PersonRepository personRepository;

    public CarController(CarService carService, PersonService personService,
                         CarRepository carRepository,
                         PersonRepository personRepository,
                         ApiService apiService) {
        this.carService = carService;
        this.apiService = apiService;
        this.personService = personService;
        this.carRepository = carRepository;
        this.personRepository = personRepository;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/hello")
    public String hello(Model model, HttpSession session) {
        List<Car> cars = carService.getAllCars();
        List<Person> persons = personService.getAllPersons();
        Map<Car, List<Person>> carObserversMap = new HashMap<>();

        for (Car car : cars) {
            carObserversMap.put(car, car.getObservers());
        }

        Map<Person, List<Car>> personObserverMap = new HashMap<>();
        for (Person person : persons) {
            List<Car> observedCars = new ArrayList<>();

            for (Car car : person.getCars()) {
                if (car.getObservers().contains(person)) {
                    observedCars.add(car);
                    car.updateObservers();
                }
            }
            personObserverMap.put(person, observedCars);
        }

        String selectedVoivodeship = (String) session.getAttribute("selectedVoivodeship");
        if (selectedVoivodeship == null || selectedVoivodeship.isEmpty()) {
            selectedVoivodeship = "";
        }

        model.addAttribute("carObserversMap", carObserversMap);
        model.addAttribute("personObserverMap", personObserverMap);
        model.addAttribute("carCountMap", carCountMap);
        model.addAttribute("selectedVoivodeship", selectedVoivodeship);
        model.addAttribute("voivodeships", VoivodeshipName.values());
        model.addAttribute("voivodeshipNameCodes", VoivodeshipName.getCodeMap());
        return "hello";
    }

    @PostMapping("/hello/countCars")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String countCars(Model model, @RequestParam(required = false, name = "voivodeshipName") String voivodeshipName, HttpSession session) {

        List<Car> cars = carService.getAllCars();
        for (Car car : cars) {
            if (voivodeshipName != null) {
                int carsDtoCount = apiService.getCarsDto(voivodeshipName, car.getBrand()).size();
                carCountMap.put(car.getBrand(), carsDtoCount);
            }
        }
        String voivodeshipNameByCode = getVoivodeshipNameByCode(voivodeshipName);
        session.setAttribute("selectedVoivodeship", voivodeshipNameByCode);
        model.addAttribute("voivodeshipName", voivodeshipName);
        return hello(model, session);
    }

    private String getVoivodeshipNameByCode(String code) {
        Map<String, String> codeMap = VoivodeshipName.getCodeMap();
        for (Map.Entry<String, String> entry : codeMap.entrySet()) {
            if (entry.getKey().equals(code)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @GetMapping("/addCar")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String showAddCarForm(Model model) {
        List<Person> observers = personService.getAllPersons();
        model.addAttribute("persons", observers);
        model.addAttribute("car", new Car());
        return "addCar";
    }

    @PostMapping("/addCar")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String createCar(@ModelAttribute("car") Car car, Model model, @RequestParam(required = false, name = "persons") List<Long> observerIds) throws IOException {

        Car createdCar = carService.createCar(car);
        if (observerIds != null) {
            List<Person> observers = personRepository.findAllById(observerIds);
            createdCar.setObservers(observers);
            carRepository.save(createdCar);
            Map<Car, List<Person>> carObserversMap = new HashMap<>();
            for (Person person : observers) {
                person.getCars().add(createdCar);
                personRepository.save(person);
            }
            for (Car carObj : carService.getAllCars()) {
                carObserversMap.put(carObj, carObj.getObservers());
            }
        }
        return "redirect:/observers";
    }

    @GetMapping("/addObserver")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String showAddObserverForm(Model model) {
        List<Car> cars = carService.getAllCars();
        model.addAttribute("cars", cars);
        model.addAttribute("person", new Person());
        return "addObserver";
    }

    @PostMapping("/addObserver")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String createObserver(@ModelAttribute("car") Person person, Model model, @RequestParam(required = false, name = "cars") List<Long> carIds) {
        Person createdObserver = personService.createPerson(person);
        personRepository.save(createdObserver);
        if (carIds != null) {
            List<Car> cars = carRepository.findAllById(carIds);
            createdObserver.setCars(cars);
            personRepository.save(createdObserver);
            Map<Person, List<Car>> personMap = new HashMap<>();
            for (Car car : cars) {
                car.getObservers().add(createdObserver);
                carRepository.save(car);
            }
            for (Person personObj : personService.getAllPersons()) {
                personMap.put(personObj, personObj.getCars());
            }
        }
        return "redirect:/addCar";
    }

    @GetMapping("/observers")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String showObservers(Model model) {
        List<Person> persons = personService.getAllPersons();
        List<Car> cars = carService.getAllCars();
        Map<Person, List<Car>> personObserverMap = new HashMap<>();
        for (Person person : persons) {
            List<Car> observedCars = new ArrayList<>();

            for (Car car : person.getCars()) {
                if (car.getObservers().contains(person)) {
                    observedCars.add(car);
                    car.updateObservers();
                }
            }
            personObserverMap.put(person, observedCars);
        }
        model.addAttribute("personObserverMap", personObserverMap);
        model.addAttribute("persons", persons);
        model.addAttribute("cars", cars);
        return "observers";
    }

    @PostMapping("/hello/car/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String deleteCarById(@PathVariable("id") Long id) {
        if (carRepository.existsById(id)) {
            carService.deleteCarById(id);
        }
        return "redirect:/hello";
    }

    @PostMapping("/hello/person/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String deleteCarPersonById(@PathVariable("id") Long id) {
        if (personRepository.existsById(id)) {
            personService.deletePersonById(id);
        }
        return "redirect:/observers";
    }

    @GetMapping("/error")
    public String handleError(Model model) {
        model.addAttribute("404", "404.jpg");
        return "error-404";
    }

    @GetMapping("/")
    public String homePage() {
        return "redirect:/hello";
    }

    @RequestMapping("*")
    public String fallback() {
        return "redirect:/error";
    }

}