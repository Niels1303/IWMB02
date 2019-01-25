package iwmb02.com.iwmb02.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import iwmb02.com.iwmb02.R;
import iwmb02.com.iwmb02.models.Spieltermin;
import iwmb02.com.iwmb02.models.Teilnehmer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class TeilnehmerAdapter extends RecyclerView.Adapter<TeilnehmerAdapter.SpielterminViewHolder>{

    private Map<Date,ArrayList<Teilnehmer>> map;
    private DateFormat dateFormat = new SimpleDateFormat("dd  MMMM yyyy");


    public TeilnehmerAdapter(Map<Date,ArrayList<Teilnehmer>> map) {
        this.map = map;
    }

    @NonNull
    @Override
    public SpielterminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewtype) {

        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.fragment_events_row,parent, false );

        return new SpielterminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpielterminViewHolder holder, int position) {

        //Damit die Liste über ein Index gesucht werden kann, muss die Objektreferenz zuerst in ein ArrayList kopiert werden.
        ArrayList<Map.Entry<Date,ArrayList<Teilnehmer>>> indexedList = new ArrayList<>(map.entrySet());
        Map.Entry<Date,ArrayList<Teilnehmer>> entry = indexedList.get(position);

        String  strDate = dateFormat.format(entry.getKey());
        holder.eventSpielterminTextView.setText(strDate);
        holder.eventFoodSupplierTextView.setText("Participants: ");

        ArrayList<Teilnehmer> teilnehmer = entry.getValue();
        for(int i=0; i < teilnehmer.size(); i++) {
            holder.eventFoodSupplierTextView.append(teilnehmer.get(i).getUserId().getVorname());
            if(teilnehmer.get(i).isAusrichter()) {
                holder.eventFoodSupplierTextView.append(" (host)");
            }
            if(i < teilnehmer.size()-1) {
                holder.eventFoodSupplierTextView.append(", "); //Nach dem letzten Teilnehmer sollte kein Komma gesetzt werden.
            }

        }
        if(teilnehmer.get(0).getSpielterminId().getFoodSupplier() != null) {
            holder.eventFoodSupplierTextView.append(System.getProperty("line.separator"));
            holder.eventFoodSupplierTextView.append("Food Supplier: " + teilnehmer.get(0).getSpielterminId().getFoodSupplier()); //Alle Teilnehmer in der ArrayListe zeigen auf das selbe Spieltermin. Also kann auch das erste Element in der ArrayList verwendet werden.
        }
        if(teilnehmer.get(0).getSpielterminId().getGame() != null) {
            holder.eventFoodSupplierTextView.append(System.getProperty("line.separator"));
            holder.eventFoodSupplierTextView.append("Game(s): " + teilnehmer.get(0).getSpielterminId().getGame());
        }

    }

    @Override
    public int getItemCount() {
        return map.size();
    }

    class SpielterminViewHolder extends RecyclerView.ViewHolder {

        TextView eventFoodSupplierTextView;
        TextView eventSpielterminTextView;

        public SpielterminViewHolder(@NonNull View itemView) {
            super(itemView);

            eventSpielterminTextView=itemView.findViewById(R.id.tv_eventdate);
            eventFoodSupplierTextView=itemView.findViewById(R.id.tv_foodsupplier);

        }
    }

}