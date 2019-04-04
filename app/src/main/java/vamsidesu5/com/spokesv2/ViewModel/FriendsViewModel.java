package vamsidesu5.com.spokesv2.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import vamsidesu5.com.spokesv2.Model.Database;
import vamsidesu5.com.spokesv2.Model.User;

public class FriendsViewModel extends ViewModel {
    private User currUser = User.getInstance();
    private Database database;

    public void pokeFriend(String inputFriend) {
        final String friend = inputFriend;
        final DatabaseReference currRef = FirebaseDatabase.getInstance().getReference("users/");
        final DatabaseReference pokeRef = FirebaseDatabase.getInstance().getReference("pokes/");
        final DatabaseReference pokeRefUser = FirebaseDatabase.getInstance().getReference("users/" + currUser.getFirebaseUserID());

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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
                        final DatabaseReference messageRef = FirebaseDatabase.getInstance().getReference("pendingMessages/" + totalPokes);
                        for (DataSnapshot c : dataSnapshot.getChildren()) {
                            Map<String, String> map = (Map) dataSnapshot.getValue();
                            Map<String, String> map2 = (Map) c.getValue();
                            int i = 0;
                            String userID = "";
                            for(Map.Entry<String, String> uids: map2.entrySet()){
                                if(uids.getKey().equals("token") && i == 1){
                                    final String token = uids.getValue();
                                    final String newId = userID;
                                    final Map<String, String> timestamp = ServerValue.TIMESTAMP;
                                    Log.d("regTok", token);
                                    Map<String, Object> messageData = new HashMap<>();
                                    messageData.put("receiveToken", token);
                                    messageData.put("receiveID", userID);
                                    messageData.put("senderID", user.getUid());
                                    messageData.put("timestamp", timestamp);
                                    messageData.put("senderName", user.getDisplayName());
                                    messageData.put("receiveName", friend);
                                    Log.d("yo", friend);
                                    messageRef.updateChildren(messageData);
                                    final DatabaseReference currUserRefNotifications = FirebaseDatabase.getInstance().getReference("users/" + currUser.getFirebaseUserID() + "/notifications/" + totalPokes);
                                    currUserRefNotifications.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Map<String, Object> messageData = new HashMap<>();
                                            messageData.put("receiveToken", token);
                                            messageData.put("receiveID", newId);
                                            messageData.put("senderID", user.getUid());
                                            messageData.put("timestamp", timestamp);
                                            messageData.put("senderName", user.getDisplayName());
                                            messageData.put("receiveName", friend);
                                            currUserRefNotifications.updateChildren(messageData);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    final DatabaseReference pokedUserRefNotifications = FirebaseDatabase.getInstance().getReference("users/" + newId + "/notifications/" + totalPokes);
                                    pokedUserRefNotifications.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Map<String, Object> messageData = new HashMap<>();
                                            messageData.put("receiveToken", token);
                                            messageData.put("receiveID", newId);
                                            messageData.put("senderID", user.getUid());
                                            messageData.put("timestamp", timestamp);
                                            messageData.put("senderName", user.getDisplayName());
                                            messageData.put("receiveName", friend);
                                            pokedUserRefNotifications.updateChildren(messageData);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
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
                pokeRefUser.child("numPokes").runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Integer numPokes = mutableData.getValue(Integer.class);
                        if (numPokes == null) {
                            return Transaction.success(mutableData);
                        }
                        numPokes++;
                        mutableData.setValue(numPokes);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot2) {
                        Log.d("Updated" , "Poke for User is Updated.");
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        database = new Database("users/" + currUser.getFirebaseUserID() + "/friends");

    }
}
