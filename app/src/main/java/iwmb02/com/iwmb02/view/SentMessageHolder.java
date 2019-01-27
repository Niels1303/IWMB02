package iwmb02.com.iwmb02.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import iwmb02.com.iwmb02.R;
import iwmb02.com.iwmb02.models.Nachricht;

import java.text.SimpleDateFormat;

public class SentMessageHolder extends RecyclerView.ViewHolder {
    TextView messageText, timeText;

    SentMessageHolder(View itemView) {
        super(itemView);
        messageText = itemView.findViewById(R.id.text_message_body);
        timeText = itemView.findViewById(R.id.text_message_time);
    }

    void bind(Nachricht message) {
        messageText.setText(message.getBody());

        // Format the stored timestamp into a readable String using method.
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH.mm");
        timeText.setText(simpleDateFormat.format(message.getCreatedAt()));
    }
}