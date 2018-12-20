package iwmb02.com.iwmb02.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class joinUserBrettspiel {

    @SerializedName("objectId")
    @Expose
    private String objectId;
    @SerializedName("brettspiel")
    @Expose
    private Brettspiel brettspiel;


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Brettspiel getBrettspiel() {
        return brettspiel;
    }

    public void setBrettspiel(Brettspiel brettspiel) {
        this.brettspiel = brettspiel;
    }
}
