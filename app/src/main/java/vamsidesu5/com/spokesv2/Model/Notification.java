package vamsidesu5.com.spokesv2.Model;

import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.sql.Timestamp;
import java.util.Date;

public class Notification {
    private String sendUser;
    private String date;

    public Notification(String sendUser, long timestamp) {
        this.sendUser = sendUser;
        date = new Date(timestamp).toString();
    }

    public String getSendUser() {
        return sendUser;
    }

    public void setSendUser(String sendUser) {
        this.sendUser = sendUser;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
