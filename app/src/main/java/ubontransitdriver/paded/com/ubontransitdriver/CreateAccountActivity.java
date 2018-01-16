package ubontransitdriver.paded.com.ubontransitdriver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.h6ah4i.android.materialshadowninepatch.MaterialShadowContainerView;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_layout);

        MaterialShadowContainerView shadowView =
                (MaterialShadowContainerView) findViewById(R.id.shadow_item_container);

        float density = getResources().getDisplayMetrics().density;

        shadowView.setShadowTranslationZ(density * 2.0f); // 2.0 dp
        shadowView.setShadowElevation(density * 4.0f); // 4.0 dp


        //Get Firebase auth instance
//        auth = FirebaseAuth.getInstance();
//
//        btnSignIn = (Button) findViewById(R.id.sign_in_button);
//        btnSignUp = (Button) findViewById(R.id.sign_up_button);
//        inputEmail = (EditText) findViewById(R.id.email);
//        inputPassword = (EditText) findViewById(R.id.password);
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);





    }
}
