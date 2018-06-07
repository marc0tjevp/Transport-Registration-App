package theekransje.douaneapp.Domain;

import java.io.Serializable;

/**
 * Created by Sander on 5/24/2018.
 */

public class Driver implements Serializable {
    private static final String TAG = "Driver";
    private String uid;
    private String userName;
    private String passwd;
    private String IMEIHash;
    private String lastName;
    private String firstName;
    private String token;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getIMEIHash() {
        return IMEIHash;
    }

    public void setIMEIHash(String IMEIHash) {
        this.IMEIHash = IMEIHash;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
