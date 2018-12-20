package iwmb02.com.iwmb02.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JSONGameResponse {
    public class JSONNachrichtResponse {
        @SerializedName("results")
        @Expose
        private joinUserBrettspiel[] results;

        public joinUserBrettspiel[] getResults() {
            return results;
        }

        public void setResults(joinUserBrettspiel[] results) {
            this.results = results;
        }

    }
}
