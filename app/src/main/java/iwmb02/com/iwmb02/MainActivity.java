package iwmb02.com.iwmb02;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import iwmb02.com.iwmb02.models.Chat;
import iwmb02.com.iwmb02.services.ConnectionChecker;
import iwmb02.com.iwmb02.services.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

 /*       //Hier wird der "default" channell aboniert
        if (ConnectionChecker.connectionAvailable(getApplicationContext())) {
            Chat chat = new Chat();

            NetworkService.getInstance()
                    .getRestApiClient()
                    .subscribeChannel(chat)
                    .enqueue(new Callback<Chat>() {
                        @Override
                        public void onResponse(Call<Chat> call, Response<Chat> response) {
                            if (response.isSuccessful()) {
                                Chat resp = response.body();
                                SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
                                sp.edit().putString("subscriptionId", resp.getSubscriptionId()).apply();
                            }
                        }

                        @Override
                        public void onFailure(Call<Chat> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Error: couldn't subscribe to channell", Toast.LENGTH_SHORT).show();
                        }
                    });
        }*/


        android.support.v7.widget.Toolbar mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new EventsFragment()).commit();
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
            // Beim Drücken auf "logout" werden alle SharedPreferences gelöscht...
            SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
            sp.edit().clear().apply();
            //... und der Benutzer zur Login Activity weitergeleitet.
            Intent intent = new Intent(MainActivity.this, Login.class);
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
