package iwmb02.com.iwmb02.services;
import iwmb02.com.iwmb02.models.Backendless;
import retrofit2.Call;
import retrofit2.http.*;

public interface RestApiClient {
    @Headers("Content-Type: application/json")
    @POST("users/register")
    Call<Backendless> createUser(@Body Backendless user);
}
