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
import java.util.*;

public class RatingsFragment extends Fragment {

    private ArrayList<Spieltermin> spieltermine = Globals.getInstance().getSpieltermine(); //Die in der MainActivity abgefragte Lsite aller Spieltermine wird aus den globalen Variablen geladen.
    //Hier kann das Format des anzeigten Datums angepasst werden
    private DateFormat dateFormat = new SimpleDateFormat("dd  MMMM yyyy");
    private TextView tvDate, tvScoreHost, tvScoreGames, tvScoreFood, tvScoreEvening;
    private ImageButton ibLeft, ibRight;
    private RatingBar rbHost, rbGames, rbFood, rbEvent;
    private int hostRating, gamesRating, foodRating, eventRating;
    private Button bntFeedback;
    private int counter = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ratings, container, false);
        tvDate = v.findViewById(R.id.tvDate);
        ibLeft = v.findViewById(R.id.ibLeft);
        ibRight = v.findViewById(R.id.ibRight);

        buildEventNav();

        rbHost = v.findViewById(R.id.rbHost);
        rbGames = v.findViewById(R.id.rbGames);
        rbFood = v.findViewById(R.id.rbFood);
        rbEvent = v.findViewById(R.id.rbEvent);

        buildRatingBars();

        tvScoreHost = v.findViewById(R.id.tvScoreHost);
        tvScoreGames = v.findViewById(R.id.tvScoreGame);
        tvScoreFood = v.findViewById(R.id.tvScoreFood);
        tvScoreEvening = v.findViewById(R.id.tvScoreEvening);

        checkScore();

        bntFeedback = v.findViewById(R.id.btFeedback);
        bntFeedback.setOnClickListener(new View.OnClickListener() {
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
                        .enqueue(new Callback<BewertungResponse>() {
                            @Override
                            public void onResponse(Call<BewertungResponse> call, Response<BewertungResponse> response) {
                                if(response.isSuccessful()) {
                                    BewertungResponse resp = response.body();
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
                                                .enqueue(new Callback<BewertungResponse>() {
                                                    @Override
                                                    public void onResponse(Call<BewertungResponse> call, Response<BewertungResponse> response) {
                                                        if(response.isSuccessful()) {
                                                            Toast.makeText(getActivity(),"Ratings sent",Toast.LENGTH_SHORT).show();
                                                        }else {
                                                            Toast.makeText(getActivity(), "Error: something went wrong", Toast.LENGTH_SHORT ).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<BewertungResponse> call, Throwable t) {
                                                        Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT ).show();
                                                    }
                                                });

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<BewertungResponse> call, Throwable t) {
                                Toast.makeText( getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT ).show();
                            }
                        });

            }
        });






        return v;
    }

    public void checkScore() {
        NetworkService.getInstance()
                .getRestApiClient()
                .getBewertung()
                .enqueue(new Callback<BewertungResponse>() {
                    @Override
                    public void onResponse(Call<BewertungResponse> call, Response<BewertungResponse> response) {
                        if(response.isSuccessful()) {
                            BewertungResponse resp = response.body();
                            Bewertung[] bewertungen = resp.getResults();
                            float scoreH = 0, scoreG = 0, scoreE = 0, scoreF = 0;
                            int count = 0; //Anzahl der Bewertungen eines Spieltermins
                            for(int i=0; i < bewertungen.length; i++) {
                                if(spieltermine.get(counter).getObjectId().equals(bewertungen[i].getSpielterminId().getObjectId())) { //es werden nur die Bewertungen des ausgewählten Spieltermins ausgewertet
                                    scoreH = scoreH + bewertungen[i].getHostRating();
                                    scoreG = scoreG + bewertungen[i].getGamesRating();
                                    scoreF = scoreF + bewertungen[i].getFoodRating();
                                    scoreE = scoreE + bewertungen[i].getEventRating();
                                    count++;
                                }
                            }
                            if(scoreH > 0) { // Es reicht aus einen Wert zu überprüfen, denn Ratings werden immer insgesamt in der DB gespeichert
                                tvScoreHost.setText("Score: " + (scoreH / count) + " ( " + count + " votes)");
                                tvScoreGames.setText("Score: " + (scoreG / count) + " ( " + count + " votes)");
                                tvScoreEvening.setText("Score: " + (scoreE / count) + " ( " + count + " votes)");
                                tvScoreFood.setText("Score: " + (scoreF / count) + " ( " + count + " votes)");
                            } else {
                                tvScoreHost.setText("Score: no votes yet"); //Reset des Textes falls noch keine Ratings vorhanden sind
                                tvScoreGames.setText("Score: no votes yet");
                                tvScoreEvening.setText("Score: no votes yet");
                                tvScoreFood.setText("Score: no votes yet");
                            }
                        }else {
                            Toast.makeText(getActivity(), "Error: something went wrong", Toast.LENGTH_SHORT ).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<BewertungResponse> call, Throwable t) {
                        Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT ).show();
                    }
                });



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
                    checkScore();
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
                    checkScore();
                }
            }

        });
    }

    public void buildRatingBars() {
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
    }
}
