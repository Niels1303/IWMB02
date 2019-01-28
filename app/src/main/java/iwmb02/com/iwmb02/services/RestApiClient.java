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
    Call<JoinUserBrettspielResponse> getGames(@QueryMap(encoded=true) Map<String, String> options);

    @Headers({
            "X-Parse-Application-Id: pGAKUNimtJjDaR4rgXvUPyuhWLYDmbBSLsVHIu9T",
            "X-Parse-REST-API-Key: TMuft7MLYvlz8uNY7c8DIno2yiQXRQj1LgNtlzOb",
            "Content-Type: application/json"
    })
    @POST("/classes/joinUserBrettspiel")
    Call<JoinUserBrettspiel> createUserBrettspiel(@Body JoinUserBrettspiel joinUserBrettspiel);

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
    Call<NachrichtResponse> refreshMessages();

    //Mit getTeilnehmer werden alle Spieltermine inklusive aller Teilnehmer abgerufen
    @Headers({
            "X-Parse-Application-Id: pGAKUNimtJjDaR4rgXvUPyuhWLYDmbBSLsVHIu9T",
            "X-Parse-REST-API-Key: TMuft7MLYvlz8uNY7c8DIno2yiQXRQj1LgNtlzOb"
    })
    @GET("/classes/Teilnehmer")
    Call<TeilnehmerResponse> getTeilnehmer(@QueryMap(encoded=true) Map<String, String> options);

    //Mit diesem Query wird überprüpft ob der eingelogte User für ein bestimmtes Spielevent bereits seine Bewertungen abgegeben hat.
    @Headers({
            "X-Parse-Application-Id: pGAKUNimtJjDaR4rgXvUPyuhWLYDmbBSLsVHIu9T",
            "X-Parse-REST-API-Key: TMuft7MLYvlz8uNY7c8DIno2yiQXRQj1LgNtlzOb"
    })
    @GET("/classes/Bewertung")
    Call<BewertungResponse> checkBewertung(@QueryMap(encoded=true) Map<String, String> options);

    //Hier wird eine neue Bewertung eingetragen
    @Headers({
            "X-Parse-Application-Id: pGAKUNimtJjDaR4rgXvUPyuhWLYDmbBSLsVHIu9T",
            "X-Parse-REST-API-Key: TMuft7MLYvlz8uNY7c8DIno2yiQXRQj1LgNtlzOb",
            "Content-Type: application/json"
    })
    @POST("/classes/Bewertung")
    Call<BewertungResponse> setBewertung(@Body Bewertung bewertung);

    //Abruf aller vorhandenen Bewertungen. Wird für das "Rating" Fragment benötigt um die Bewertungsergebnisse anzuzeigen.
    @Headers({
            "X-Parse-Application-Id: pGAKUNimtJjDaR4rgXvUPyuhWLYDmbBSLsVHIu9T",
            "X-Parse-REST-API-Key: TMuft7MLYvlz8uNY7c8DIno2yiQXRQj1LgNtlzOb",
    })
    @GET("/classes/Bewertung")
    Call<BewertungResponse> getBewertung();

    //Abruf aller vorhandenen Brettspiele. Wird für das "Games" Fragment benötigt.
    @Headers({
            "X-Parse-Application-Id: pGAKUNimtJjDaR4rgXvUPyuhWLYDmbBSLsVHIu9T",
            "X-Parse-REST-API-Key: TMuft7MLYvlz8uNY7c8DIno2yiQXRQj1LgNtlzOb",
    })
    @GET("/classes/Brettspiel")
    Call<BrettspielResponse> getBrettspiel();

    @Headers({
            "X-Parse-Application-Id: pGAKUNimtJjDaR4rgXvUPyuhWLYDmbBSLsVHIu9T",
            "X-Parse-REST-API-Key: TMuft7MLYvlz8uNY7c8DIno2yiQXRQj1LgNtlzOb",
            "Content-Type: application/json"
    })
    @POST("/classes/Brettspiel")
    Call<Brettspiel> createBrettspiel(@Body Brettspiel brettspiel);

    //Abruf aller vorhandenen Spieltermine. Wird für das Navigieren zwischen den Terminen in den Fragmenten benötigt.
    @Headers({
            "X-Parse-Application-Id: pGAKUNimtJjDaR4rgXvUPyuhWLYDmbBSLsVHIu9T",
            "X-Parse-REST-API-Key: TMuft7MLYvlz8uNY7c8DIno2yiQXRQj1LgNtlzOb",
    })
    @GET("/classes/Spieltermin")
    Call<JSONgetSpielterminResponse> getSpieltermin();

    //Fügt ein neues Spieltermin hinzu. Wird von der Activity "AddEvent" verwendet.
    @Headers({
            "X-Parse-Application-Id: pGAKUNimtJjDaR4rgXvUPyuhWLYDmbBSLsVHIu9T",
            "X-Parse-REST-API-Key: TMuft7MLYvlz8uNY7c8DIno2yiQXRQj1LgNtlzOb",
            "Content-Type: application/json"
    })
    @POST("/classes/Spieltermin")
    Call<JSONgetSpielterminResponse> createSpieltermin(@Body Spieltermin spieltermin);

    //Fügt einen neuen Teilnehmer hinzu. Wir von der AddEvent Activity benötigt.
    @Headers({
            "X-Parse-Application-Id: pGAKUNimtJjDaR4rgXvUPyuhWLYDmbBSLsVHIu9T",
            "X-Parse-REST-API-Key: TMuft7MLYvlz8uNY7c8DIno2yiQXRQj1LgNtlzOb",
            "Content-Type: application/json"
    })
    @POST("/classes/Teilnehmer")
    Call<TeilnehmerResponse> createTeilnehmer(@Body Teilnehmer teilnehmer);

    //Änderung der Attribute eines Teilnehmers. Wird vom Fragment "Games" verwendet um die Auswahl eines Brettspiels zu speichern
    @Headers({
            "X-Parse-Application-Id: pGAKUNimtJjDaR4rgXvUPyuhWLYDmbBSLsVHIu9T",
            "X-Parse-REST-API-Key: TMuft7MLYvlz8uNY7c8DIno2yiQXRQj1LgNtlzOb",
            "Content-Type: application/json"
    })
    @PUT("/classes/Teilnehmer/{id}")
    Call<Teilnehmer> putTeilnehmer(@Path("id") String id, @Body Teilnehmer teilnehmer);

    //Abruf aller vorhandenen Essensrichtungen. Wird für das "Food" Fragment benötigt.
    @Headers({
            "X-Parse-Application-Id: pGAKUNimtJjDaR4rgXvUPyuhWLYDmbBSLsVHIu9T",
            "X-Parse-REST-API-Key: TMuft7MLYvlz8uNY7c8DIno2yiQXRQj1LgNtlzOb",
    })
    @GET("/classes/Essensrichtung")
    Call<EssensrichtungResponse> getEssensrichtung();

    @Headers({
            "X-Parse-Application-Id: pGAKUNimtJjDaR4rgXvUPyuhWLYDmbBSLsVHIu9T",
            "X-Parse-REST-API-Key: TMuft7MLYvlz8uNY7c8DIno2yiQXRQj1LgNtlzOb",
            "Content-Type: application/json"
    })
    @PUT("/classes/Spieltermin/{id}")
    Call<Spieltermin> putSpieltermin(@Path("id") String id, @Body Spieltermin spieltermin);

}
