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
import iwmb02.com.iwmb02.Adapter.SpielterminAdapter;
import iwmb02.com.iwmb02.R;
import iwmb02.com.iwmb02.models.JSONgetSpielterminResponse;
import iwmb02.com.iwmb02.models.Spieltermin;
import iwmb02.com.iwmb02.services.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Arrays;

public class EventsFragment extends Fragment {

    private ArrayList<Spieltermin> results;
    private SpielterminAdapter spielterminAdapter;
    private RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_events, container, false);
        recyclerView= v.findViewById(R.id.rv_eventlist);
        getSpieltermin();
        return v;
    }

    public  Object getSpieltermin() {

        NetworkService.getInstance()
                .getRestApiClient()
                .getSpieltermin()
                .enqueue(new Callback<JSONgetSpielterminResponse>() {
                    @Override
                    public void onResponse(Call<JSONgetSpielterminResponse> call, Response<JSONgetSpielterminResponse> response) {
                        JSONgetSpielterminResponse resp = response.body();
                        if (resp != null && resp.getResults() != null) {

                            Spieltermin[] spieltermine = resp.getResults();
                            results = new ArrayList<Spieltermin>(Arrays.asList(spieltermine));

                            //                  for (Result r : results) {
//
                            //                      Log.i("testing123", "*********************" + r.getEventDate().getIso());                      ;
                            //                    Log.i("testing123", "*********************" + r.getFoodSupplier());
                            //              }

                            viewData();
                        }

                    }

                    @Override
                    public void onFailure(Call<JSONgetSpielterminResponse> call, Throwable t) {

                    }
                });

        return results;
    }

    private void viewData() {
        spielterminAdapter=new SpielterminAdapter(results);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(spielterminAdapter);
    }
}