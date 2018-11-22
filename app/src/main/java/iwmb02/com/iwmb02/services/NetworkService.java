package iwmb02.com.iwmb02.services;
import com.google.gson.*;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.lang.reflect.Type;
import java.util.Date;

public class NetworkService {
    private static NetworkService instance;
    private static final String BASE_URL = "https://api.backendless.com/3FA964C2-D3DC-1C27-FF09-C5B55C266C00/99F1C48F-F038-E26F-FFA5-3B5849089200/";
    private Retrofit retrofit;

    private NetworkService() {

        //Backendless speichert ein Datum in Form eines Unix Timestamps. Standardmäßig weiß Gson jedoch nichts damit anzufangen.
        //Deswegen muss ein custom deserializer implementiert werden und an die GsonConverterFactory übergeben werden.
        final Gson builder = new GsonBuilder()
                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
                        return new Date(jsonElement.getAsJsonPrimitive().getAsLong());
                    }
                })
                .create();



        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(builder))
                .build();
    }


    public static NetworkService getInstance() {
        if (instance == null) {
            instance = new NetworkService();
        }
        return instance;
    }

    public RestApiClient getRestApiClient() {
        return retrofit.create(RestApiClient.class);
    }
}
