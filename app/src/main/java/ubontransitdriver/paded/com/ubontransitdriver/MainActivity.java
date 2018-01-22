package ubontransitdriver.paded.com.ubontransitdriver;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private ProgressBar progressBar;
    private Button btn_endtrip, btn_endtrip_cancel, btn_active_bus, btn_return,btn_endtrip_yes;
//    private Boolean user_status = true;
    private TextView txt_user_status_on, txt_user_status_off;
    BottomSheetDialog bottomSheetDialog;
    BottomSheetBehavior bottomSheetBehavior;
    private FrameLayout view_cover;
    private boolean active_status = true;
    private static final int PERMISSIONS_REQUEST = 1;

    String bus_id;
    String user_status;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);


        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();



        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btn_profile = (ImageButton)findViewById(R.id.btn_profile);
        btn_endtrip = (Button)findViewById(R.id.btn_endtrip);
        txt_user_status_on = (TextView)findViewById(R.id.txt_user_status_on);
        txt_user_status_off = (TextView)findViewById(R.id.txt_user_status_off);
        btn_active_bus = (Button)findViewById(R.id.btn_active_bus);
        btn_return = (Button)findViewById(R.id.btn_return);

//        view_cover = (FrameLayout) findViewById(R.id.view_cover);

        progressBar.setVisibility(View.VISIBLE);

//        view_cover.getForeground().setAlpha(100);


        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_endtrip, null);
        bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        bottomSheetDialog.setContentView(bottomSheetView);

        bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        btn_endtrip_cancel = (Button)bottomSheetView.findViewById(R.id.btn_endtrip_cancel);
        btn_endtrip_yes = (Button)bottomSheetView.findViewById(R.id.btn_endtrip_yes);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        // Attach a listener to read the data at our posts reference
        database.child("users/"+user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser user = auth.getCurrentUser();
                Log.d(TAG, "onDataChange: "+dataSnapshot.child("name").getValue(String.class));
                Log.d(TAG, "onDataChange: "+dataSnapshot.child("bus_id").getValue(String.class));
                Log.d(TAG, "onDataChange: "+dataSnapshot.child("status").getValue(String.class));
                user_status = dataSnapshot.child("status").getValue(String.class);
                bus_id = dataSnapshot.child("bus_id").getValue(String.class);
                user_id = user.getUid();

                updateUI(user_status);

                if(user_status.equalsIgnoreCase("on")){
                    trackBus(user_id,bus_id);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


//        if(!user_status){
//            txt_user_status.setVisibility(View.GONE);
//        }


        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileSettingActivity.class);
                startActivity(intent);
            }
        });

        btn_endtrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
            }
        });

        btn_endtrip_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                FirebaseUser user = auth.getCurrentUser();
                active_status = false;

                if(bus_id!=null){
                    Intent intent = new Intent(MainActivity.this, TrackerService.class);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("active_bus/"+bus_id);
                    myRef.child(user.getUid()).removeValue();
                    stopService(intent);
                }
//                DatabaseReference myRef = database.getReference("active_bus/"+bus_id+"/"+user_id);
//                myRef.child("lat").setValue(12);
//                myRef.child("lng").setValue(12);

                UpdateUserStatus(user.getUid(),active_status);
                bottomSheetDialog.hide();
            }
        });

        btn_endtrip_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.hide();
            }
        });

        btn_active_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                FirebaseUser user = auth.getCurrentUser();
                active_status = true;
                UpdateUserStatus(user.getUid(),active_status);
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

    public void UpdateUserStatus(String user_id, boolean active_status){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users/"+user_id);
        if(!active_status){
            myRef.child("status").setValue("off");
        }else{
            myRef.child("status").setValue("on");
        }

        progressBar.setVisibility(View.GONE);
    }

    public void updateUI(String user_status){
        if(user_status.equalsIgnoreCase("off")){
            btn_active_bus.setVisibility(View.VISIBLE);
            txt_user_status_off.setVisibility(View.VISIBLE);

            btn_endtrip.setVisibility(View.GONE);
            btn_return.setVisibility(View.GONE);
            txt_user_status_on.setVisibility(View.GONE);
        }else{
            btn_active_bus.setVisibility(View.GONE);
            txt_user_status_off.setVisibility(View.GONE);

            btn_endtrip.setVisibility(View.VISIBLE);
            btn_return.setVisibility(View.VISIBLE);
            txt_user_status_on.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
//        view_cover.getForeground().setAlpha(0);
        Log.d(TAG, "updateUI: "+user_status);
    }

    public void trackBus(String user_id,String bus_id){
        // Check GPS is enabled ****
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
            finish();
        }
        // Check location permission is granted - if it is, start
        // the service, otherwise request the permission
        int permission = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            startTrackerService(user_id,bus_id);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("active_bus/"+bus_id+"/"+user_id);
//        myRef.child("lat").setValue(12);
//        myRef.child("lng").setValue(12);



    }

    private void startTrackerService(String user_id,String bus_id) {
        Intent intent = new Intent(this,TrackerService.class);
        intent.putExtra("user_id",user_id);
        intent.putExtra("bus_id",bus_id);
        startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Start the service when the permission is granted
            startTrackerService(user_id,bus_id);
        } else {
            finish();
        }
    }

}
