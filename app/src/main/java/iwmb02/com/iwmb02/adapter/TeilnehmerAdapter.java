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
import java.util.ArrayList;

public class TeilnehmerAdapter extends RecyclerView.Adapter<TeilnehmerAdapter.SpielterminViewHolder>{

    private ArrayList<Teilnehmer> teilnehmerList;
    private DateFormat dateFormat = new SimpleDateFormat("dd  MMMM yyyy");


    public TeilnehmerAdapter(ArrayList<Teilnehmer> spielterminList) {
        this.teilnehmerList = spielterminList;
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


        holder.eventSpielterminTextView.setText(teilnehmerList.get(position).getSpielterminId().getEventDate().gettransformdate());
        holder.eventFoodSupplierTextView.setText("Participants: " +teilnehmerList.get(position).getUserId().getVorname());
        if(teilnehmerList.get(position).getSpielterminId().getFoodSupplier() != null) {
            holder.eventFoodSupplierTextView.append(System.getProperty("line.separator"));
            holder.eventFoodSupplierTextView.append("Food Supplier: " + teilnehmerList.get(position).getSpielterminId().getFoodSupplier());
        }
        if(teilnehmerList.get(position).getSpielterminId().getGame() != null) {
            holder.eventFoodSupplierTextView.append(System.getProperty("line.separator"));
            holder.eventFoodSupplierTextView.append("Game(s): " + teilnehmerList.get(position).getSpielterminId().getGame());
        }
    }

    @Override
    public int getItemCount() {
        return teilnehmerList.size();
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
