package iwmb02.com.iwmb02.services;
import com.google.gson.*;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.lang.reflect.Type;
import java.util.Date;

public class NetworkService {
    private static NetworkService instance;
    private static final String BASE_URL = "https://parseapi.back4app.com";
    private Retrofit retrofit;

    private NetworkService() {

        //User speichert ein Datum in Form eines Unix Timestamps. Standardmäßig weiß Gson jedoch nichts damit anzufangen.
        //Deswegen muss ein custom deserializer implementiert werden und an die GsonConverterFactory übergeben werden.
 /*       final Gson builder = new GsonBuilder()
                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
                        return new Date(jsonElement.getAsJsonPrimitive().getAsLong());
                    }
                })
                .create();*/

        retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
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
