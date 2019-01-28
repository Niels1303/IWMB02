package iwmb02.com.iwmb02.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import iwmb02.com.iwmb02.R;
import iwmb02.com.iwmb02.models.*;
import iwmb02.com.iwmb02.services.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EditEvent extends AppCompatActivity {

    private Map.Entry<Date,ArrayList<Teilnehmer>> entry = Globals.getInstance().getEventData();
    private ArrayList<Teilnehmer> teilnehmer = new ArrayList<>(entry.getValue());
    List<String> hostList = new ArrayList<>();
    List<String> gameList = new ArrayList<>();
    private DateFormat dateFormat = new SimpleDateFormat("dd  MMMM yyyy");
    private TextView tvEventDate, tvParticipants;
    private Spinner spinHost, spinGame;
    private String selectedHost, selectedGame;
    private EditText etFood;
    private int selectedHostPos;
    private Button btnJoinEvent;
    private boolean isHost, hasJoined;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        String  strDate = dateFormat.format(entry.getKey());
        tvEventDate = findViewById(R.id.tvEventDate);
        tvEventDate.setText("Game Night Date: " + strDate);
        spinGame = findViewById(R.id.spinGame);
        spinHost = findViewById(R.id.spinHost);
        etFood = findViewById(R.id.etFood);
        tvParticipants = findViewById(R.id.tvParticipants);
        btnJoinEvent = findViewById(R.id.btnJoinEvent);

        etFood.setText(teilnehmer.get(0).getSpielterminId().getFoodSupplier());

        isHost = false;
        hasJoined = false;
        for(int i=0; i < teilnehmer.size(); i++) {
            if(teilnehmer.get(i).getUserId().getObjectId().equals(Globals.getInstance().getUserId())){
                hasJoined = true;
            }
            if(teilnehmer.get(i).getSpielterminId().getAusrichter().getObjectId().equals(Globals.getInstance().getUserId())) { //Nur wenn der eingelogte User auch Spielleiter ist werden die Änderungsmöglichkeiten angezeigt.
                spinHost.setEnabled(true);
                spinGame.setEnabled(true);
                etFood.setEnabled(true);
                isHost = true; //Boolean der später entscheidet ob entweder der eingelogte User dem Spieltermin hinzugefügt wird (join) oder die Daten des Events verändert werden (updateEvent)
            } else {
                spinHost.setEnabled(false);
                spinGame.setEnabled(false);
                etFood.setEnabled(false);
            }
        }

        tvParticipants.setText("Participants:");
        tvParticipants.append(System.getProperty("line.separator"));
        for(int i=0; i < teilnehmer.size(); i++) {
            tvParticipants.append(System.getProperty("line.separator"));
            tvParticipants.append(teilnehmer.get(i).getUserId().getVorname() + " " + teilnehmer.get(i).getUserId().getNachname() + " (Joined Events: " + teilnehmer.get(i).getUserId().getTeilnehmerCounter().toString() + " , Hosted Events: " + teilnehmer.get(i).getUserId().getAusrichterCounter().toString() + " )");
            if(teilnehmer.get(i).getSpielterminId().getAusrichter().getObjectId().equals(teilnehmer.get(i).getUserId().getObjectId())) {
                tvParticipants.append(" [HOST]");
            }
        }
        tvParticipants.append(System.getProperty("line.separator"));
        tvParticipants.append(System.getProperty("line.separator"));
        tvParticipants.append("Game: ");
        if(teilnehmer.get(0).getSpielterminId().getGame() != null) {
            tvParticipants.append(teilnehmer.get(0).getSpielterminId().getGame());
        }
        tvParticipants.append(System.getProperty("line.separator"));
        tvParticipants.append(System.getProperty("line.separator"));
        tvParticipants.append("Food Supplier: ");
        if(teilnehmer.get(0).getSpielterminId().getFoodSupplier() != null){
            tvParticipants.append(teilnehmer.get(0).getSpielterminId().getFoodSupplier());
        }


        addItemsOnSpinner1();
        addItemsOnSpinner2();
        setBtnListener();
    }

    public void addItemsOnSpinner1() {
        for(int i=0; i < teilnehmer.size(); i++) {
            hostList.add(teilnehmer.get(i).getUserId().getVorname() + " " + teilnehmer.get(i).getUserId().getNachname());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(EditEvent.this, android.R.layout.simple_spinner_item, hostList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinHost.setAdapter(dataAdapter);
        spinHost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedHost = parent.getItemAtPosition(position).toString();
                selectedHostPos = position;
                Toast.makeText(EditEvent.this, selectedHost, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void addItemsOnSpinner2() {
        NetworkService.getInstance()
                .getRestApiClient()
                .getBrettspiel()
                .enqueue(new Callback<BrettspielResponse>() {
                    @Override
                    public void onResponse(Call<BrettspielResponse> call, Response<BrettspielResponse> response) {
                        if(response.isSuccessful()) {
                            BrettspielResponse resp = response.body();
                            Brettspiel[] brettspiele = resp.getResults();
                            //Brettspiele Array wird durchgelaufen und alle Brettspielnamen werden der Liste hinzugefügt
                            for (int i = 0; i < brettspiele.length; i++) {
                                gameList.add(brettspiele[i].getName());
                            }
                            //Da die DB Abfrage asynchron ist, darf der ArrayAdapter nur nach der erfolgreichen Abfrage mit der Brettspielliste geladen werden.
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(EditEvent.this, android.R.layout.simple_spinner_item, gameList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinGame.setAdapter(dataAdapter);
                            spinGame.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    //Speichert das ausgewählte Brettspiel im DropdownMenü in der Variabel "selectedGame"
                                    selectedGame = parent.getItemAtPosition(position).toString();
                                    Toast.makeText(EditEvent.this, selectedGame, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    //ToDO
                                }
                            });
                        } else {
                            Toast.makeText(EditEvent.this, "Error: something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BrettspielResponse> call, Throwable t) {
                        Toast.makeText(EditEvent.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT ).show();
                    }
                });

    }

    public void updateSpieltermin() {
        Spieltermin spieltermin = new Spieltermin();
        User user = new User();
        if(selectedGame != null) {
            spieltermin.setGame(selectedGame);
        }
        if(etFood.getText().toString() != teilnehmer.get(0).getSpielterminId().getFoodSupplier()) {
            spieltermin.setFoodSupplier(etFood.getText().toString());
        }
        if(selectedHost != null){ //Nur wenn ein anderer Host ausgewählt wurde muss dieser im Objekt hinzugefügt werden
            user.setObjectId(teilnehmer.get(selectedHostPos).getUserId().getObjectId());
            user.setType("Pointer");
            user.setClassName("_User");
            spieltermin.setAusrichter(user);
        }
        if(spieltermin.getGame() != null || spieltermin.getFoodSupplier() != null || spieltermin.getAusrichter() != null){ //Nur wenn eine Änderung stattgefunden hat ist ein Update des Spielterminobjekts notwendig.
            NetworkService.getInstance()
                    .getRestApiClient()
                    .putSpieltermin(teilnehmer.get(0).getSpielterminId().getObjectId(),spieltermin)
                    .enqueue(new Callback<Spieltermin>() {
                        @Override
                        public void onResponse(Call<Spieltermin> call, Response<Spieltermin> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(EditEvent.this, "Data updated", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(EditEvent.this, "Error: something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Spieltermin> call, Throwable t) {
                            Toast.makeText(EditEvent.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(EditEvent.this,"No changes detected. No updates needed",Toast.LENGTH_SHORT).show();
        }


    }

    public void joinEvent(){
        Spieltermin spieltermin = new Spieltermin();
        spieltermin.setObjectId(teilnehmer.get(0).getSpielterminId().getObjectId());
        spieltermin.setType("Pointer");
        spieltermin.setClassName("Spieltermin");
        User user = new User();
        user.setObjectId(Globals.getInstance().getUserId());
        user.setType("Pointer");
        user.setClassName("_User");
        Teilnehmer tnmr = new Teilnehmer();
        tnmr.setSpielterminId(spieltermin);
        tnmr.setUserId(user);
        NetworkService.getInstance()
                .getRestApiClient()
                .createTeilnehmer(tnmr)
                .enqueue(new Callback<TeilnehmerResponse>() {
                    @Override
                    public void onResponse(Call<TeilnehmerResponse> call, Response<TeilnehmerResponse> response) {
                        if(response.isSuccessful()){
                         Toast.makeText(EditEvent.this,"You successfully joined",Toast.LENGTH_SHORT).show();
                         hasJoined = true; //Damit wird verhindert, dass bei einem 2. Klick der User ein 2. Mal angelegt wird.
                        } else{
                            Toast.makeText(EditEvent.this, "Error: something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TeilnehmerResponse> call, Throwable t) {
                        Toast.makeText(EditEvent.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void setBtnListener() {
        btnJoinEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isHost == true){
                    updateSpieltermin();
                } else if(hasJoined == false){
                    joinEvent();
                } else{
                    Toast.makeText(EditEvent.this,"You already joined the event",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
