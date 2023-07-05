package pl.nurkowski.maintenance.model;


public interface CarObservable {
    void addObserver(Person observer);
    void removeObserver(Person observer);
    void updateObservers();
}
