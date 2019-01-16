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
import iwmb02.com.iwmb02.models.JSONSpielterminResponse;
import iwmb02.com.iwmb02.models.Spieltermin;
import iwmb02.com.iwmb02.services.NetworkService;
import iwmb02.com.iwmb02.services.RestApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

public class EventsFragment extends Fragment {

    private ArrayList<Spieltermin> results;
    private SpielterminAdapter spielterminAdapter;
    private RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_events, container, false);

        getSpieltermin();
    }

    public  Object getSpieltermin() {

        RestApiClient restApiClient= (RestApiClient) NetworkService.getInstance();
        Call<JSONSpielterminResponse> call=restApiClient.getResults();

        call.enqueue(new Callback<JSONSpielterminResponse>() {


            @Override
            public void onResponse(retrofit2.Call<JSONSpielterminResponse> call, Response<JSONSpielterminResponse> response) {
                JSONSpielterminResponse jsonSpielterminResponse = response.body();
                if (jsonSpielterminResponse != null && jsonSpielterminResponse.getResults() != null) {

                    results = (ArrayList<Spieltermin>) jsonSpielterminResponse.getResults();

                    //                  for (Result r : results) {
//
                    //                      Log.i("testing123", "*********************" + r.getEventDate().getIso());                      ;
                    //                    Log.i("testing123", "*********************" + r.getFoodSupplier());
                    //              }

                    viewData();

                }
            }

            @Override
            public void onFailure(retrofit2.Call<JSONSpielterminResponse> call, Throwable t) {

            }
        });
        return results;
    }

    private void viewData() {

        recyclerView= recyclerView.findViewById(R.id.rv_eventlist);
        spielterminAdapter=new SpielterminAdapter(results);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(EventsFragment.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(spielterminAdapter);



    }
}



