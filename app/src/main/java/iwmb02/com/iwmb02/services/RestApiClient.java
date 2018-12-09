package iwmb02.com.iwmb02.services;
import iwmb02.com.iwmb02.models.User;
import iwmb02.com.iwmb02.models.Chat;
import retrofit2.Call;
import retrofit2.http.*;

public interface RestApiClient {

    @Headers({
            "X-Parse-Application-Id: pGAKUNimtJjDaR4rgXvUPyuhWLYDmbBSLsVHIu9T",
            "X-Parse-REST-API-Key: TMuft7MLYvlz8uNY7c8DIno2yiQXRQj1LgNtlzOb",
            "X-Parse-Revocable-Session: 1",
            "Content-Type: application/json"
    })
    @POST("/users")
    Call<User> createUser(@Body User user);

    @Headers({
            "X-Parse-Application-Id: pGAKUNimtJjDaR4rgXvUPyuhWLYDmbBSLsVHIu9T",
            "X-Parse-REST-API-Key: TMuft7MLYvlz8uNY7c8DIno2yiQXRQj1LgNtlzOb",
            "X-Parse-Revocable-Session: 1"
    })
    @FormUrlEncoded
    @POST("/login")
    Call<User> loginUser(@Field("username") String username, @Field("password") String password);

    //Abonnieren eines Channels
    @Headers("Content-Type: application/json")
    @POST("messaging/default/subscribe")
    Call<Chat> subscribeChannel(@Body Chat chat);

    @Headers("Content-Type: application/json")
    @POST("messaging/default")
    Call<Chat> publishMsg(@Body Chat chat);
}
