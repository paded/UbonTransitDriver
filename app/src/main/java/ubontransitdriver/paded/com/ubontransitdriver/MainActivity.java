package ubontransitdriver.paded.com.ubontransitdriver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h6ah4i.android.materialshadowninepatch.MaterialShadowContainerView;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private ImageButton btn_profile;
    private String TAG = "mainAC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        btn_profile = (ImageButton)findViewById(R.id.btn_profile);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        // Attach a listener to read the data at our posts reference
        database.child("users/"+user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: "+dataSnapshot.child("name").getValue(String.class));
                Log.d(TAG, "onDataChange: "+dataSnapshot.child("bus_id").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });



        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileSettingActivity.class);
                startActivity(intent);
            }
        });





//
//        MaterialShadowContainerView shadowView =
//                (MaterialShadowContainerView) findViewById(R.id.shadow_item_container);
//
//        float density = getResources().getDisplayMetrics().density;
//
//        shadowView.setShadowTranslationZ(density * 2.0f); // 2.0 dp
//        shadowView.setShadowElevation(density * 4.0f); // 4.0 dp


    }
}
