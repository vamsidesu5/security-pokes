package vamsidesu5.com.spokesv2.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import vamsidesu5.com.spokesv2.R;

public class SetupView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
    }

    protected void submit(View view) {
        startActivity(new Intent(SetupView.this, FriendsView.class));
    }
}