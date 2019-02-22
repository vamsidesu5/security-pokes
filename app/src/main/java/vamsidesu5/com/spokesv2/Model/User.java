package vamsidesu5.com.spokesv2.Model;

import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseUser;

public class User {
    private static final User currUser = new User();
    private String firebaseUserID;
    private AccessToken facebookToken;
    private FirebaseUser firebaseUser;

    public static User getInstance() {
        return currUser;
    }

    public void updateUserInfo (String firebaseUserID, AccessToken facebookToken, FirebaseUser firebaseUser) {
        this.firebaseUserID = firebaseUserID;
        this.facebookToken = facebookToken;
        this.firebaseUser = firebaseUser;
    }

    public String getFirebaseUserID() {
        return firebaseUserID;
    }

    public void setFirebaseUserID(String firebaseUserID) {
        this.firebaseUserID = firebaseUserID;
    }

    public AccessToken getFacebookToken() {
        return facebookToken;
    }

    public void setFacebookToken(AccessToken facebookToken) {
        this.facebookToken = facebookToken;
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public void setFirebaseUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }
}
