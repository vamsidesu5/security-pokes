package vamsidesu5.com.spokesv2.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import vamsidesu5.com.spokesv2.Model.Database;
import vamsidesu5.com.spokesv2.Model.User;

public class LoginViewModel extends ViewModel {
    private static final int RC_SIGN_IN = 123;
    private User currUser = User.getInstance();

    public List<AuthUI.IdpConfig> signInSetup() {
        List<String> permissions = new ArrayList<>();
        permissions.add("email");
        permissions.add("public_profile");
        permissions.add("user_friends");
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().setPermissions(permissions).build());
        return providers;
    }

    public void loginSuccessLog(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String regToken = sharedPreferences.getString("token", "-1");
        currUser.updateUserInfo(FirebaseAuth.getInstance().getCurrentUser().getUid(), AccessToken.getCurrentAccessToken(), FirebaseAuth.getInstance().getCurrentUser());

        updateUserInfo();
        updateToken(regToken);
        updateFriendList();
    }

    public boolean checkSetup() {
        return true;
    }

    private void updateUserInfo() {
        Database database = new Database("users/" + currUser.getFirebaseUserID());
        List<String> userInfoNodes = new ArrayList<>();
        userInfoNodes.add("email");
        userInfoNodes.add("name");
        List<Object> userInfoData = new ArrayList<>();
        userInfoData.add(currUser.getFirebaseUser().getEmail());
        userInfoData.add(currUser.getFirebaseUser().getDisplayName());
        database.updateChild(database.constructPayload(userInfoNodes, userInfoData));
    }

    private void updateToken(String token) {
        if (!token.equals("-1")) {
            Database database = new Database("users/" + currUser.getFirebaseUserID());
            database.updateChild(database.constructPayload("/token", token));
        }
    }

    private void updateFriendList() {
        GraphRequest request = GraphRequest.newMeRequest(
                currUser.getFacebookToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            JSONArray jsonArray = object.getJSONObject("friends").getJSONArray("data");
                            Database database = new Database("users/" + currUser.getFirebaseUserID() + "/friends");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String friendsName = jsonArray.getJSONObject(i).getString("name");
                                String friendId = jsonArray.getJSONObject(i).getString("id");
                                database.updateChild(database.constructPayload("/" + friendId, friendsName));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "friends");
        request.setParameters(parameters);
        request.executeAsync();
    }

}
