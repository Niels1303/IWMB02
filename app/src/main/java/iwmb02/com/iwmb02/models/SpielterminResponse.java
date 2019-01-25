package iwmb02.com.iwmb02.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SpielterminResponse {
    @SerializedName("results")
    @Expose
    private List<Spieltermin> results = null;

    public List<Spieltermin> getResults() {
        return results;
    };

    public void setResults(List<Spieltermin> results) {
        this.results = results;
    };
}

