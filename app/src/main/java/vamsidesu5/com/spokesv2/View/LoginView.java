package vamsidesu5.com.spokesv2.View;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import vamsidesu5.com.spokesv2.R;
import vamsidesu5.com.spokesv2.ViewModel.LoginViewModel;

public class LoginView extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private LoginViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        List<AuthUI.IdpConfig> providers = mViewModel.signInSetup();
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
        super.onCreate(savedInstanceState);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                mViewModel.loginSuccessLog(getApplicationContext());
                if (mViewModel.checkSetup())
                    startActivity(new Intent(LoginView.this, SetupView.class));
                else
                    startActivity(new Intent(LoginView.this, FriendsView.class));
            }

        }
    }
}
