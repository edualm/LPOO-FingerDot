//
//  FingerDot
//
//  Created by Eduardo Almeida and Joao Almeida
//  LPOO 13/14
//

package pt.up.fe.lpoo.fingerdot.logic.singleplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class LeaderboardManager {
    private static final String kBaseApiURL = "https://edr.io/fingerdot/v1/leaderboard/";
    private static final String kLeaderboardURL = kBaseApiURL + "get_leaderboard.php";
    private static final String kPostScoreURL = kBaseApiURL + "add_score.php";

    private static final String kLocalLeaderboardPath = "local_highscores.fdh";

    private ArrayList<LeaderboardEntry> _onlineLeaderboard = null;
    private ArrayList<LeaderboardEntry> _localLeaderboard = null;

    private LeaderboardEntryComparator lec = new LeaderboardEntryComparator();

    private static LeaderboardManager _sharedInstance = null;

    /**
     * Returns the shared instance of the singleton.
     *
     * @return The object's shared instance.
     */

    public static LeaderboardManager sharedManager() {
        if (_sharedInstance == null)
            _sharedInstance = new LeaderboardManager();

        return _sharedInstance;
    }

    private LeaderboardManager() {
        try {
            loadLocalLeaderboard();
        } catch (Exception e) {

        }
    }

    /**
     * Adds an entry to the local leaderboard.
     *
     * @param entry A leaderboard entry.
     */

    public void addLocalScore(LeaderboardEntry entry) {
        _localLeaderboard.add(entry);

        Collections.sort(_localLeaderboard, lec);

        saveLocalLeaderboard();
    }

    /**
     * Loads the local leaderboard.
     *
     * @return true on success, false otherwise.
     */

    public boolean loadLocalLeaderboard() {
        FileHandle file = Gdx.files.local(kLocalLeaderboardPath);

        if (file.exists()) {
            try {
                ObjectInputStream ois = new ObjectInputStream(file.read());

                _localLeaderboard = (ArrayList<LeaderboardEntry>) ois.readObject();

                ois.close();

                return true;
            } catch (Exception e) {

            }
        }

        _localLeaderboard = new ArrayList<LeaderboardEntry>();

        return false;
    }

    /**
     * Saves the local leaderboard.
     *
     * @return true on success, false otherwise.
     */

    public boolean saveLocalLeaderboard() {
        FileHandle file = Gdx.files.local(kLocalLeaderboardPath);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(file.write(false));

            oos.writeObject(_localLeaderboard);

            return true;
        } catch (Exception e) {
            System.out.println("saveLocalLeaderboard: " + e);
        }

        return false;
    }

    /**
     * Retrieves the online leaderboard.
     *
     * @return true on success, false otherwise.
     */

    public boolean retrieveOnlineLeaderboard() {
        try {
            URL lbURL = new URL(kLeaderboardURL);

            HttpsURLConnection connection = (HttpsURLConnection) lbURL.openConnection();

            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();

                String line;

                while ((line = rd.readLine()) != null)
                    sb.append(line);

                Gson gs = new Gson();

                Type leaderboardType = new TypeToken<ArrayList<LeaderboardEntry>>(){}.getType();

                _onlineLeaderboard = gs.fromJson(sb.toString(), leaderboardType);
            } catch (Exception e) {
                return false;
            } finally {
                connection.disconnect();
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * Getter for the online leaderboard.
     *
     * @return The online leaderboard.
     */

    public ArrayList<LeaderboardEntry> getOnlineLeaderboard() {
        return _onlineLeaderboard;
    }

    /**
     * Getter for the local leaderboard.
     *
     * @return The local leaderboard.
     */

    public ArrayList<LeaderboardEntry> getLocalLeaderboard() {
        return _localLeaderboard;
    }

    /**
     * Publishes a score to the online leaderboard.
     *
     * @param entry The leaderboard entry.
     * @return true on success, false otherwise.
     */

    public static boolean publishScoreOnOnlineLeaderboard(LeaderboardEntry entry) {
        try {
            URL lbURL = new URL(kPostScoreURL + "?username=" + entry.username + "&score=" + entry.score + "&version=" + entry.version);

            HttpsURLConnection connection = (HttpsURLConnection) lbURL.openConnection();

            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();

                String line;

                while ((line = rd.readLine()) != null)
                    sb.append(line);

                Gson gs = new Gson();

                HashMap<String, Boolean> response = gs.fromJson(sb.toString(), (new HashMap<String, Boolean>()).getClass());

                return response.get("success");
            } catch (Exception e) {
                return false;
            } finally {
                connection.disconnect();
            }
        } catch (Exception e) {
            return false;
        }
    }
}
