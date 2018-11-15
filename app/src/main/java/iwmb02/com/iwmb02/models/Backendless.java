package iwmb02.com.iwmb02.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Backendless {

    @SerializedName("userStatus")
    @Expose
    private String userStatus;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("created")
    @Expose
    private Integer created;
    @SerializedName("ownerId")
    @Expose
    private Object ownerId;
    @SerializedName("updated")
    @Expose
    private Object updated;
    @SerializedName("objectId")
    @Expose
    private String objectId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("___class")
    @Expose
    private String _class;

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    public Object getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Object ownerId) {
        this.ownerId = ownerId;
    }

    public Object getUpdated() {
        return updated;
    }

    public void setUpdated(Object updated) {
        this.updated = updated;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClass_() {
        return _class;
    }

    public void setClass_(String _class) {
        this._class = _class;
    }

}
