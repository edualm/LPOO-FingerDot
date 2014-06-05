package pt.up.fe.lpoo.fingerdot.logic.common;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import pt.up.fe.lpoo.fingerdot.logic.singleplayer.LeaderboardEntry;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MegaEduX on 03/06/14.
 */

public class UserManager {
    private class LoginResponse {
        public boolean success;
        public HashMap<String, String> info;
    }

    private static final String kBaseApiURL = "https://edr.io/fingerdot/v1/users/";
    private static final String kLoginURL = kBaseApiURL + "login.php";

    private static final String kLocalUserPath = "user.fdu";

    private static UserManager _sharedInstance = null;

    private User _user = null;

    public static UserManager sharedManager() {
        if (_sharedInstance == null)
            _sharedInstance = new UserManager();

        return _sharedInstance;
    }

    public UserManager() {
        try {
            loadUserFromDisk();
        } catch (Exception exc) {

        }
    }

    public User getUser() {
        return _user;
    }

    /*
     *  Code taken from http://stackoverflow.com/questions/6825226/trust-anchor-not-found-for-android-ssl-connection
     */

    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    private static void trustAllHosts() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        } };

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean login(User details) {
        try {
            URL loginURL = new URL(kLoginURL + "?username=" + details.getUsername() + "&pin=" + details.getPin());

            if (Gdx.app.getType() == Application.ApplicationType.Android)
                trustAllHosts();

            HttpsURLConnection connection = (HttpsURLConnection) loginURL.openConnection();

            if (Gdx.app.getType() == Application.ApplicationType.Android)
                connection.setHostnameVerifier(DO_NOT_VERIFY);

            System.out.println(loginURL);

            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();

                String line;

                while ((line = rd.readLine()) != null)
                    sb.append(line);

                System.out.println(sb);

                Gson gs = new Gson();

                Type responseType = new TypeToken<LoginResponse>(){}.getType();

                LoginResponse response = gs.fromJson(sb.toString(), responseType);

                if (response.success) {
                    _user = new User(response.info.get("username"), response.info.get("pin"));

                    saveUserToDisk();

                    return true;
                }

                return false;
            } catch (Exception e) {
                return false;
            } finally {
                connection.disconnect();
            }
        } catch (Exception e) {
            return false;
        }
    }

    private void loadUserFromDisk() throws Exception {
        FileHandle file = Gdx.files.local(kLocalUserPath);

        ObjectInputStream ois = new ObjectInputStream(file.read());

        _user = (User) ois.readObject();

        System.out.println("Loaded user " + _user.getUsername() + " with pin " + _user.getPin() + " from disk.");

        ois.close();
    }

    private void saveUserToDisk() throws Exception {
        FileHandle file = Gdx.files.local(kLocalUserPath);

        ObjectOutputStream oos = new ObjectOutputStream(file.write(false));

        oos.writeObject(_user);
    }
}
