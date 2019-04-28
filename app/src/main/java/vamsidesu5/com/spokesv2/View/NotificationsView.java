package vamsidesu5.com.spokesv2.View;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import vamsidesu5.com.spokesv2.Model.Database;
import vamsidesu5.com.spokesv2.Model.Notification;
import vamsidesu5.com.spokesv2.Model.NotificationManager;
import vamsidesu5.com.spokesv2.Model.RecyclerViewAdapter;
import vamsidesu5.com.spokesv2.Model.User;
import vamsidesu5.com.spokesv2.R;
import vamsidesu5.com.spokesv2.ViewModel.NotificationsViewModel;

public class NotificationsView extends AppCompatActivity {
    private NotificationsViewModel mViewModel;
    public RecyclerViewAdapter adapter;
    LinkedList<Notification> animalNames = new LinkedList<Notification>();
    float x1, x2, y1, y2;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        long ts = 1551298097782L;

        Date d = new Date(ts);
        Log.d("yo", d.toString());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        adapter = new RecyclerViewAdapter(animalNames);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        Database database = new Database("users/" + User.getInstance().getFirebaseUserID() + "/notifications");

        database.getRef().limitToLast(10).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.d("animal", child.toString());
                    String sendUser = child.child("senderName").getValue().toString();
                    String receiveUser = child.child("receiveName").getValue().toString();
                    if (sendUser.equals(User.getInstance().getFirebaseUser().getDisplayName())) {
                        sendUser = "You";
                    } else {
                        receiveUser = "You";
                    }
                    animalNames.addFirst(new Notification(sendUser + " poked " +
                            receiveUser,
                            Long.parseLong(child.child("timestamp").getValue().toString())));
                    Log.d("sizelist", "" + animalNames.size());
                }
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mViewModel = ViewModelProviders.of(this).get(NotificationsViewModel.class);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        Log.d("motionTouch", "motion" + " " + motionEvent.getAction() + " " + motionEvent.getX() + " " + motionEvent.getY());
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = motionEvent.getX();
                y1 = motionEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = motionEvent.getX();
                y2 = motionEvent.getY();
                if (x2 > x1 && Math.abs(y2 - y1) <= 300) {
                    finish();
                }
        }
        return false;
    }
}
