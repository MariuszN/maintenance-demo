package pl.nurkowski.maintenance.model;


import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Person implements CarObserver {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Long Id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "surname", nullable = false)
    private String surname;

    @ManyToMany
    @JoinTable(
            name = "car_person",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"))
    private List<Car> cars = new ArrayList<>();


    public Person(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public Person() {
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Person{");
        sb.append("Id=").append(Id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", surname='").append(surname).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        if (!Id.equals(person.Id)) return false;
        if (!name.equals(person.name)) return false;
        return surname.equals(person.surname);
    }

    @Override
    public int hashCode() {
        int result = Id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + surname.hashCode();
        return result;
    }

    public void update() {
        //System.out.println("");
    }

    public List<Car> getCars() {
        return cars;
    }
}
