package iwmb02.com.iwmb02;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

//Die "Splash" Activity ist ein "Splashscreen" das nur verwendet wird um zu überprüfen, ob der User bereits eingelogt ist.
public class Splash extends AppCompatActivity {

    //Diese Android Klasse wird verwendet um User-Daten über alle Activities abrufen bzw. speichern zu können.
    SharedPreferences sp;


    TextView tvLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tvLoading = findViewById(R.id.tvLoading);

        sp = getSharedPreferences("user", MODE_PRIVATE);
        //Mit diesem "if" Block wird überprüft ob der User bereits eingelogt ist. Gegebenenfalls wird er zur MainActivity weitergeleitet.
        if(sp.getBoolean("logged",false)){
            Intent intent = new Intent(Splash.this, MainActivity.class);
            startActivity(intent);
            Splash.this.finish();
        //...andernfalls wird der Benutzer zur LoginActivity weitergeleitet.
        } else {
            tvLoading.setText(R.string.busyLogin);
            Intent intent = new Intent(Splash.this, Login.class);
            startActivity(intent);
            Splash.this.finish();
        }


    }
}
