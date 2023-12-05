package AdventureModel;

import java.io.Serializable;

/**
 * The PauseTimerState represents the state where the game timer is paused.
 */
public class PauseTimerState implements TimerState, Serializable {

    /**
     * Handles a button push event in the PauseTimerState, triggering a state transition to ResumeTimerState.
     * Also starts the game timer.
     *
     * @param adventureGame The AdventureGame instance associated with the GameTimer.
     */
    @Override
    public void buttonPush(AdventureGame adventureGame) {
        adventureGame.setTimerState(new ResumeTimerState());
        adventureGame.gameTimer.startTimer();
    }

    /**
     * Gets a description of the timer state.
     *
     * @return A string description of the timer state.
     */
    @Override
    public String description() {
        return "PauseTimerState";
    }
}