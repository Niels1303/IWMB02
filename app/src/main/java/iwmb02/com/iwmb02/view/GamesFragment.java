package iwmb02.com.iwmb02.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GamesFragment extends Fragment {

    private Spinner spinner1;
    private ArrayList<Spieltermin> spieltermine = Globals.getInstance().getSpieltermine(); //Die in der MainActivity abgefragte Lsite aller Spieltermine wird aus den globalen Variablen geladen.
    //Hier kann das Format des anzeigten Datums angepasst werden
    private DateFormat dateFormat = new SimpleDateFormat("dd  MMMM yyyy");
    private TextView tvDate;
    private ImageButton ibLeft, ibRight;
    private int counter = 0;
    private String selectedGame, objectId;
    List<String> list = new ArrayList<String>();
    Teilnehmer[] teilnehmer;
    private Button btnSaveChoice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_games, container, false);
        tvDate = v.findViewById(R.id.tvDate);
        ibLeft = v.findViewById(R.id.ibLeft);
        ibRight = v.findViewById(R.id.ibRight);

        buildEventNav();
        addItemsOnSpinner(v); //Liste aller Brettspiele wird im Spinner geladen
        getTeilnehmer(); //Lädt alle Teilnehmer Objekte aus der Teilnehmer Tabelle in der "teilnehmer" Variabel
        btnSaveChoice = v.findViewById(R.id.btnSaveGame);
        btnSaveChoice.setOnClickListener(new View.OnClickListener() { //Beim einem Click auf den Button wird überprüft, ob sich der User für das ausgewählte Spieltermin angemeldet hat. Gegebenenfalls wird sein Brettspielwahl in der DB gespeichert.
            @Override
            public void onClick(View v) {
                boolean checkJoined = false;
                for(int i = 0; i < teilnehmer.length; i++) {
                    if(teilnehmer[i].getSpielterminId().getObjectId().equals(spieltermine.get(counter).getObjectId())  && teilnehmer[i].getUserId().getObjectId().equals(Globals.getInstance().getUserId())) { //Die Wahl kann nur gespeichert werden, wenn bereits ein Objekt mit entsprechendem Spieltermin UND dem eingelogten User bestehen.
                        checkJoined = true;
                        objectId = teilnehmer[i].getObjectId(); //wird von der "updateTeilnehmer" Methode benötigt um das Objekt zu bestimmen, das verändert werden muss.
                        updateTeilnehmer(); //Das ausgewählte Spiel wird in der DB im entsprechendem Objekt gespeichert.
                    }


                }
                if(checkJoined == false) {
                    Toast.makeText(getActivity(), "Please join the event first", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }

    public void buildEventNav() {
        //Counter fängt bei "0" an, deswegen wird das älteste Datum angezeigt
        String strDate = dateFormat.format(spieltermine.get(counter).getEventDate().getIso());
        tvDate.setText(strDate);
        //Click auf dem linken Pfeil (ältere Spieltermine)
        ibLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(counter <= 0){
                    Toast.makeText( getActivity(), "No older events", Toast.LENGTH_SHORT ).show();
                } else {
                    counter--;
                    String strDate = dateFormat.format(spieltermine.get(counter).getEventDate().getIso());
                    tvDate.setText(strDate);
                }
            }

        });

        //Click auf dem rechten Pfeil (neuere Spieltermine)
        ibRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(counter >= spieltermine.size()-1){
                    Toast.makeText( getActivity(), "No younger events", Toast.LENGTH_SHORT ).show();
                } else {
                    counter++;
                    String strDate = dateFormat.format(spieltermine.get(counter).getEventDate().getIso());
                    tvDate.setText(strDate);
                }
            }

        });
    }

    //Liste aller vorhandenen Brettspiele werden dynamisch dem Spinner hinzugefügt
    public void addItemsOnSpinner(View v) {
        spinner1 = (Spinner) v.findViewById(R.id.spinner1);
        //Die Liste aller vorhandenen Brettspiele wird aus der DB abgefragt damit sie später im Dropdownmenü angezeigt werden kann.
        NetworkService.getInstance()
                .getRestApiClient()
                .getBrettspiel()
                .enqueue(new Callback<JSONgetBrettspielResponse>() {
                    @Override
                    public void onResponse(Call<JSONgetBrettspielResponse> call, Response<JSONgetBrettspielResponse> response) {
                        if(response.isSuccessful()) {
                            JSONgetBrettspielResponse resp = response.body();
                            Brettspiel[] brettspiele = resp.getResults();
                            //Brettspiele Array wird durchgelaufen und alle Brettspielnamen werden der Liste hinzugefügt
                            for (int i = 0; i < brettspiele.length; i++) {
                                list.add(brettspiele[i].getName());
                            }
                            //Da die DB Abfrage asynchron ist, darf der ArrayAdapter nur nach der erfolgreichen Abfrage mit der Brettspielliste geladen werden.
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, list);
                            //dataAdapter.notifyDataSetChanged();
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner1.setAdapter(dataAdapter);
                            spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    //Speichert das ausgewählte Brettspiel im DropdownMenü in der Variabel "selectedGame"
                                    selectedGame = parent.getItemAtPosition(position).toString();
                                    Toast.makeText(getActivity().getBaseContext(), selectedGame, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    //ToDO
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), "Error: something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JSONgetBrettspielResponse> call, Throwable t) {
                        Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT ).show();
                    }
                });

    }

    public void getTeilnehmer() {
        Map<String, String> data = new HashMap<>();
        data.put("include", "spielterminId,userId"); //Beide Tabellen "Spieltermin" und "User" werden eingebunden
        NetworkService.getInstance()
                .getRestApiClient()
                .getTeilnehmer(data)
                .enqueue(new Callback<GetTeilnehmerResponse>() {
                    @Override
                    public void onResponse(Call<GetTeilnehmerResponse> call, Response<GetTeilnehmerResponse> response) {
                        if (response.isSuccessful()) {
                            GetTeilnehmerResponse resp = response.body();
                            teilnehmer = resp.getResults();
                        } else {
                            Toast.makeText(getActivity(), "Error: something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<GetTeilnehmerResponse> call, Throwable t) {
                        Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Methode um das ausgewählte Brettspiel im entsprechenden Teilnehmerobjekt zu speichern.
    public void updateTeilnehmer() {
        Teilnehmer tnmr = new Teilnehmer();
        tnmr.setSelectedGame(selectedGame); //Nur das "selectedGame" Attribut des "Teilnehmer" Objekts wird bestimmt. Die leeren Attribute werden in der DB nicht verändert.
        NetworkService.getInstance()
                .getRestApiClient()
                .putTeilnehmer(objectId,tnmr)
                .enqueue(new Callback<Teilnehmer>() {
                    @Override
                    public void onResponse(Call<Teilnehmer> call, Response<Teilnehmer> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(getActivity(), "Your choice has been saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Error: Something went wrong", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Teilnehmer> call, Throwable t) {
                        Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
