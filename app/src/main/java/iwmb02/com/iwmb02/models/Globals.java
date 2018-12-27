//Diese Klasse dient dazu Werte die aus mehreren Activities erreichbar werden müssen global zu speichern.
//Diese Werte sind verfügbar solange die App nicht zerstört wird.
//Diese Klasse muss im Android Manifest deklariert werden.
package iwmb02.com.iwmb02.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Globals {

    private static Globals instance = new Globals();

    public static Globals getInstance() {
        return instance;
    }

    public static void setInstance(Globals instance) {
        Globals.instance = instance;
    }

    private boolean isLoggedIn;
    private String userId;
    private String username;
    private String sessionToken;
    private Date createdAt;
    private TeilnehmerResponse[] teilnehmerResponses;

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public TeilnehmerResponse[] getTeilnehmerResponses() {
        return teilnehmerResponses;
    }

    public void setTeilnehmerResponses(TeilnehmerResponse[] teilnehmerResponses) {
        this.teilnehmerResponses = teilnehmerResponses;
    }
}
