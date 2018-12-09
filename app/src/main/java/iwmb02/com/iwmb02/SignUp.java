package iwmb02.com.iwmb02;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import iwmb02.com.iwmb02.models.User;
import iwmb02.com.iwmb02.services.ConnectionChecker;
import iwmb02.com.iwmb02.services.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    EditText etName, etFirstName, etUsername, etEmail, etPassword, etRePassword;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        etFirstName = findViewById(R.id.etFirstName);
        etName = findViewById(R.id.etName);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etRePassword = findViewById(R.id.etRePassword);
    }

    public void btnSignUp(View v) {
        String firstName = etFirstName.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String repassword = etPassword.getText().toString().trim();

        if(name.equals("") || firstName.equals("") || username.equals("") || email.equals("") || password.equals("") || repassword.equals("")) {
            Toast.makeText(SignUp.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
        } else if(!password.equals(repassword)) {
            Toast.makeText(SignUp.this,"Please make sure your passwords match", Toast.LENGTH_SHORT).show();
        } else {
            if(ConnectionChecker.connectionAvailable(getApplicationContext())) {
                User user = new User();
                user.setVorname(firstName);
                user.setNachname(name);
                user.setUsername(username);
                user.setEmail(email);
                user.setPassword(password);
                user.setAusrichterCounter(0);


                progressDialog = new ProgressDialog(SignUp.this);
                progressDialog.show();

                NetworkService.getInstance()
                        .getRestApiClient()
                        .createUser(user)
                        .enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                Toast.makeText(SignUp.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                SignUp.this.finish();
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Toast.makeText(SignUp.this,"Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });


            } else {
                Toast.makeText(SignUp.this, "No Internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }



}
