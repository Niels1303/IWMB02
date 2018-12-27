package iwmb02.com.iwmb02.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JSONBewertungResponse {
    @SerializedName("results")
    @Expose
    private Bewertung[] results;

    public Bewertung[] getResults() {
        return results;
    }

    public void setResults(Bewertung[] results) {
        this.results = results;
    }
}
