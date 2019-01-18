package iwmb02.com.iwmb02.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import iwmb02.com.iwmb02.R;
import iwmb02.com.iwmb02.models.Nachricht;

import java.text.SimpleDateFormat;

public class ReceivedMessageHolder extends RecyclerView.ViewHolder {
    TextView messageText, timeText, nameText;

    ReceivedMessageHolder(View itemView) {
        super(itemView);
        messageText = (TextView) itemView.findViewById(R.id.text_message_body);
        timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        nameText = (TextView) itemView.findViewById(R.id.text_message_name);
    }

    void bind(Nachricht message) {
        messageText.setText(message.getBody());

        // Format the stored timestamp into a readable String using method.
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH.mm");
        timeText.setText(simpleDateFormat.format(message.getCreatedAt()));
        nameText.setText(message.getUserId());

    }
}