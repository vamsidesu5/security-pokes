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
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
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
        final DatabaseReference currRef = FirebaseDatabase.getInstance().getReference("users/" + uId + "/friends");
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
                final DatabaseReference currRef = FirebaseDatabase.getInstance().getReference("users/");
                final DatabaseReference pokeRef = FirebaseDatabase.getInstance().getReference("pokes/");
                final DatabaseReference messageRef = FirebaseDatabase.getInstance().getReference("messages/pending");


                currRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot c : dataSnapshot.getChildren()) {
                            Map<String, String> map = (Map) dataSnapshot.getValue();
                            Map<String, String> map2 = (Map) c.getValue();
                            int i = 0;
                            String userID = "";
                            for(Map.Entry<String, String> uids: map2.entrySet()){
                                if(uids.getKey().equals("token") && i == 1){
                                    String token = uids.getValue();
                                    Log.d("regTok", token);
                                    Map<String, Object> messageData = new HashMap<>();
                                    messageData.put("receiveToken", token);
                                    messageData.put("receiveID", userID);
                                    messageData.put("senderID", user.getUid());
                                    messageRef.updateChildren(messageData);
                                    i = 0;
                                }
                                if(friend.equals(uids.getValue())){
                                    userID = c.getKey();
                                    i++;
                                }
                            }
                        }
                        pokeRef.child("totalPokes").runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result doTransaction(MutableData mutableData) {
                                Integer score = mutableData.getValue(Integer.class);
                                if (score == null) {
                                    return Transaction.success(mutableData);
                                }
                                mutableData.setValue(score + 1);
                                return Transaction.success(mutableData);
                            }

                            @Override
                            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {}
                        });
                        //pokeRef.child("totalPokes").setValue(1);
                        //pokeRef.child("totalPokes").setValue(Integer.parseInt(dataSnapshot.child("totalPokes").getValue(String.class))+1);
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
