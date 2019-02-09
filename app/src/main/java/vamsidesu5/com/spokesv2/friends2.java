package vamsidesu5.com.spokesv2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
//import com.firebase.client.Firebase;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Map;

public class friends2 extends AppCompatActivity {
    private ListView friendsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends2);
        friendsList = (ListView) findViewById(R.id.friendsspinner);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AccessToken fb_token = AccessToken.getCurrentAccessToken();
        final String uId = user.getUid();
        final ArrayList<String> friendslist = new ArrayList<>();
        DatabaseReference currRef = FirebaseDatabase.getInstance().getReference("users/" + uId + "/friends");
        currRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long j = dataSnapshot.getChildrenCount();
                for (DataSnapshot c : dataSnapshot.getChildren()) {
                    String add = c.getValue().toString();
                    friendslist.add(add);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(friends2.this, android.R.layout.simple_spinner_item, friendslist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                friendsList.setAdapter(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        friendsList.setClickable(true);
        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                final String friend = (String) friendsList.getItemAtPosition(position);
                DatabaseReference currRef = FirebaseDatabase.getInstance().getReference("users/");
                currRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot c : dataSnapshot.getChildren()) {
                            Map<String, String> map2 = (Map) c.getValue();
                            for(Map.Entry<String, String> uids: map2.entrySet()){
                                if(friend.equals(uids.getValue())){
                                    String userID = c.getKey();
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

            }
        });





        ImageButton addfriend = (ImageButton) findViewById(R.id.addfriend);
        addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(friends2.this, addfriends.class));
            }
        });

        ImageButton activityfeed = (ImageButton) findViewById(R.id.menu);
        activityfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(friends2.this, activityfeed.class));
            }
        });

        ImageButton notifications = (ImageButton) findViewById(R.id.notifications);
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(friends2.this, notifications.class));
            }
        });

        ImageButton gotopoke = (ImageButton) findViewById(R.id.gotopoke);
        gotopoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(friends2.this, friends2.class));
            }
        });
    }
}
