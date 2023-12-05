package AdventureModel;

import java.io.Serializable;

/**
 * The TimerState interface defines the contract for different states of the game timer.
 * Each state encapsulates specific behavior for the timer and allows transitions between states.
 *
 */
public interface TimerState extends Serializable {

    /**
     * Handles a button push event, triggering a state transition or action.
     *
     * @param adventureGame The AdventureGame instance associated with the GameTimer.
     */
    void buttonPush(AdventureGame adventureGame);

    /**
     * Gets a description of the timer state.
     *
     * @return A string description of the timer state.
     */
    String description();
}
