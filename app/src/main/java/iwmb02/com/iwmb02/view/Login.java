package iwmb02.com.iwmb02.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import iwmb02.com.iwmb02.R;
import iwmb02.com.iwmb02.models.User;
import iwmb02.com.iwmb02.models.Globals;
import iwmb02.com.iwmb02.services.ConnectionChecker;
import iwmb02.com.iwmb02.services.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    EditText etUsername, etPassword;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
    }

    public void btnLogin(View v){
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        //Hier wird sicher gestellt, dass der Benutzer sowohl eine Email Adresse als auch sein Passwort eingetippt hat.
        if (username.equals("") || password.equals("")){
            Toast.makeText(this,"Please fill out all fields", Toast.LENGTH_SHORT).show();
        }
        //... gegebenenfalls wird zuerst überprüft, ob eine Internetverbindung vorhanden ist.
        else {
            if(ConnectionChecker.connectionAvailable(getApplicationContext())){

                progressDialog = new ProgressDialog(Login.this);
                progressDialog.show();

                //Die API von Parse setzt voraus, dass der Benutzername und das Password zum Anmelden benutzt werden sollen.
                NetworkService.getInstance()
                        .getRestApiClient()
                        .loginUser(username, password)
                        .enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()) {
                                    User resp = response.body();
                                    //Die Rückgabewerte werden abgefangen und als globale Variabel für weitere Activities gespeichert.
                                    //"apply()" ist eine assynchronische Methode um die Daten zu speichern (damit die UI nicht blockiert wird).
                                    Globals global = Globals.getInstance();
                                    global.setLoggedIn(true);
                                    global.setUserId(resp.getObjectId());
                                    global.setUsername(resp.getUsername());
                                    global.setCreatedAt(resp.getCreatedAt());
                                    global.setSessionToken(resp.getSessionToken());
                                    Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    //Nach dem Einlogen wird der Benutzer zur Main Activity weitergeleitet.
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                    progressDialog.dismiss();
                                } else {
                                    Toast.makeText(Login.this,"Error: something went wrong", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }

                        }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                //Bei einem Fehler wird die geworfene Nachricht als "Toast" angezeigt.
                                Toast.makeText(Login.this,"Error: " + t.getMessage() , Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
            } else{
                Toast.makeText(this, "No Internet connection", Toast.LENGTH_SHORT).show();
            }

        }

    }

    //Beim Klicken auf dem TextView (Create new Account) wird der Benuter zur Signup Activity weitergeleitet.
    public void tvGoCreate(View v){
        Intent intent = new Intent(Login.this, SignUp.class);
        startActivity(intent);

    }

}
