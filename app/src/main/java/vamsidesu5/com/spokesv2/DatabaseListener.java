package vamsidesu5.com.spokesv2;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DatabaseListener implements ValueEventListener {

    String child;
    boolean exists = false;
    boolean retrieve = false;
    DatabaseReference userRef;
    String childData = "";
    Map<String,Object> mapUpdated;
    View myView;

    public DatabaseListener (String newChild, Map<String,Object> newMapUpdated, DatabaseReference newUserRef){
        System.out.print("access");
        child = newChild;
        mapUpdated = newMapUpdated;
        userRef = newUserRef;
    }

    public DatabaseListener(boolean ret, DatabaseReference newUserRef, View view) {
        userRef = newUserRef;
        retrieve = true;
        myView = view;
    }

    public DatabaseListener (String newChild, String newChildData, DatabaseReference newUserRef){
        child = newChild;
        childData = newChildData;
        userRef = newUserRef;
    }
    @Override
    public void onDataChange(DataSnapshot snapshot) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (retrieve) {
            String username = snapshot.getKey();
            String uId = snapshot.child("uID").getValue(String.class);
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
                }
            });

        }
        if (childData != null && !snapshot.exists()) {
            userRef.setValue(childData);
        } else if (mapUpdated != null && !snapshot.hasChildren()) {
            userRef.updateChildren(mapUpdated);
        }
    }
    @Override
    public void onCancelled(DatabaseError error) {
    }

    public String retData() {
        return childData;
    }
}
