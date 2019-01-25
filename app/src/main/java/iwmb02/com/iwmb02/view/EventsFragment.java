package iwmb02.com.iwmb02.view;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import iwmb02.com.iwmb02.adapter.TeilnehmerAdapter;
import iwmb02.com.iwmb02.R;
import iwmb02.com.iwmb02.models.*;
import iwmb02.com.iwmb02.services.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.*;

public class EventsFragment extends Fragment {

    private ArrayList<Teilnehmer> list = new ArrayList<Teilnehmer>();
    private TeilnehmerAdapter teilnehmerAdapter;
    private RecyclerView recyclerView;
    private FloatingActionButton addEvent;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_events, container, false);
        recyclerView= v.findViewById(R.id.rv_eventlist);
        getTeilnehmer();
        addEvent = v.findViewById(R.id.fbtnAddEvent);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddEvent.class); //Bei einem Click auf dem FloatingActionButton wird zur "AddEvent" Activity gewechselt.
                startActivity(intent);
            }
        });
        return v;
    }

    public  void getTeilnehmer() {
        Map<String, String> data = new HashMap<>();
        data.put("include", "spielterminId,userId"); //Beide Tabellen "Spieltermin" und "User" werden eingebunden
        NetworkService.getInstance()
                .getRestApiClient()
                .getTeilnehmer(data)
                .enqueue(new Callback<TeilnehmerResponse>() {
                    @Override
                    public void onResponse(Call<TeilnehmerResponse> call, Response<TeilnehmerResponse> response) {
                        if(response.isSuccessful()) {
                            TeilnehmerResponse resp = response.body();
                            Teilnehmer[] teilnehmer = resp.getResults();
                            Map<Date,ArrayList<Teilnehmer>> map = new TreeMap<Date,ArrayList<Teilnehmer>>(); //Wir benutzen eine Treemap hier, da diese Datenstruktur automatisch nach dem Key sortiert wird. Somit werden im Key das Eventdatum und im Value eine Liste aller Teilnehmer stehen.
                            //LinkedHashMap<Date,ArrayList<Teilnehmer>> map = new LinkedHashMap<>(); //Um alle vorhandenen Teilnehmer nach dem Eventdatum zu gliedern, sollen die Daten als HashMap an den Adapter übermittelt werden.
                            for(int i=0; i < teilnehmer.length; i++) {
                                Date TreeMapKey = teilnehmer[i].getSpielterminId().getEventDate().getIso();
                                if(map.containsKey(TreeMapKey)) { //hier wird überprüft, ob bereits ein Key mit dem Evendatum in "map" vorhanden ist.
                                    map.get(TreeMapKey).add(teilnehmer[i]); //in diesem Fall wird der aktuelle Teilnehmer dem "Value" hinzugefügt

                                    //List<Teilnehmer> tnmr = map.get(teilnehmer[i].getSpielterminId().getEventDate().getIso()); //übernimmt die bestehende Teilnehmer des entsprechenden Eventdatums.
                                    //tnmr.add(teilnehmer[i]); //Fügt der ArrayList das aktuelle Teilnehmerobjekt hinzu.
                                    //map.put(teilnehmer[i].getSpielterminId().getEventDate().getIso(),tnmr); //Speichert die aktuallisierte ArrayList als Value in der HashMap
                                } else {
                                    ArrayList<Teilnehmer> tnmr = new ArrayList<>(); //Falls noch kein entsprechendes Key vorhanden ist, wird ein neues Key mit dem aktuellen Teilnehmerobjekt erstellt.
                                    tnmr.add(teilnehmer[i]);
                                    map.put(TreeMapKey,tnmr);

                                    //ArrayList<Teilnehmer> tnmr = new ArrayList<Teilnehmer>();
                                    //tnmr.add(teilnehmer[i]);
                                    //map.put(teilnehmer[i].getSpielterminId().getEventDate().getIso(),tnmr); //Falls noch kein entsprechendes Key vorhanden ist, wird ein neues Key mit dem aktuellen Teilnehmerobjekt erstellt.
                                }

                            }




                            viewData(map);

                        } else {
                            Toast.makeText(getActivity(), "Error: something went wrong", Toast.LENGTH_SHORT ).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<TeilnehmerResponse> call, Throwable t) {
                        Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void viewData(Map<Date,ArrayList<Teilnehmer>> map) {
        teilnehmerAdapter =new TeilnehmerAdapter(map);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(teilnehmerAdapter);
    }
}