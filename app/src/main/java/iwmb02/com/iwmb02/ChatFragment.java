package iwmb02.com.iwmb02;

import android.content.Context;
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
import iwmb02.com.iwmb02.models.Globals;
import iwmb02.com.iwmb02.models.JSONResponse;
import iwmb02.com.iwmb02.models.Nachricht;
import iwmb02.com.iwmb02.services.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
        //final Context thiscontext = inflater.getContext();

        MessageRecycler = (RecyclerView) v.findViewById(R.id.rvChat);
        MessageAdapter = new MessageListAdapter(getContext(), messageList);
        MessageRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        MessageRecycler.setAdapter(MessageAdapter);
        MessageRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        btnSend = v.findViewById(R.id.btnSend);
        etMessage = v.findViewById(R.id.etMessage);
        btnRefresh = v.findViewById(R.id.btnRefresh);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Der zuvor local gespeicherte Username wird zusammen mit der Nachricht in der DB gespeichert
                Globals global = Globals.getInstance();
                String userId = global.getUsername();
                Nachricht message = new Nachricht();
                message.setUserId(userId);
                String text = etMessage.getText().toString().trim();
                //der eingegebene Text wird gelöscht nachdem er gesendet wurde
                etMessage.getText().clear();
                message.setBody(text);

                NetworkService.getInstance()
                        .getRestApiClient()
                        .sendMessage(message)
                        .enqueue(new Callback<Nachricht>() {
                    @Override
                    public void onResponse(Call<Nachricht> call, Response<Nachricht> response) {

                    }

                    @Override
                    public void onFailure(Call<Nachricht> call, Throwable t) {
                        Toast.makeText( getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT ).show();
                    }
                });

            }
        });
        //Um die Backendanfragen zu minimieren, werden vorerst die Chat Nachrichten nur manuell abgefragt. Durch einen Sesrver Polling oder Benutzung eines Websockets kann man später neu Nachrichten automatisch anzeigen lassen.
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkService.getInstance()
                        .getRestApiClient()
                        .refreshMessages()
                        .enqueue(new Callback<JSONResponse>() {
                            @Override
                            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                                if(response.isSuccessful()) {
                                    JSONResponse jsonResponse = response.body();
                                    data = new ArrayList<>(Arrays.asList(jsonResponse.getResults())); //das JSON array wird in Nachrichten Objekte konvertiert.
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
                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                                Toast.makeText(getActivity(),"Error: " + t.getMessage() , Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
        return v;
    }
}
