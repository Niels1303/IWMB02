package iwmb02.com.iwmb02.services;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService mInstance;
    private static final String BASE_URL = "https://api.backendless.com/3FA964C2-D3DC-1C27-FF09-C5B55C266C00/99F1C48F-F038-E26F-FFA5-3B5849089200/";
    private Retrofit mRetrofit;

    private NetworkService() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public static NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    public RestApiClient getRestApiClient() {
        return mRetrofit.create(RestApiClient.class);
    }
}
