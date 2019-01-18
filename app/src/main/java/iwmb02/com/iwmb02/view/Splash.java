package iwmb02.com.iwmb02.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import iwmb02.com.iwmb02.R;
import iwmb02.com.iwmb02.models.Globals;

//Die "Splash" Activity ist ein "Splashscreen" das nur verwendet wird um zu überprüfen, ob der User bereits eingelogt ist.
public class Splash extends AppCompatActivity {
    TextView tvLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tvLoading = findViewById(R.id.tvLoading);

        //Diese Android Klasse wird verwendet um User-Daten über alle Activities abrufen bzw. lokal speichern zu können.
        Globals global = Globals.getInstance();

        //Mit diesem "if" Block wird überprüft ob der User bereits eingelogt ist. Gegebenenfalls wird er zur MainActivity weitergeleitet.
        if(global.isLoggedIn()) {
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
