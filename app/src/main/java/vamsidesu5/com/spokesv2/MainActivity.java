package vamsidesu5.com.spokesv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;

    private FirebaseAuth mAuth;

    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        createSignInIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

    }
    public void createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        Log.d("status", "created sign in intent");
        List<String> permissions = new ArrayList<>();
        permissions.add("email");
        permissions.add("public_profile");
        permissions.add("user_friends");
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().setPermissions(permissions).build());


        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

        Log.d("status", "complete sign in");
        // [END auth_fui_create_intent]
    }
//
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("status", "onActivityResult");
        //Log.d("access", "activity result");
        super.onActivityResult(requestCode, resultCode, data);
        //callbackManager.onActivityResult(requestCode, resultCode,data);


        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                SharedPreferences sharedPreferences;
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String token = sharedPreferences.getString("token", "-1");

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                AccessToken fb_token = AccessToken.getCurrentAccessToken();

                final String uId = user.getUid();

                DatabaseReference userRef2 = FirebaseDatabase.getInstance().getReference("users/" + uId);
                if (!token.equals("-1")) {
                    DatabaseReference userRef3 = FirebaseDatabase.getInstance().getReference("users/" + uId + "/token");
                    userRef3.setValue(token);
                }



                Log.d("yo", fb_token.getUserId());
                GraphRequest request = GraphRequest.newMeRequest(
                        fb_token,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                Log.d("res", object.toString());
                                try {
                                    JSONArray jsonArray = object.getJSONObject("friends").getJSONArray("data");
                                    String j = " " + jsonArray.length();
                                    Log.d("leggooo", jsonArray.toString());

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        String name = jsonArray.getJSONObject(i).getString("name");
                                        String id = jsonArray.getJSONObject(i).getString("id");
                                        DatabaseReference userRef2 = FirebaseDatabase.getInstance().getReference("users/" + uId + "/friends/" + id);

                                        userRef2.setValue(name);

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



                StringBuilder newEmailId = new StringBuilder();
                String currEmail = user.getEmail();
                int i = 0;
                while (currEmail.charAt(i) != '@') {
                    newEmailId.append(currEmail.charAt(i));
                    i++;
                }

                String emailId = newEmailId.toString();
                DatabaseReference userRef7 = FirebaseDatabase.getInstance().getReference("messages");
                userRef7.setValue("hey");

                Log.d("tag", user.getEmail());
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users/" + uId);
                DatabaseListener childListener = new DatabaseListener(uId, uId, userRef);
                userRef.addListenerForSingleValueEvent(childListener);

                DatabaseReference dirRef = FirebaseDatabase.getInstance().getReference("dir/" + emailId);
                Map<String, Object> childUpdates1 = new HashMap<>();
                childUpdates1.put("uID", user.getUid());
                childUpdates1.put("email", user.getEmail());
                dirRef.updateChildren(childUpdates1);

                DatabaseListener childListenerDataDir = new DatabaseListener(emailId, childUpdates1, dirRef);
                dirRef.addListenerForSingleValueEvent(childListenerDataDir);

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("name", user.getDisplayName());
                childUpdates.put("email", user.getEmail());

                DatabaseReference tokenRef = FirebaseDatabase.getInstance().getReference("users/" + uId);
                DatabaseListener childListenerData = new DatabaseListener(uId, childUpdates, tokenRef);
                tokenRef.addListenerForSingleValueEvent(childListenerData);

                userRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    boolean hasChild = false;
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.hasChild("preferences")) {
                            startActivity(new Intent(MainActivity.this, setup.class));
                        } else {
                            startActivity(new Intent(MainActivity.this, addfriends.class));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });
            } else {

            }
        }
    }
}
