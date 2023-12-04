package views;

/**
 * The Subject interface represents an object that can be observed by one or more observers.
 * Observers can be notified of changes in size or style through the provided methods.
 */
public interface Subject {

    /**
     * Adds an observer to the list of observers for this subject.
     *
     * @param observer The observer to be added.
     */
    void addObserver(Observer observer);

    /**
     * Notifies all registered observers about a change in size.
     *
     * @param newSize The new size information to be communicated to observers.
     */
    void notifyObservers_Size(String newSize);

    /**
     * Notifies all registered observers about a change in style.
     *
     * @param newStyle The new style information to be communicated to observers.
     */
    void notifyObservers_Style(String newStyle);
}
