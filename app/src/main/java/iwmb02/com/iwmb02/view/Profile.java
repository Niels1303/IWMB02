package iwmb02.com.iwmb02.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import iwmb02.com.iwmb02.R;
import iwmb02.com.iwmb02.models.Brettspiel;
import iwmb02.com.iwmb02.models.GameResponse;
import iwmb02.com.iwmb02.models.Globals;
import iwmb02.com.iwmb02.models.JSONGameResponse;
import iwmb02.com.iwmb02.services.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        android.support.v7.widget.Toolbar mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        String userId = Globals.getInstance().getUserId();
        //in diesem HashMap werden alle Parameter eingegeben
        Map<String, String> data = new HashMap<>();
        //Die UserId ist dynamisch. Sie entspricht dem eingelogten User und wurde nach dem einlogen in eine globale Variabel gespeichert.
        data.put("where", "{\"user\":{\"__type\":\"Pointer\",\"className\":\"_User\",\"objectId\":\""+userId+"\"}}");
        data.put("include", "brettspiel");
        NetworkService.getInstance()
                .getRestApiClient()
                .getGames(data)
                .enqueue(new Callback<JSONGameResponse>() {
            @Override
            public void onResponse(Call<JSONGameResponse> call, Response<JSONGameResponse> response) {
                if(response.isSuccessful()) {
                    ListView lv = (ListView) findViewById(R.id.lvProfile);
                    JSONGameResponse resp = response.body();
                    GameResponse[] jub = resp.getResults();
                    ArrayList<String> pList = new ArrayList<String>();
                    pList.add("Username: " + Globals.getInstance().getUsername());
                    pList.add("Owned Games:");
                    int length = Array.getLength(jub);
                    for (int i = 0; i < length; i++) {
                        Brettspiel brettspiel = jub[i].getBrettspiel();
                        pList.add("   " + brettspiel.getName() + ", Amount of Players: " + brettspiel.getPlayersNumber());
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Profile.this,android.R.layout.simple_list_item_1,pList);
                    lv.setAdapter(arrayAdapter);

                } else {
                    Toast.makeText(Profile.this, "Error: something went wrong", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JSONGameResponse> call, Throwable t) {
                Toast.makeText(Profile.this,"Error: " + t.getMessage() , Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(Profile.this, Login.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
