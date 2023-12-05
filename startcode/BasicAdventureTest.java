
import java.io.IOException;
import java.util.Collections;

import AdventureModel.AdventureGame;
import AdventureModel.Leaderboard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BasicAdventureTest {
    @Test
    void getCommandsTest() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        String commands = game.player.getCurrentRoom().getCommands();
        assertEquals("WEST, UP, NORTH, IN, SOUTH, DOWN", commands);
    }

    @Test
    void getObjectString() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        String objects = game.player.getCurrentRoom().getObjectString();
        assertEquals("a water bird", objects);
    }

    @Test
    void gameTimerTest() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        assertTrue(game.gameTimer.isRunning());
        game.gameTimer.stopTimer();
        assertFalse(game.gameTimer.isRunning());
    }

    @Test
    void timerStateTest() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        assertEquals("ResumeTimerState", game.timerState.description());
        game.changeState();
        assertEquals("PauseTimerState", game.timerState.description());
        game.changeState();
        assertEquals("ResumeTimerState", game.timerState.description());
    }

    @Test
    void leaderboardTest() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        assertTrue(Leaderboard.bestTimes.size() <= 3);
        game.leaderboard.addTime("saj", 11);
        game.leaderboard.addTime("lucy", 12);
        game.leaderboard.addTime("jacob", 13);
        assertTrue(Leaderboard.bestTimes.size() == 3);
        game.leaderboard.addTime("aabbcc", 14);
        assertTrue(Leaderboard.bestTimes.size() == 3);
    }

}
