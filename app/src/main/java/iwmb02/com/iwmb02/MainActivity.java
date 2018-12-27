package iwmb02.com.iwmb02;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import iwmb02.com.iwmb02.models.Globals;
import iwmb02.com.iwmb02.models.JSONTeilnehmerResponse;
import iwmb02.com.iwmb02.services.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.widget.Toolbar mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new EventsFragment()).commit();

        //Alle vorhandene Spieltermine inkl. Teilnehmer werden abgefragen und für spätere Anfragen in globale Variabel gespeichert
        Map<String, String> data = new HashMap<>();
        data.put("include", "spielterminId,userId");
        NetworkService.getInstance()
                .getRestApiClient()
                .getTeilnehmer(data)
                .enqueue(new Callback<JSONTeilnehmerResponse>() {
                    @Override
                    public void onResponse(Call<JSONTeilnehmerResponse> call, Response<JSONTeilnehmerResponse> response) {
                        if (response.isSuccessful()) {
                            JSONTeilnehmerResponse resp = response.body();
                            Globals.getInstance().setTeilnehmerResponses(resp.getResults());
                        } else {
                            Toast.makeText(MainActivity.this, "Error: something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JSONTeilnehmerResponse> call, Throwable t) {
                        Toast.makeText(MainActivity.this,"Error: " + t.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Laden des Custom Menu (custom_menu.xml).
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.custom_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            // Beim Drücken auf "logout" wird global.isLoggedIn auf "false" gesetzt. Dadurch muss sich der Anwender wieder einlogen.
            Globals global = Globals.getInstance();
            global.setLoggedIn(false);
            //... und der Benutzer zur Login Activity weitergeleitet.
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.action_profile) {
            Intent intent = new Intent(MainActivity.this, Profile.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.nav_date:
                    selectedFragment = new EventsFragment();
                    break;
                case R.id.nav_game:
                    selectedFragment = new GamesFragment();
                    break;
                case R.id.nav_food:
                    selectedFragment = new FoodFragment();
                    break;
                case R.id.nav_rating:
                    selectedFragment = new RatingsFragment();
                    break;
                case R.id.nav_chat:
                    selectedFragment = new ChatFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();
            return true;
        }
    };



}
