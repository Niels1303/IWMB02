package iwmb02.com.iwmb02.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetEssensrichtungResponse {
    @SerializedName("results")
    @Expose
    private Essensrichtung[] results;

    public Essensrichtung[] getResults() {
        return results;
    }

    public void setResults(Essensrichtung[] results) {
        this.results = results;
    }
}
