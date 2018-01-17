package ubontransitdriver.paded.com.ubontransitdriver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ResetPasswordActivity extends AppCompatActivity {
    private Button btn_resetPassword, btn_back;
    private EditText input_email;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private String TAG = "LOGIN22";
    ScrollView mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_layout);

        auth = FirebaseAuth.getInstance();

        mainLayout = (ScrollView) findViewById(R.id.mainlayout);

        btn_resetPassword = (Button) findViewById(R.id.btn_reset_password);
        btn_back = (Button) findViewById(R.id.btn_back);

        input_email = (EditText) findViewById(R.id.input_email);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);



        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }


}
