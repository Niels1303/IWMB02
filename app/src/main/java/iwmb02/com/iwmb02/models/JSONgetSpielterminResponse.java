package iwmb02.com.iwmb02.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JSONgetSpielterminResponse {
    @SerializedName("results")
    @Expose
    private Spieltermin[] results;
    @SerializedName("objectId")
    @Expose
    private String objectId; //Wird für Antworten des Backends verwendet

    public Spieltermin[] getResults() {
        return results;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
