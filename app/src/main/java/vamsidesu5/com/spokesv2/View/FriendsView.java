package vamsidesu5.com.spokesv2.View;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vamsidesu5.com.spokesv2.R;
import vamsidesu5.com.spokesv2.ViewModel.FriendsViewModel;

//TODO Clean UP Class
public class FriendsView extends AppCompatActivity {
    private ListView friendsList;
    private FriendsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends2);

        //mViewModel = ViewModelProviders.of(this).get(FriendsViewModel.class);
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

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(FriendsView.this, android.R.layout.simple_spinner_item, friendslist);
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


                currRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    String totalPokes;
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        pokeRef.child("totalPokes").runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result doTransaction(MutableData mutableData) {
                                Integer score = mutableData.getValue(Integer.class);
                                if (score == null) {
                                    return Transaction.success(mutableData);
                                }
                                Log.d("score", score.toString());

                                score++;
                                totalPokes = score.toString();

                                mutableData.setValue(score);
                                return Transaction.success(mutableData);
                            }

                            @Override
                            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot2) {
                                final DatabaseReference messageRef = FirebaseDatabase.getInstance().getReference("pendingMessages/" + "m" + totalPokes);
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
                            }
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

                startActivity(new Intent(FriendsView.this, FriendsView.class));
            }
        });

        ImageButton activityfeed = (ImageButton) findViewById(R.id.menu);
        activityfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FriendsView.this, ActivityView.class));
            }
        });

        ImageButton notifications = (ImageButton) findViewById(R.id.notifications);
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FriendsView.this, vamsidesu5.com.spokesv2.notifications.class));
            }
        });

        ImageButton gotopoke = (ImageButton) findViewById(R.id.gotopoke);
        gotopoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FriendsView.this, FriendsView.class));
            }
        });
    }
}
