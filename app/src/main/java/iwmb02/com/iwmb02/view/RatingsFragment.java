package iwmb02.com.iwmb02.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.EventLog;
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
import java.util.*;

public class RatingsFragment extends Fragment {

    private TeilnehmerResponse[] resp = Globals.getInstance().getTeilnehmerResponses();
    //Dies ist nur eine "shortcut" Lösung. Fall mehr Zeit besteht, sollte eine Objectliste anstelle 2 Arraylisten verwendet werden damit die Daten chronologisch sortiert werden können.
    private ArrayList<Spieltermin> spieltermine = new ArrayList<Spieltermin>();
    //Hier kann das Format des anzeigten Datums angepasst werden
    private DateFormat dateFormat = new SimpleDateFormat("dd  MMMM yyyy");
    private TextView tvDate;
    private ImageButton ibLeft, ibRight;
    private RatingBar rbHost, rbGames, rbFood, rbEvent;
    private int hostRating, gamesRating, foodRating, eventRating;
    private Button btFeedback;
    private int counter = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ratings, container, false);
        //Da die JSONRückgabe verschachtelt ist, müssen die Spieltermine tief in dem Objekt gesucht werden
        for (int i = 0; i < resp.length; i++) {
                TeilnehmerResponse teilnehmerResp = resp[i];
                spieltermine.add(teilnehmerResp.getSpielterminId());
        }
        Collections.sort(spieltermine);
        tvDate = v.findViewById(R.id.tvDate);
        //Counter fängt bei "0" an, deswegen wird das älteste Datum angezeigt
        String strDate = dateFormat.format(spieltermine.get(counter).getEventDate().getEventDate());
        tvDate.setText(strDate);
        ibLeft = v.findViewById(R.id.ibLeft);
        //Click auf dem linken Pfeil (ältere Spieltermine)
        ibLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(counter <= 0){
                    Toast.makeText( getActivity(), "No older events", Toast.LENGTH_SHORT ).show();
                } else {
                    counter--;
                    String strDate = dateFormat.format(spieltermine.get(counter).getEventDate().getEventDate());
                    tvDate.setText(strDate);
                }
            }

        });
        ibRight = v.findViewById(R.id.ibRight);
        //Click auf dem rechten Pfeil (neuere Spieltermine)
        ibRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(counter >= spieltermine.size()-1){
                    Toast.makeText( getActivity(), "No younger events", Toast.LENGTH_SHORT ).show();
                } else {
                    counter++;
                    String strDate = dateFormat.format(spieltermine.get(counter).getEventDate().getEventDate());
                    tvDate.setText(strDate);
                }
            }

        });

        rbHost = v.findViewById(R.id.rbHost);
        rbHost.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                switch ((int) ratingBar.getRating()) {
                    case 1:
                        hostRating = 1;
                        break;
                    case 2:
                        hostRating = 2;
                        break;
                    case 3:
                        hostRating = 3;
                        break;
                    case 4:
                        hostRating = 4;
                        break;
                    case 5:
                        hostRating = 5;
                        break;
                    default:
                        hostRating = 3;
                }
            }
        });

        rbGames = v.findViewById(R.id.rbGames);
        rbGames.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                switch ((int) ratingBar.getRating()) {
                    case 1:
                        gamesRating = 1;
                        break;
                    case 2:
                        gamesRating = 2;
                        break;
                    case 3:
                        gamesRating = 3;
                        break;
                    case 4:
                        gamesRating = 4;
                        break;
                    case 5:
                        gamesRating = 5;
                        break;
                    default:
                        gamesRating = 3;
                }
            }
        });

        rbFood = v.findViewById(R.id.rbFood);
        rbFood.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                switch ((int) ratingBar.getRating()) {
                    case 1:
                        foodRating = 1;
                        break;
                    case 2:
                        foodRating = 2;
                        break;
                    case 3:
                        foodRating = 3;
                        break;
                    case 4:
                        foodRating = 4;
                        break;
                    case 5:
                        foodRating = 5;
                        break;
                    default:
                        foodRating = 3;
                }
            }
        });

        rbEvent = v.findViewById(R.id.rbEvent);
        rbEvent.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                switch ((int) ratingBar.getRating()) {
                    case 1:
                        eventRating = 1;
                        break;
                    case 2:
                        eventRating = 2;
                        break;
                    case 3:
                        eventRating = 3;
                        break;
                    case 4:
                        eventRating = 4;
                        break;
                    case 5:
                        eventRating = 5;
                        break;
                    default:
                        eventRating = 3;
                }
            }
        });

        btFeedback = v.findViewById(R.id.btFeedback);
        btFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String userId = Globals.getInstance().getUserId();
                final String spielterminId = spieltermine.get(counter).getObjectId();
                Map<String, String> data = new HashMap<>();
                //Bevor die Bewertungen in der DB gespeichert werden wird überprüft, ob der User seine Bewertung bereits abgegeben hat. Also ob ein Feld bereits sowohl die userId als auch die spielterminId besitzt.
                data.put("where", "{\"$and\":[{\"userId\":{\"__type\":\"Pointer\",\"className\":\"_User\",\"objectId\":\""+ userId +"\"}},{\"spielterminId\":{\"__type\":\"Pointer\",\"className\":\"Spieltermin\",\"objectId\":\""+ spielterminId +"\"}}]}");
                NetworkService.getInstance()
                        .getRestApiClient()
                        .checkBewertung(data)
                        .enqueue(new Callback<JSONBewertungResponse>() {
                            @Override
                            public void onResponse(Call<JSONBewertungResponse> call, Response<JSONBewertungResponse> response) {
                                if(response.isSuccessful()) {
                                    JSONBewertungResponse resp = response.body();
                                    //Falls ein Datensatz zurückgegeben wird, bedeutet es, dass der User bereits dieses Event bewertet hat.
                                    if(resp.getResults().length != 0) {
                                        Toast.makeText(getActivity(),"You have already rated this event!",Toast.LENGTH_SHORT).show();
                                    } else{
                                        //Für den POST Request wird JSON benötigt. Also muss ein Objekt übermittelt werden.
                                        User user = new User();
                                        user.setType("Pointer");
                                        user.setClassName("_User");
                                        user.setObjectId(userId);

                                        Spieltermin spieltermin = new Spieltermin();
                                        spieltermin.setType("Pointer");
                                        spieltermin.setClassName("Spieltermin");
                                        spieltermin.setObjectId(spielterminId);

                                        Bewertung bewertung = new Bewertung();
                                        bewertung.setUserId(user);
                                        bewertung.setSpielterminId(spieltermin);
                                        bewertung.setHostRating(hostRating);
                                        bewertung.setGamesRating(gamesRating);
                                        bewertung.setFoodRating(foodRating);
                                        bewertung.setEventRating(eventRating);

                                        NetworkService.getInstance()
                                                .getRestApiClient()
                                                .setBewertung(bewertung)
                                                .enqueue(new Callback<JSONBewertungResponse>() {
                                                    @Override
                                                    public void onResponse(Call<JSONBewertungResponse> call, Response<JSONBewertungResponse> response) {
                                                        if(response.isSuccessful()) {
                                                            Toast.makeText(getActivity(),"Ratings sent",Toast.LENGTH_SHORT).show();
                                                        }else {
                                                            Toast.makeText( getActivity(), "Error: something went wrong", Toast.LENGTH_SHORT ).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<JSONBewertungResponse> call, Throwable t) {
                                                        Toast.makeText( getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT ).show();
                                                    }
                                                });

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<JSONBewertungResponse> call, Throwable t) {
                                Toast.makeText( getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT ).show();
                            }
                        });

            }
        });






        return v;
    }
}
