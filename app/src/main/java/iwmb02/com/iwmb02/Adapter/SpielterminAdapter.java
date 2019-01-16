package iwmb02.com.iwmb02.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import iwmb02.com.iwmb02.R;
import iwmb02.com.iwmb02.models.Spieltermin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SpielterminAdapter extends RecyclerView.Adapter<SpielterminAdapter.SpielterminViewHolder>{

    private ArrayList<Spieltermin> spielterminList;
    private DateFormat dateFormat = new SimpleDateFormat("dd  MMMM yyyy");


    public SpielterminAdapter(ArrayList<Spieltermin> spielterminList) {
        this.spielterminList = spielterminList;
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


        holder.eventSpielterminTextView.setText(spielterminList.get(position).getEventDate().gettransformdate());
        holder.eventFoodSupplierTextView.setText(spielterminList.get(position).getFoodSupplier());


    }

    @Override
    public int getItemCount() {
        return spielterminList.size();
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
