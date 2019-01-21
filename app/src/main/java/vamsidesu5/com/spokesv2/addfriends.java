package vamsidesu5.com.spokesv2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class addfriends extends AppCompatActivity {

    private static addfriends mContext;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriends);
        mContext = this;
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String friendUserId = null;

        DatabaseReference currRef = FirebaseDatabase.getInstance().getReference("friend_req/" + user.getUid());
        currRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final TextView pendingFriends = (TextView) findViewById(R.id.pending);
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot c : dataSnapshot.getChildren()) {
                        DatabaseReference newSnapRef = FirebaseDatabase.getInstance().getReference("users/" + c.getKey());
                        newSnapRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                pendingFriends.append(System.getProperty("line.separator") + dataSnapshot.child("name").getValue(String.class));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button search = (Button) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText emailId = (EditText) findViewById(R.id.searchEmail);
                String stringEmailId = emailId.getText().toString();
                DatabaseReference userRef2 = FirebaseDatabase.getInstance().getReference("dir/" + stringEmailId);

                userRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot snapshot) {
                        final String username = snapshot.getKey();
                        final String uId = snapshot.child("uID").getValue(String.class);
                        Log.d("hey", uId);
                        final String email = snapshot.child("email").getValue(String.class);

                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("request_type", "sent");

                        Map<String, Object> childUpdates2 = new HashMap<>();
                        childUpdates2.put("request_type", "received");

                        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("friend_req/" + user.getUid() + "/" + uId);
                        dbRef.updateChildren(childUpdates);

                        DatabaseReference dbRefUser2 = FirebaseDatabase.getInstance().getReference("friend_req/" + uId + "/" + user.getUid());
                        dbRefUser2.updateChildren(childUpdates2).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(addfriends.getContext(), "Pending Friend Request with " + email, Toast.LENGTH_LONG).show();
                                final TextView pendingFriends = (TextView) findViewById(R.id.pending);
                                final DatabaseReference newSnapRef = FirebaseDatabase.getInstance().getReference("users/" + uId);
                                newSnapRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        pendingFriends.append(System.getProperty("line.separator") + dataSnapshot.child("name").getValue(String.class));
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                emailId.setText("");


                //UserRecord userRecord = FirebaseAuth.getInstance().getUser(user.getUid());

            }
        });

        final ImageButton addfriend = (ImageButton) findViewById(R.id.addfriend);
        addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(addfriends.this, addfriends.class));
            }
        });

        ImageButton activityfeed = (ImageButton) findViewById(R.id.menu);
        activityfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(addfriends.this, activityfeed.class));
            }
        });

        ImageButton notifications = (ImageButton) findViewById(R.id.notifications);
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(addfriends.this, notifications.class));
            }
        });

        ImageButton gotopoke = (ImageButton) findViewById(R.id.gotopoke);
        gotopoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(addfriends.this, friends2.class));
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String post(String url, String param ) throws Exception{
        String charset = "UTF-8";
        URLConnection connection = new URL(url).openConnection();
        connection.setDoOutput(true); // Triggers POST.
        connection.setRequestProperty("Accept-Charset", charset);
        connection.setRequestProperty("Content-Type", "application/json;charset=" + charset);

        try (OutputStream output = connection.getOutputStream()) {
            output.write(param.getBytes(charset));
        }

        InputStream response = connection.getInputStream();
        String friendUserId = convertStreamToString(response);
        return friendUserId;
    }
    public static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static addfriends getContext() {
        return mContext;
    }
}
