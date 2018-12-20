package iwmb02.com.iwmb02.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Brettspiel {

    @SerializedName("objectId")
    @Expose
    private String objectId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("playersNumber")
    @Expose
    private String playersNumber;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlayersNumber() {
        return playersNumber;
    }

    public void setPlayersNumber(String playersNumber) {
        this.playersNumber = playersNumber;
    }
}
