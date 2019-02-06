package vamsidesu5.com.spokesv2;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    //@Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // [START initialize_auth]
//        // Initialize Firebase Auth
//        mAuth = FirebaseAuth.getInstance();
//        // [END initialize_auth]
//
//        // [START initialize_fblogin]
//        // Initialize Facebook Login button
//        mCallbackManager = CallbackManager.Factory.create();
//        LoginButton loginButton = findViewById(R.id.login_button);
//        loginButton.setReadPermissions("email", "public_profile");
//        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Log.d("yo", "facebook:onSuccess:" + loginResult);
//                handleFacebookAccessToken(loginResult.getAccessToken());
//            }
//
//            @Override
//            public void onCancel() {
//                Log.d("TAG", "facebook:onCancel");
//                // [START_EXCLUDE]
//                // [END_EXCLUDE]
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Log.d("TAG", "facebook:onError", error);
//                // [START_EXCLUDE]
//                // [END_EXCLUDE]
//            }
//        });
//        // [END initialize_fblogin]
//    }
//
//    // [START on_start_check_user]
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//    }
//    // [END on_start_check_user]
//
//    // [START on_activity_result]
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Pass the activity result back to the Facebook SDK
//        mCallbackManager.onActivityResult(requestCode, resultCode, data);
//    }
//    // [END on_activity_result]
//
//    // [START auth_with_facebook]
//    private void handleFacebookAccessToken(AccessToken token) {
//        Log.d("yo", "handleFacebookAccessToken:" + token);
//        // [START_EXCLUDE silent]
//        // [END_EXCLUDE]
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        Log.d("data", database.toString());
//        DatabaseReference myRef = database.getReference("yo");
//
//        myRef.setValue("Hello");
//
//        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
//        Log.d("yo", "started" + credential.getSignInMethod() + credential.getProvider());
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("yo", "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w("TAG", "signInWithCredential:failure", task.getException());
//                            Toast.makeText(MainActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//
//                        // [START_EXCLUDE]
//                        // [END_EXCLUDE]
//                    }
//                });
//        Log.d("user", mAuth.getCurrentUser().getDisplayName());
//        Log.d("yo", "ended");
//    }
//    FirebaseAuth firebaseAuth;
//    CallbackManager callbackManager;
//
//    LoginButton loginButton;
//
//
//    private FirebaseAuth auth;
//    private static final int REQUEST_CODE = 101;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        authenticateUser();
//
//        setContentView(R.layout.activity_main);
//
//
//
//    }
//
//    private void authenticateUser() {
//        startActivityForResult(
//                AuthUI.getInstance().createSignInIntentBuilder()
//                        .setAvailableProviders(getProviderList())
//                        .setIsSmartLockEnabled(false)
//                        .build(),
//                REQUEST_CODE);
//    }
//
//    private List<AuthUI.IdpConfig> getProviderList() {
//
//        List<AuthUI.IdpConfig> providers = new ArrayList<>();
//
//        providers.add(
//                new AuthUI.IdpConfig.EmailBuilder().build());
//        providers.add(new
//                AuthUI.IdpConfig.FacebookBuilder().build());
//        return providers;
//    }
//
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.d("user", "res");
//        super.onActivityResult(requestCode, resultCode, data);
//
//        IdpResponse response = IdpResponse.fromResultIntent(data);
//
//        if (requestCode == REQUEST_CODE) {
//
//            if (resultCode == RESULT_OK) {
//                startActivity(new Intent(this, friends2.class));
//                return;
//            }
//        } else {
//            if (response == null) {
//                // User cancelled Sign-in
//                return;
//            }
//        }
//    }
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        createSignInIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");


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
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                AccessToken fb_token = AccessToken.getCurrentAccessToken();



                Log.d("yo", fb_token.getUserId());


                GraphRequest request = GraphRequest.newMeRequest(
                        fb_token,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Application code
                                try {
                                    String email = object.getString("email");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "name");
                request.setParameters(parameters);
                Log.d("res", request.executeAsync().toString());


                final String uId = user.getUid();

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

                DatabaseReference userRef2 = FirebaseDatabase.getInstance().getReference("users/" + uId);
                DatabaseListener childListenerData = new DatabaseListener(uId, childUpdates, userRef2);
                userRef2.addListenerForSingleValueEvent(childListenerData);

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
