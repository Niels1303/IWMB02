package iwmb02.com.iwmb02.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeilnehmerResponse {

    @SerializedName("objectId")
    @Expose
    private String objectId;
    @SerializedName("spielterminId")
    @Expose
    private Spieltermin spielterminId;
    @SerializedName("userId")
    @Expose
    private User userId;
    @SerializedName("ausrichter")
    @Expose
    private boolean ausrichter;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Spieltermin getSpielterminId() {
        return spielterminId;
    }

    public void setSpielterminId(Spieltermin spielterminId) {
        this.spielterminId = spielterminId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public boolean isAusrichter() {
        return ausrichter;
    }

    public void setAusrichter(boolean ausrichter) {
        this.ausrichter = ausrichter;
    }
}
