package pl.nurkowski.maintenance.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Car implements CarObservable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Long id;
    @Column(name = "model")
    private String model;
    @Column(name = "brand")
    private String brand;
    @Column(name = "engineCapacity")
    private Double engineCapacity;
    @Column(name = "enginePower")
    private Double enginePower;

    @ManyToMany(mappedBy = "cars")
    private List<Person> observers = new ArrayList<>();

    public Car() {
    }

    public Car(Builder builder) {
        this.id = builder.id;
        this.model = builder.model;
        this.brand = builder.brand;
        this.engineCapacity = builder.engineCapacity;
        this.enginePower = builder.enginePower;
    }

    public void addObserver(Person observer) {
        observers.add(observer);
    }

    public void removeObserver(Person observer) {
        observers.remove(observer);
    }

    public void updateObservers() {
        observers.forEach(Person::update);
    }

    public List<Person> getObservers() {
        return observers;
    }

    public void setObservers(List<Person> observers) {
        this.observers = observers;
    }

    public Long getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getBrand() {
        return brand;
    }

    public Double getEngineCapacity() {
        return engineCapacity;
    }

    public Double getEnginePower() {
        return enginePower;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setEngineCapacity(Double engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public void setEnginePower(Double enginePower) {
        this.enginePower = enginePower;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", brand='" + brand + '\'' +
                ", engineCapacity=" + engineCapacity +
                ", enginePower=" + enginePower + " KM " +
                '}';
    }

    public static class Builder {
        private Long id;
        private String model;
        private String brand;
        private Double engineCapacity;
        private Double enginePower;

        public Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder brand(String brand) {
            this.brand = brand;
            return this;
        }

        public Builder engineCapacity(Double engineCapacity) {
            this.engineCapacity = engineCapacity;
            return this;
        }

        public Builder enginePower(Double enginePower) {
            this.enginePower = enginePower;
            return this;
        }

        public Car build() {
            return new Car(this);
        }
    }
}