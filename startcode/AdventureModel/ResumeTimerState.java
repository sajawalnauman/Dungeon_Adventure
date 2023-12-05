package AdventureModel;

import java.io.Serializable;

/**
 * The ResumeTimerState represents the state where the game timer is resumed.
 */
public class ResumeTimerState implements TimerState, Serializable {

    /**
     * Handles a button push event in the ResumeTimerState, triggering a state transition to PauseTimerState.
     * Also stops the game timer.
     *
     * @param adventureGame The AdventureGame instance associated with the GameTimer.
     */
    @Override
    public void buttonPush(AdventureGame adventureGame) {
        adventureGame.setTimerState(new PauseTimerState());
        adventureGame.gameTimer.stopTimer();
    }

    /**
     * Gets a description of the timer state.
     *
     * @return A string description of the timer state.
     */
    @Override
    public String description() {
        return "ResumeTimerState";
    }
}