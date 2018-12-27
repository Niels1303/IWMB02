package iwmb02.com.iwmb02.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JSONGameResponse {
        @SerializedName("results")
        @Expose
        private GameResponse[] results;

        public GameResponse[] getResults() {
            return results;
        }

        public void setResults(GameResponse[] results) {
            this.results = results;
        }
}
