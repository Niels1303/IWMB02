package iwmb02.com.iwmb02.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class Spieltermin implements Comparable<Spieltermin> {


    @SerializedName("objectId")
    @Expose
    private String objectId;
    @SerializedName("EventDate")
    @Expose
    private EventDate eventDate;
    @SerializedName("createdAt")
    @Expose
    private Date createdAt;
    @SerializedName("updatedAt")
    @Expose
    private Date updatedAt;
    @SerializedName("__type")
    @Expose
    private String type;
    @SerializedName("className")
    @Expose
    private String className;
    @SerializedName("Food_Supplier")
    @Expose
    private String foodSupplier;
    @SerializedName("game")
    @Expose
    private String game;
    @SerializedName("ausrichter")
    @Expose
    private User ausrichter;

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


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public EventDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(EventDate eventDate) {
        this.eventDate = eventDate;
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

    public String getFoodSupplier() {
        return foodSupplier;
    }

    public void setFoodSupplier(String foodSupplier) {
        this.foodSupplier = foodSupplier;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public User getAusrichter() {
        return ausrichter;
    }

    public void setAusrichter(User ausrichter) {
        this.ausrichter = ausrichter;
    }

    //Methode um die Spieltermine chronologisch nach Eventdatum zu sortieren
    @Override
    public int compareTo(Spieltermin spieltermin) {
        return this.getEventDate().getIso().compareTo(spieltermin.getEventDate().getIso());
    }
}
