package iwmb02.com.iwmb02.services;
import iwmb02.com.iwmb02.models.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface RestApiClient {

    @Headers({
            "X-Parse-Application-Id: pGAKUNimtJjDaR4rgXvUPyuhWLYDmbBSLsVHIu9T",
            "X-Parse-REST-API-Key: TMuft7MLYvlz8uNY7c8DIno2yiQXRQj1LgNtlzOb",
            "X-Parse-Revocable-Session: 1",
            "Content-Type: application/json"
    })
    @POST("/users")
    Call<User> createUser(@Body User user);

    // getGames wird aufgerufen um in der ProfileActivity anzeigen zu können welche Spiele ein User besitzt
    @Headers({
            "X-Parse-Application-Id: pGAKUNimtJjDaR4rgXvUPyuhWLYDmbBSLsVHIu9T",
            "X-Parse-REST-API-Key: TMuft7MLYvlz8uNY7c8DIno2yiQXRQj1LgNtlzOb"
    })
    @GET("/classes/joinUserBrettspiel")
    Call<JSONGameResponse> getGames(@QueryMap(encoded=true) Map<String, String> options);

    // "@Field" entspricht dem "key" und das dahinter stehende Object beinhaltet den Wert
    @Headers({
            "X-Parse-Application-Id: pGAKUNimtJjDaR4rgXvUPyuhWLYDmbBSLsVHIu9T",
            "X-Parse-REST-API-Key: TMuft7MLYvlz8uNY7c8DIno2yiQXRQj1LgNtlzOb",
            "X-Parse-Revocable-Session: 1"
    })
    @FormUrlEncoded
    @POST("/login")
    Call<User> loginUser(@Field("username") String username, @Field("password") String password);

    //Mit sendMessage werden Chatnachrichten in der DB gespeichert
    @Headers({
            "X-Parse-Application-Id: pGAKUNimtJjDaR4rgXvUPyuhWLYDmbBSLsVHIu9T",
            "X-Parse-REST-API-Key: TMuft7MLYvlz8uNY7c8DIno2yiQXRQj1LgNtlzOb",
            "Content-Type: application/json"
    })
    @POST("/classes/Nachricht")
    Call<Nachricht> sendMessage(@Body Nachricht msg);

    //Bei einer Anfrage ohne Parameter werden alle Objekte der entsprechenden Klasse zurückgegeben. Zukünftig sollte eine Limiter eingebaut werden, damit nicht alle Objekte jedes Mal abgerufen werden müssen.
    @Headers({
            "X-Parse-Application-Id: pGAKUNimtJjDaR4rgXvUPyuhWLYDmbBSLsVHIu9T",
            "X-Parse-REST-API-Key: TMuft7MLYvlz8uNY7c8DIno2yiQXRQj1LgNtlzOb"
    })
    @GET("/classes/Nachricht")
    Call<JSONNachrichtResponse> refreshMessages();

    //Mit getTeilnehmer werden alle Spieltermine inklusive aller Teilnehmer abgerufen
    @Headers({
            "X-Parse-Application-Id: pGAKUNimtJjDaR4rgXvUPyuhWLYDmbBSLsVHIu9T",
            "X-Parse-REST-API-Key: TMuft7MLYvlz8uNY7c8DIno2yiQXRQj1LgNtlzOb"
    })
    @GET("/classes/Teilnehmer")
    Call<JSONTeilnehmerResponse> getTeilnehmer(@QueryMap(encoded=true) Map<String, String> options);

    //Mit diesem Query wird überprüpft ob der eingelogte User für ein bestimmtes Spielevent bereits seine Bewertungen abgegeben hat.
    @Headers({
            "X-Parse-Application-Id: pGAKUNimtJjDaR4rgXvUPyuhWLYDmbBSLsVHIu9T",
            "X-Parse-REST-API-Key: TMuft7MLYvlz8uNY7c8DIno2yiQXRQj1LgNtlzOb"
    })
    @GET("/classes/Bewertung")
    Call<JSONBewertungResponse> checkBewertung(@QueryMap(encoded=true) Map<String, String> options);

    //Hier wird eine neue Bewertung eingetragen
    @Headers({
            "X-Parse-Application-Id: pGAKUNimtJjDaR4rgXvUPyuhWLYDmbBSLsVHIu9T",
            "X-Parse-REST-API-Key: TMuft7MLYvlz8uNY7c8DIno2yiQXRQj1LgNtlzOb",
            "Content-Type: application/json"
    })
    @POST("/classes/Bewertung")
    Call<JSONBewertungResponse> setBewertung(@Body Bewertung bewertung);


}
