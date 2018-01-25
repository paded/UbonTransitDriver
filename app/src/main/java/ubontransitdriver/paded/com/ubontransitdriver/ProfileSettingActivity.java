package ubontransitdriver.paded.com.ubontransitdriver;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileSettingActivity extends AppCompatActivity {
    private ImageButton btn_back;
    private Button btn_logout;
    private FirebaseAuth auth;
    private String TAG = "PST11";
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_setting_layout);

        btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_logout = (Button)findViewById(R.id.btn_logout);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    progressBar.setVisibility(View.GONE);
                    // user auth state is changed - user is null
                    // launch login activity
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("logout",true);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
//                    startActivity(new Intent(ProfileSettingActivity.this, LoginActivity.class));
//                    finish();
                }
            }
        };





        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                auth.signOut();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
