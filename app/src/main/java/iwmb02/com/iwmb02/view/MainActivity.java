package iwmb02.com.iwmb02.view;

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
import iwmb02.com.iwmb02.R;
import iwmb02.com.iwmb02.models.*;
import iwmb02.com.iwmb02.services.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.*;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.widget.Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new EventsFragment()).commit();

        getSpieltermine(); //Liste aller Spieltermine wird abgerufen, sortiert und als globale Variabel für die Fragments gespeichert


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
            // Die Session wird in der DB gelöscht
            NetworkService.getInstance()
                    .getRestApiClient()
                    .logout(Globals.getInstance().getSessionToken())
                    .enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(MainActivity.this,"Logout successful",Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(MainActivity.this, "Error: something went wrong!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT ).show();
                        }
                    });
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

    //Alle vorhandene Spieltermine werden aus der DB abgerufen und als globale Variabel gespeichert um die Navigation durch die Spieltermine in den Fragmenten anzeigen zu können.
    private void getSpieltermine() {
        final ArrayList<Spieltermin> list = new ArrayList<Spieltermin>();
        NetworkService.getInstance()
                .getRestApiClient()
                .getSpieltermin()
                .enqueue(new Callback<JSONgetSpielterminResponse>() {
                    @Override
                    public void onResponse(Call<JSONgetSpielterminResponse> call, Response<JSONgetSpielterminResponse> response) {
                        if(response.isSuccessful()) {
                            JSONgetSpielterminResponse resp = response.body();
                            Spieltermin[] spieltermine = resp.getResults();
                            for (int i = 0; i < spieltermine.length; i++) {
                                list.add(spieltermine[i]);
                            }
                            Collections.sort(list); //Die ArrayList wird chronologisch nach dem Spieleventdatum sortiert
                            Globals.getInstance().setSpieltermine(list); //Die sortierte Liste wird als globale Variabel gespeichert um in den Event verwendet werden zu können.
                        } else {
                            Toast.makeText(MainActivity.this, "Error: something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JSONgetSpielterminResponse> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT ).show();
                    }
                });
    }



}
