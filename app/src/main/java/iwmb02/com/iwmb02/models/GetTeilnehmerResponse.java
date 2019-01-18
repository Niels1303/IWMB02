package iwmb02.com.iwmb02.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetTeilnehmerResponse {
    @SerializedName("results")
    @Expose
    private Teilnehmer[] results;

    public Teilnehmer[] getResults() {
        return results;
    }

    public void setResults(Teilnehmer[] results) {
        this.results = results;
    }


}
