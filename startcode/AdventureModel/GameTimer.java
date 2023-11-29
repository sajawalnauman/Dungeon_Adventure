package AdventureModel;

import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameTimer implements Serializable {
    private transient ScheduledExecutorService scheduler;
    public Integer currentTime;
    public Integer remainingTime;
    public Integer endingTime;
    public int initialTime = 92;
    private boolean isRunning;

    public GameTimer() {
        this.currentTime = 0;
        this.remainingTime = initialTime;
        this.endingTime = 0;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void startTimer() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::updateCurrentTime, 0, 1, TimeUnit.SECONDS);
    }

    public void stopTimer() {
        scheduler.shutdown();
    }

    public boolean isRunning() {
        isRunning = !scheduler.isShutdown();
        return isRunning;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public Integer getRemainingTime() {
        return remainingTime;
    }

    public int getEndingTime() {
        endingTime = currentTime;
        return endingTime;
    }

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

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        this.scheduler = Executors.newScheduledThreadPool(1);
    }
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeInt(currentTime);
        oos.writeInt(remainingTime);
    }
}
