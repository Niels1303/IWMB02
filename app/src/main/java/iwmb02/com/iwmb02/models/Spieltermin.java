package iwmb02.com.iwmb02.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Spieltermin implements Comparable<Spieltermin> {


    @SerializedName("objectId")
    @Expose
    private String objectId;
    @SerializedName("EventDate")
    @Expose
    public EventDate eventDate;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("Food_Supplier")
    @Expose
    public String foodSupplier;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public EventDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(EventDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getFoodSupplier() {
        return foodSupplier;
    }

    public void setFoodSupplier(String foodSupplier) {
        this.foodSupplier = foodSupplier;
    }

    @Override
    public int compareTo(Spieltermin spieltermin) {
        return this.getEventDate().gettransformdate().compareTo(spieltermin.getEventDate().gettransformdate());
    }
}
