package vamsidesu5.com.spokesv2.View;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vamsidesu5.com.spokesv2.Model.FriendsViewAdapter;
import vamsidesu5.com.spokesv2.Model.RecyclerViewAdapter;
import vamsidesu5.com.spokesv2.Model.User;
import vamsidesu5.com.spokesv2.R;
import vamsidesu5.com.spokesv2.ViewModel.FriendsViewModel;

//TODO Clean UP Class
public class FriendsView extends AppCompatActivity {
    private FriendsViewModel mViewModel;
    private User currUser = User.getInstance();
    public FriendsViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends2);
        mViewModel = ViewModelProviders.of(this).get(FriendsViewModel.class);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AccessToken fb_token = AccessToken.getCurrentAccessToken();
        final String uId = user.getUid();
        final ArrayList<String> friendslist = new ArrayList<>();
        final DatabaseReference currRef = FirebaseDatabase.getInstance().getReference("users/" + uId + "/friends");

        adapter = new FriendsViewAdapter(friendslist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        friendslist.add("Temp1");
        friendslist.add("Temp2");
        friendslist.add("Temp3");
        friendslist.add("Temp4");
        friendslist.add("Temp5");
        friendslist.add("Temp6");

        currRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long j = dataSnapshot.getChildrenCount();
                for (DataSnapshot c : dataSnapshot.getChildren()) {
                    String add = c.getValue().toString();
                    friendslist.add(add);
                }
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        Button notifications = (Button) findViewById(R.id.notifications);
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FriendsView.this, NotificationsView.class));
            }
        });

    }

    public void friendClicked(View view) {
        Button currFriend = view.findViewById(R.id.friendButton);
        mViewModel.pokeFriend(currFriend.getText().toString());
        Toast.makeText(this, "Successfuly poked " + currFriend.getText().toString(), Toast.LENGTH_SHORT).show();
    }

}
