package iwmb02.com.iwmb02.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JSONTeilnehmerResponse {
    @SerializedName("results")
    @Expose
    private TeilnehmerResponse[] results;

    public TeilnehmerResponse[] getResults() {
        return results;
    }

    public void setResults(TeilnehmerResponse[] results) {
        this.results = results;
    }


}
