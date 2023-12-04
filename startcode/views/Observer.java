package views;

/**
 * The Observer interface represents an object that observes changes in a subject's size or style.
 */
public interface Observer {

    /**
     * Updates the observer with new size information.
     *
     * @param newSize The new size information received from the subject.
     */
    void updateSize(String newSize);

    /**
     * Updates the observer with new style information.
     *
     * @param newStyle The new style information received from the subject.
     */
    void updateStyle(String newStyle);
}