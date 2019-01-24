package iwmb02.com.iwmb02.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventDate {
    @SerializedName("__type")
    @Expose
    private String type;
    @SerializedName("iso")
    @Expose
    public Date iso;

    public transient DateFormat dateFormat = new SimpleDateFormat("dd  MMMM yyyy");

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getIso() {
        return iso;
    }

    public void setIso(Date iso) {
        this.iso = iso;
    }

    public String gettransformdate(){

        String  strDate = dateFormat.format(getIso());
        return strDate;
    }
}