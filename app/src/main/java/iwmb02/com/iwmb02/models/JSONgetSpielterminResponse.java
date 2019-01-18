package iwmb02.com.iwmb02.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JSONgetSpielterminResponse {
    @SerializedName("results")
    @Expose
    private Spieltermin[] results;

    public Spieltermin[] getResults() {
        return results;
    }
}
