package vamsidesu5.com.spokesv2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button spokes = (Button) findViewById(R.id.spokes);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
        spokes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                createSignInIntent();
            }
        });


    }

    public void createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_create_intent]
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String uId = user.getUid();

                StringBuilder newEmailId = new StringBuilder();
                String currEmail = user.getEmail();
                int i = 0;
                while (currEmail.charAt(i) != '@') {
                    newEmailId.append(currEmail.charAt(i));
                    i++;
                }

                String emailId = newEmailId.toString();

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
