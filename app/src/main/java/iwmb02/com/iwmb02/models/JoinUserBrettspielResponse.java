package iwmb02.com.iwmb02.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JoinUserBrettspielResponse {
        @SerializedName("results")
        @Expose
        private JoinUserBrettspiel[] results;

        public JoinUserBrettspiel[] getResults() {
            return results;
        }

        public void setResults(JoinUserBrettspiel[] results) {
            this.results = results;
        }
}
