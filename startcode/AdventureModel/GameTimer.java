package AdventureModel;

import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The GameTimer class represents a timer for tracking the progress of the game.
 * It allows for starting, stopping, and querying the current and remaining time.
 */
public class GameTimer implements Serializable {

    private transient ScheduledExecutorService scheduler; // Scheduler for updating the current time
    public Integer currentTime; // Current time on the game timer
    public Integer remainingTime; // Remaining time on the game timer
    public Integer endingTime; // Ending time on the game timer
    public int initialTime = 93; // Initial time limit for the game timer
    private boolean isRunning; // Flag indicating whether the timer is currently running


    /**
     * GameTimer Constructor
     * __________________________
     * Initializes the GameTimer with default values.
     */
    public GameTimer() {
        this.currentTime = 0;
        this.remainingTime = initialTime;
        this.endingTime = 0;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    /**
     * startTimer
     * __________________________
     * Starts the game timer, scheduling regular updates to the current time.
     */
    public void startTimer() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::updateCurrentTime, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * stopTimer
     * __________________________
     * Stops the game timer.
     */
    public void stopTimer() {
        scheduler.shutdown();
    }

    /**
     * isRunning
     * __________________________
     * Checks whether the game timer is currently running.
     *
     * @return True if the timer is running, false otherwise.
     */
    public boolean isRunning() {
        isRunning = !scheduler.isShutdown();
        return isRunning;
    }

    /**
     * getCurrentTime
     * __________________________
     * Gets the current time on the game timer.
     *
     * @return The current time in seconds.
     */
    public int getCurrentTime() {
        return currentTime;
    }

    /**
     * getRemainingTime
     * __________________________
     * Gets the remaining time on the game timer.
     *
     * @return The remaining time in seconds.
     */
    public Integer getRemainingTime() {
        return remainingTime;
    }

    /**
     * getEndingTime
     * __________________________
     * Gets the ending time on the game timer.
     *
     * @return The ending time in seconds.
     */
    public int getEndingTime() {
        endingTime = currentTime;
        return endingTime;
    }

    /**
     * updateCurrentTime
     * __________________________
     * Update the current time on the game timer.
     * Increments the current time by one second and checks
     * if the timer has reached its initial time limit.
     * If the limit is reached, the timer is stopped,
     * and relevant attributes are reset.
     */
    private void updateCurrentTime() {
        currentTime++;

        if (currentTime >= initialTime) {
            stopTimer();
            currentTime = initialTime;
            endingTime = 0;
            remainingTime = 0;
        } else {
            remainingTime = initialTime - currentTime;
        }
    }

    /**
     * readObject
     * Deserializing the GameTimer object.
     *
     * @param ois The ObjectInputStream used for deserialization.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     * @throws IOException            If an I/O error occurs during deserialization.
     */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    /**
     * writeObject
     * Serializing the GameTimer object.
     *
     * @param oos The ObjectOutputStream used for serialization.
     * @throws IOException If an I/O error occurs during serialization.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeInt(currentTime);
        oos.writeInt(remainingTime);
    }
}
