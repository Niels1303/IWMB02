package iwmb02.com.iwmb02.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import iwmb02.com.iwmb02.R;
import iwmb02.com.iwmb02.adapter.ProfileListAdapter;
import iwmb02.com.iwmb02.models.*;
import iwmb02.com.iwmb02.services.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;


public class Profile extends AppCompatActivity {

    private ListView lv;
    private ProfileListAdapter mAdapter;
    final Context c = this;
    private FloatingActionButton fbtnAddGame;
    private String gameId;
    private String userId = Globals.getInstance().getUserId();
    private String newGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        android.support.v7.widget.Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        fbtnAddGame = findViewById(R.id.fbtnAddGame);

        addBtnOnclickListener();
        getGames();
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
        if (item.getItemId() == R.id.action_profile) {
            Intent intent = new Intent(Profile.this, Profile.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void addBtnOnclickListener() {
        fbtnAddGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                alertDialogBuilderUserInput.setView(mView);

                final EditText userInputDialogEditText = mView.findViewById(R.id.userInputDialog);
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                newGame = userInputDialogEditText.getText().toString();
                                NetworkService.getInstance()
                                        .getRestApiClient()
                                        .getBrettspiel()
                                        .enqueue(new Callback<BrettspielResponse>() {
                                            @Override
                                            public void onResponse(Call<BrettspielResponse> call, Response<BrettspielResponse> response) {
                                                if(response.isSuccessful()) {
                                                    boolean exists = false;
                                                    final BrettspielResponse resp = response.body();
                                                    Brettspiel[] brettspiele = resp.getResults();
                                                    for(int i=0; i < brettspiele.length; i++){
                                                        if(brettspiele[i].getName().equals(newGame)) {
                                                            exists = true;
                                                            gameId = brettspiele[i].getObjectId();
                                                        }
                                                    }
                                                    //Falls das Spiel noch nicht in der Brettspiel Tabelle vorhanden ist, wird es dort angelegt.
                                                    if(exists == false) {
                                                        addGame();
                                                    } else {
                                                        linkGame(); //Das Spiel wird dem eingelogten User zugeordnet.
                                                    }

                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<BrettspielResponse> call, Throwable t) {
                                                Toast.makeText(Profile.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                // ToDo get user input here
                            }
                        })

                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();
            }
        });

    }

    public void getGames() {
        //in diesem HashMap werden alle Parameter eingegeben
        Map<String, String> data = new HashMap<>();
        //Die UserId ist dynamisch. Sie entspricht dem eingelogten User und wurde nach dem einlogen in eine globale Variabel gespeichert.
        data.put("where", "{\"user\":{\"__type\":\"Pointer\",\"className\":\"_User\",\"objectId\":\""+userId+"\"}}");
        data.put("include", "brettspiel,user");
        NetworkService.getInstance()
                .getRestApiClient()
                .getGames(data)
                .enqueue(new Callback<JoinUserBrettspielResponse>() {
                    @Override
                    public void onResponse(Call<JoinUserBrettspielResponse> call, Response<JoinUserBrettspielResponse> response) {
                        if(response.isSuccessful()) {
                            JoinUserBrettspielResponse resp = response.body();
                            JoinUserBrettspiel[] joinUserBrettspiele = resp.getResults();

                            lv = findViewById(R.id.lvProfile);

                            mAdapter = new ProfileListAdapter(Profile.this);
                            mAdapter.addSectionHeaderItem("My Profile");
                            mAdapter.addItem("Username: " + joinUserBrettspiele[0].getUser().getUsername());
                            mAdapter.addItem("Real name: " + joinUserBrettspiele[0].getUser().getVorname() + " " + joinUserBrettspiele[0].getUser().getNachname());
                            mAdapter.addItem("Amount of hosted Game Nights: " + joinUserBrettspiele[0].getUser().getAusrichterCounter().toString());
                            mAdapter.addItem("Amount of joined Game Nights: " + joinUserBrettspiele[0].getUser().getTeilnehmerCounter().toString());
                            mAdapter.addSectionHeaderItem("My Boardgames");
                            int length = Array.getLength(joinUserBrettspiele);
                            for (int i = 0; i < length; i++) {
                                mAdapter.addItem(joinUserBrettspiele[i].getBrettspiel().getName());
                            }

                            lv.setAdapter(mAdapter);

                        } else {
                            Toast.makeText(Profile.this, "Error: something went wrong", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<JoinUserBrettspielResponse> call, Throwable t) {
                        Toast.makeText(Profile.this,"Error: " + t.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                });
    }
    //Fügt spiel in der Brettspiel Tabelle ein
    public void addGame() {
        Brettspiel brettspiel = new Brettspiel();
        brettspiel.setName(newGame);
        NetworkService.getInstance()
                .getRestApiClient()
                .createBrettspiel(brettspiel)
                .enqueue(new Callback<Brettspiel>() {
                    @Override
                    public void onResponse(Call<Brettspiel> call, Response<Brettspiel> response) {
                        if(response.isSuccessful()) {
                            Brettspiel resp = response.body();
                            gameId = resp.getObjectId();
                            linkGame(); //Das Spiel wird dem eingelogten User zugeordnet.
                            Toast.makeText(Profile.this,"Game added successfully",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Profile.this, "Error: something went wrong", Toast.LENGTH_SHORT ).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Brettspiel> call, Throwable t) {
                        Toast.makeText(Profile.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    //Das Spiel wird dem eingelogten User zugeordnet.
    public void linkGame(){
        User user = new User();
        user.setObjectId(userId);
        user.setType("Pointer");
        user.setClassName("_User");
        Brettspiel brettspiel = new Brettspiel();
        brettspiel.setObjectId(gameId);
        brettspiel.setType("Pointer");
        brettspiel.setClassName("Brettspiel");
        JoinUserBrettspiel userBrettspiel = new JoinUserBrettspiel();
        userBrettspiel.setUser(user);
        userBrettspiel.setBrettspiel(brettspiel);
        NetworkService.getInstance()
                .getRestApiClient()
                .createUserBrettspiel(userBrettspiel)
                .enqueue(new Callback<JoinUserBrettspiel>() {
                    @Override
                    public void onResponse(Call<JoinUserBrettspiel> call, Response<JoinUserBrettspiel> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(Profile.this,"Game successfully linked to your Profile",Toast.LENGTH_SHORT).show();
                            mAdapter.addItem(newGame); //Fügt das neue Spiel in der Liste der ListView ein.
                            mAdapter.notifyDataSetChanged(); //Refresh der ListView damit das neue Spiel sofort angezeigt wird.

                        } else {
                            Toast.makeText(Profile.this, "Error: something went wrong", Toast.LENGTH_SHORT ).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JoinUserBrettspiel> call, Throwable t) {
                        Toast.makeText(Profile.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
