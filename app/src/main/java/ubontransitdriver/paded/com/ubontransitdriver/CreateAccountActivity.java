package ubontransitdriver.paded.com.ubontransitdriver;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h6ah4i.android.materialshadowninepatch.MaterialShadowContainerView;

import java.util.ArrayList;

public class CreateAccountActivity extends AppCompatActivity implements ItemAdapter.ItemListener{
    private EditText inputEmail, inputPassword, inputName;
    private Button btnCreateAccount, btnLogin, btnResetPassword, btnAddBus;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private String TAG = "TEST22";
    private FrameLayout layout_MainMenu;
    BottomSheetDialog bottomSheetDialog;
    BottomSheetBehavior bottomSheetBehavior;

    RecyclerView recyclerView;
    private ItemAdapter mAdapter;
    CoordinatorLayout coordinatorLayout;
    BottomSheetBehavior behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_layout);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

//        mainLayout = (ScrollView) findViewById(R.id.mainlayout);

        inputEmail = (EditText)findViewById(R.id.input_email);
        inputPassword = (EditText)findViewById(R.id.input_password);
        inputName = (EditText)findViewById(R.id.input_name);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnCreateAccount = (Button)findViewById(R.id.btn_create_account);
        btnResetPassword = (Button)findViewById(R.id.btn_reset_password);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnAddBus = (Button)findViewById(R.id.btn_addBus);

        layout_MainMenu = (FrameLayout) findViewById( R.id.framelayout);
        layout_MainMenu.getForeground().setAlpha( 0);

//        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);
//        bottomSheetDialog = new BottomSheetDialog(CreateAccountActivity.this);
//        bottomSheetDialog.setContentView(bottomSheetView);
//
//        bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
//
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

//        TextView addMenu = (TextView) bottomSheetView.findViewById(R.id.menu_add_bus0);
//        TextView editMenu = (TextView) bottomSheetView.findViewById(R.id.menu_add_bus1);
//        TextView deleteMenu = (TextView) bottomSheetView.findViewById(R.id.menu_add_bus2);

//        addMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bottomSheetDialog.hide();
//            }
//        });

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(coordinatorLayout.getWindowToken(), 0);

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                final String userName = inputName.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }



                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(getApplicationContext(), "Enter name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(CreateAccountActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                        if (!task.isSuccessful()) {
                            Toast.makeText(CreateAccountActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            FirebaseUser user = auth.getCurrentUser();
                            Log.d(TAG, "onComplete: "+user.getUid());
                            insertData(user.getUid(),userName);
                            startActivity(new Intent(CreateAccountActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateAccountActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);


        View bottomSheet = findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                Log.d(TAG, "onStateChanged: "+newState);

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if(slideOffset == 0.0){
                    layout_MainMenu.getForeground().setAlpha(0);
                }
                Log.d(TAG, "slideOffset: "+slideOffset);
                // React to dragging events
            }
        });





        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get a reference to our posts
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("server/saving-data/fireblog/posts");

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                System.out.println(post);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });




        ArrayList<String> items = new ArrayList<>();
        items.add("Item 16");
        items.add("Item 2");
        items.add("Item 3");
        items.add("Item 4");
        items.add("Item 5");
        items.add("Item 6");

        mAdapter = new ItemAdapter(items, this);
        recyclerView.setAdapter(mAdapter);

        btnAddBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(coordinatorLayout.getWindowToken(), 0);
                layout_MainMenu.getForeground().setAlpha(100);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

    }

    public void insertData(String uid, String name){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users/"+uid);

        myRef.child("name").setValue(name);
        myRef.child("bus_line").setValue(name);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            layout_MainMenu.getForeground().setAlpha(0);
            Log.d(TAG, "onBackPressed:1 "+behavior.getState());
        }else{
            super.onBackPressed();
        }
    }


//    for button sheet click
    @Override
    public void onItemClick(String item) {

        Snackbar.make(coordinatorLayout,item + " is selected", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }
}
