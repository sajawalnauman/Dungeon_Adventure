package AdventureModel;
import java.io.Serializable;
import java.util.*;

/** Leaderboard
 * __________________________
 * This class creates and keeps track of the Leaderboard object in the game.
 * It also holds the top 3 current best times set by the players.
 */

public class Leaderboard implements Serializable {

    public static HashMap<String, Integer> bestTimes;

    /** Leaderboard
     * __________________________
     * Initialize the leaderboard.
     */
    public Leaderboard() {
        bestTimes = new HashMap<>();
    }

    /** addTime
     * __________________________
     * Add the current Player's name and time into the bestTimes Hashmap to be shown on the leaderboard.
     * Only add up to 3 best times.
     */
    public void addTime(String name, Integer time) {

        if (bestTimes.size() < 3) {
            bestTimes.put(name, time);
        }
        else {
            if (bestTimes.containsKey(name)) {
                if (bestTimes.get(name) > time) {
                    bestTimes.replace(name, time);
                }
            }
            else {
                int highest = -1;
                String highKey = "";

                for (String key : bestTimes.keySet()) {
                    if (bestTimes.get(key) > highest) {
                        highest = bestTimes.get(key);
                        highKey = key;
                    }
                }

                if (highest > time){
                    bestTimes.remove(highKey);
                    bestTimes.put(name, time);
                }
            }
        }
    }
}
