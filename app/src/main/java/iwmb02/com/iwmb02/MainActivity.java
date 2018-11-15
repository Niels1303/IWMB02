package iwmb02.com.iwmb02;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import iwmb02.com.iwmb02.models.Backendless;
import iwmb02.com.iwmb02.services.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Backendless user = new Backendless();
        user.setEmail("niels@iubh.de");
        user.setPassword("test");
        user.setName("Niels");

        NetworkService.getInstance()
                .getRestApiClient()
                .createUser(user)
                .enqueue(new Callback<Backendless>() {
                    @Override
                    public void onResponse(Call<Backendless> call, Response<Backendless> response) {

                    }

                    @Override
                    public void onFailure(Call<Backendless> call, Throwable t) {

                    }
                });

    }
}
