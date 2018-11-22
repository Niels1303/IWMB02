package iwmb02.com.iwmb02;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import iwmb02.com.iwmb02.models.Backendless;
import iwmb02.com.iwmb02.services.ConnectionChecker;
import iwmb02.com.iwmb02.services.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    EditText etEmail, etPassword;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
    }

    public void btnLogin(View v){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        //Hier wird sicher gestellt, dass der Benutzer sowohl eine Email Adresse als auch sein Passwort eingetippt hat.
        if (email.equals("") || password.equals("")){
            Toast.makeText(this,"Please fill out all fields", Toast.LENGTH_SHORT).show();
        }
        //... gegebenenfalls wird zuerst überprüft, ob eine Internetverbindung vorhanden ist.
        else {
            if(ConnectionChecker.connectionAvailable(getApplicationContext())){
                Backendless user = new Backendless();
                //In der Datenbank wurde "email" als Identity festgelegt. Deswegen muss der HTTP Request mit diesem Wert erfolgen.
                user.setLogin(email);
                user.setPassword(password);

                progressDialog = new ProgressDialog(Login.this);
                progressDialog.show();

                NetworkService.getInstance()
                        .getRestApiClient()
                        .loginUser(user)
                        .enqueue(new Callback<Backendless>() {
                            @Override
                            public void onResponse(Call<Backendless> call, Response<Backendless> response) {
                                Backendless resp = response.body();
                                //Die Rückgabewerte werden abgefangen und in SharedPreferences für weitere Activities gespeichert.
                                //"apply()" ist eine assynchronische Methode um die Daten zu speichern (damit die UI nicht blockiert wird).
                                SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
                                sp.edit().putBoolean("logged",true).apply();
                                sp.edit().putString("username",resp.getUsername()).apply();
                                sp.edit().putString("user-token",resp.getUserToken()).apply();
                                Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                                //Nach dem Einlogen wird der Benutzer zur Main Activity weitergeleitet.
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<Backendless> call, Throwable t) {
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
