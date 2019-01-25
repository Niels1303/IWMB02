package iwmb02.com.iwmb02.view;

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
import android.widget.EditText;
import android.widget.Toast;
import iwmb02.com.iwmb02.R;
import iwmb02.com.iwmb02.adapter.MessageListAdapter;
import iwmb02.com.iwmb02.models.Globals;
import iwmb02.com.iwmb02.models.NachrichtResponse;
import iwmb02.com.iwmb02.models.Nachricht;
import iwmb02.com.iwmb02.services.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.*;


public class ChatFragment extends Fragment {

    private RecyclerView MessageRecycler;
    private MessageListAdapter MessageAdapter;
    private ArrayList<Nachricht> data;
    private List<Nachricht> messageList = new ArrayList<Nachricht>();
    private FloatingActionButton btnSend, btnRefresh;
    private EditText etMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        // Bei fragments kann der Context über den inflater erfragt werden
        MessageRecycler = (RecyclerView) v.findViewById(R.id.rvChat);
        MessageAdapter = new MessageListAdapter(getContext(), messageList);
        MessageRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        MessageRecycler.setAdapter(MessageAdapter);
        refreshMessages();

        btnSend = v.findViewById(R.id.btnSend);
        etMessage = v.findViewById(R.id.etMessage);
        btnRefresh = v.findViewById(R.id.btnRefresh);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Der zuvor local gespeicherte Username wird zusammen mit der Nachricht in der DB gespeichert.
                Globals global = Globals.getInstance();
                final String userId = global.getUsername();
                Nachricht message = new Nachricht();
                message.setUserId(userId);
                final String text = etMessage.getText().toString().trim();
                //der eingegebene Text wird gelöscht nachdem er gesendet wurde.
                etMessage.getText().clear();
                message.setBody(text);
                //Das neue Objekt wird in der DB hochgeladen.
                NetworkService.getInstance()
                        .getRestApiClient()
                        .sendMessage(message)
                        .enqueue(new Callback<Nachricht>() {
                    @Override
                    public void onResponse(Call<Nachricht> call, Response<Nachricht> response) {
                        Nachricht resp = response.body();
                        //Nachdem das Objekt in der DB gespeichert wurde, wird das Rückgabeobjekt gleich auf dem Display des Benutzers angezeigt.
                        resp.setUserId(userId);
                        resp.setBody(text);
                        messageList.add(resp);
                        MessageAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<Nachricht> call, Throwable t) {
                        Toast.makeText( getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT ).show();
                    }
                });
            }
        });
        //Um die Backendanfragen zu minimieren, werden vorerst die Chat Nachrichten nur manuell abgefragt. Durch einen Sesrver Polling oder Benutzung eines Websockets könnte man später neue Nachrichten automatisch anzeigen lassen.
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshMessages();
            }
        });


        return v;
    }

    //Methode mit der alle Chatnachrichten in der DB des Backends abgerufen werden
    private void refreshMessages() {
        NetworkService.getInstance()
                .getRestApiClient()
                .refreshMessages()
                .enqueue(new Callback<NachrichtResponse>() {
                    @Override
                    public void onResponse(Call<NachrichtResponse> call, Response<NachrichtResponse> response) {
                        if(response.isSuccessful()) {
                            NachrichtResponse nachrichtResponse = response.body();
                            data = new ArrayList<>(Arrays.asList(nachrichtResponse.getResults())); //Die Rückgabe wird als ArrayList konvertiert.
                            if(messageList != null) { //Falls messageList nicht leer ist wird diese Liste gelöscht, da alle Chatnachrichten neu geladen werden.
                                messageList.clear();
                            }
                            messageList.addAll(data);
                            MessageAdapter.notifyDataSetChanged(); // Der Adapter wird benachrichtigt, dass eine Änderung stattgefunden hat.
                        } else {
                            Toast.makeText(getActivity(),"Error!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<NachrichtResponse> call, Throwable t) {
                        Toast.makeText(getActivity(),"Error: " + t.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
