package iwmb02.com.iwmb02.view;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import iwmb02.com.iwmb02.adapter.TeilnehmerAdapter;
import iwmb02.com.iwmb02.R;
import iwmb02.com.iwmb02.models.GetTeilnehmerResponse;
import iwmb02.com.iwmb02.models.Teilnehmer;
import iwmb02.com.iwmb02.services.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.*;

public class EventsFragment extends Fragment {

    private ArrayList<Teilnehmer> list = new ArrayList<Teilnehmer>();
    private TeilnehmerAdapter teilnehmerAdapter;
    private RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_events, container, false);
        recyclerView= v.findViewById(R.id.rv_eventlist);
        getTeilnehmer();
        return v;
    }

    public  void getTeilnehmer() {
        Map<String, String> data = new HashMap<>();
        data.put("include", "spielterminId,userId"); //Beide Tabellen "Spieltermin" und "User" werden eingebunden
        NetworkService.getInstance()
                .getRestApiClient()
                .getTeilnehmer(data)
                .enqueue(new Callback<GetTeilnehmerResponse>() {
                    @Override
                    public void onResponse(Call<GetTeilnehmerResponse> call, Response<GetTeilnehmerResponse> response) {
                        GetTeilnehmerResponse resp = response.body();
                        if (resp != null && resp.getResults() != null) {
                            Teilnehmer[] teilnehmer = resp.getResults();
                            Map<Date, Teilnehmer> teilnehmerHashMap = new HashMap<Date, Teilnehmer>();
                            for(int i=0; i < teilnehmer.length; i++) {
                                Teilnehmer obj = teilnehmerHashMap.get(teilnehmer[i].getSpielterminId().getEventDate().getIso()); //holt das Objekt mit dem gleichen Eventdatum
                                if(teilnehmerHashMap.get(teilnehmer[i].getSpielterminId().getEventDate().getIso()) != null) { //falls bereits ein Objekt mit dem gleichen Eventdatum in der HashMap vorhanden ist...
                                    if(teilnehmer[i].isAusrichter()) {
                                        obj.getUserId().appendVorname(", " + teilnehmer[i].getUserId().getVorname() + " (host)"); //Fügt dem Attribut "Vorname" des in der HashMap gespeicherten Objekts den Namen des Spielausrichter hinzu
                                    } else {
                                        obj.getUserId().appendVorname(", " + teilnehmer[i].getUserId().getVorname()); //Fügt dem Namen eines weiteren Spielers hinzu.
                                    }

                                } else { // Falls noch kein Objekt mit diesem Eventdatum in der HasMap gespeichert wurde, füge dieses Objekt hinzu.
                                    teilnehmerHashMap.put(teilnehmer[i].getSpielterminId().getEventDate().getIso(), teilnehmer[i]);
                                }
                            }
                            for (Map.Entry<Date, Teilnehmer> entry : teilnehmerHashMap.entrySet()) { //Wandelt die HasMap in eine ArrayList, weil bereits ein Adapter für eine Arraylist implementiert wurde.
                                list.add(entry.getValue());
                            }



                            viewData();
                        }

                    }

                    @Override
                    public void onFailure(Call<GetTeilnehmerResponse> call, Throwable t) {

                    }
                });
    }

    private void viewData() {
        teilnehmerAdapter =new TeilnehmerAdapter(list);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(teilnehmerAdapter);
    }
}