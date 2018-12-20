//Diese Klasse ist nur notwendig, weil das Backend mehrzählige Objekte nicht als einfaches sondern als "nested array" zurücksendet.
package iwmb02.com.iwmb02.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JSONNachrichtResponse {
    @SerializedName("results")
    @Expose
    private Nachricht[] results;

    public Nachricht[] getResults() {
        return results;
    }

    public void setResults(Nachricht[] results) {
        this.results = results;
    }

}
