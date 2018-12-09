package iwmb02.com.iwmb02.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class ChatMessage {

    @SerializedName("chatSender")
    @Expose
    private String chatSender;
    @SerializedName("chatendTime")
    @Expose
    private String chatSendTime;
    @SerializedName("chatText")
    @Expose
    private String chatText;

    public ChatMessage() {

    }

    public String getChatSender() {
        return chatSender;
    }

    public void setChatSender(String chatSender) {
        this.chatSender = chatSender;
    }

    public String getChatSendTime() {
        return chatSendTime;
    }

    public void setChatSendTime(String chatSendTime) {
        this.chatSendTime = chatSendTime;
    }

    public String getChatText() {
        return chatText;
    }

    public void setChatText(String chatText) {
        this.chatText = chatText;
    }


    /*String toString() {

        String result = "Chat Message : Sender [" + chatSender + "] Time [" + chatSendTime + "]" + chatText + "]";
        return result;
    }*/
}
