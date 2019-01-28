package iwmb02.com.iwmb02.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;
import iwmb02.com.iwmb02.R;
import android.os.Bundle;
import iwmb02.com.iwmb02.models.*;
import iwmb02.com.iwmb02.services.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddEvent extends AppCompatActivity {

    private CalendarView calendarView;
    private Button btnAddEvent;
    private Date calDate;
    private DateFormat dateFormat = new SimpleDateFormat("dd  MMMM yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        android.support.v7.widget.Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        calendarView = findViewById(R.id.CalendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.clear();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DATE, dayOfMonth);
                calDate = cal.getTime();
                //Toast.makeText(AddEvent.this,calDate.toString(),Toast.LENGTH_SHORT).show();



            }
        });

        btnAddEvent = findViewById(R.id.btnAddEvent);
        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSpieltermine();
            }
        });

    }

    public void checkSpieltermine(){
        NetworkService.getInstance()
                .getRestApiClient()
                .getSpieltermin()
                .enqueue(new Callback<JSONgetSpielterminResponse>() {
                    @Override
                    public void onResponse(Call<JSONgetSpielterminResponse> call, Response<JSONgetSpielterminResponse> response) {
                        if(response.isSuccessful()) {
                            boolean exists = false;
                            JSONgetSpielterminResponse resp = response.body();
                            Spieltermin[] spieltermine = resp.getResults();
                            String strDate = dateFormat.format(calDate); //Das Datum muss zuerst formatiert werden bevor es verglichen wird, da ein Datum auch die Uhrzeit enthält
                            for(int i=0; i < spieltermine.length; i++){
                                if(spieltermine[i].getEventDate().gettransformdate().equals(strDate)) { //Überprüft zuerst, ob bereits ein Event mit diesem Datum in der DB vorhanden ist.
                                    Toast.makeText(AddEvent.this,"An event already exists on this date",Toast.LENGTH_SHORT).show();
                                    exists = true;
                                    break;
                                }
                            }
                            if(exists == false) { //Ein neuer Termin wird nur angelegt, falls es noch nicht in der DB vorhanden ist
                                Spieltermin spieltermin = new Spieltermin();
                                EventDate eventD = new EventDate();
                                eventD.setType("Date");
                                eventD.setIso(calDate);
                                spieltermin.setEventDate(eventD); //Das ausgewählte Datum aus dem CalendarView wird in einem Objekt "geladen"
                                addSpieltermin(spieltermin);
                            }
                        } else {
                            Toast.makeText(AddEvent.this, "Error: something went wrong", Toast.LENGTH_SHORT ).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JSONgetSpielterminResponse> call, Throwable t) {
                        Toast.makeText(AddEvent.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void addSpieltermin(final Spieltermin spieltermin) {
        NetworkService.getInstance()
                .getRestApiClient()
                .createSpieltermin(spieltermin)
                .enqueue(new Callback<JSONgetSpielterminResponse>() {
                    @Override
                    public void onResponse(Call<JSONgetSpielterminResponse> call, Response<JSONgetSpielterminResponse> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(AddEvent.this,"A new event has been added", Toast.LENGTH_SHORT).show();
                            spieltermin.setObjectId(response.body().getObjectId()); //Die objectId des gerade erstellten DB Eintrags wird im Spieltermin Objekt gespeichert.
                            spieltermin.setType("Pointer");
                            spieltermin.setClassName("Spieltermin");
                            User user = new User(); //Ein Teilnehmerobjekt enthält ebenfalls einen Pointer der aus ein User verweist. Deshalb muss auch ein User Objekt instanziert werden.
                            user.setType("Pointer");
                            user.setClassName("_User");
                            user.setObjectId(Globals.getInstance().getUserId()); //ObjectId des eingelogten Benutzers
                            spieltermin.setAusrichter(user); //Der User der das Event angelegt hat wird gleich auch als Spielausrichter eingesetzt.
                            Teilnehmer tnr = new Teilnehmer(); //Nachdem ein neues Spieltermin angelegt wurde, soll auch gleich der User als Teilnehmer angelegt werden werden.
                            tnr.setSpielterminId(spieltermin);
                            tnr.setUserId(user);
                        } else {
                            Toast.makeText(AddEvent.this, "Error: something went wrong", Toast.LENGTH_SHORT ).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JSONgetSpielterminResponse> call, Throwable t) {
                        Toast.makeText(AddEvent.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(AddEvent.this, Login.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
