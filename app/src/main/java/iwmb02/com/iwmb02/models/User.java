/**
 * Mit dieser Klasse werden Objekte für den Datenaustauch mit der REST Api Schnittstelle initialisiert.
 * Die Attribut Namen nach "@SerializedName" müssen mit den Json Attributen 1:1 übereinstimmen.
 */

package iwmb02.com.iwmb02.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class User {

    @SerializedName("objectId")
    @Expose
    private String objectId;
    @SerializedName("ausrichterCounter")
    @Expose
    private Integer ausrichterCounter;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("nachname")
    @Expose
    private String nachname;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("vorname")
    @Expose
    private String vorname;
    @SerializedName("createdAt")
    @Expose
    private Date createdAt;
    @SerializedName("updatedAt")
    @Expose
    private Date updatedAt;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("sessionToken")
    @Expose
    private String sessionToken;
    @SerializedName("__type")
    @Expose
    private String type;
    @SerializedName("className")
    @Expose
    private String className;


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Integer getAusrichterCounter() {
        return ausrichterCounter;
    }

    public void setAusrichterCounter(Integer ausrichterCounter) {
        this.ausrichterCounter = ausrichterCounter;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void appendVorname(String vorname) {
        this.vorname = this.vorname + vorname;
    }
}
