package iwmb02.com.iwmb02;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        android.support.v7.widget.Toolbar mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
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
        if(item.getItemId() == R.id.action_logout){
            // Beim Drücken auf "logout" werden alle SharedPreferences gelöscht...
            SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
            sp.edit().clear().apply();
            //... und der Benutzer zur Login Activity weitergeleitet.
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
