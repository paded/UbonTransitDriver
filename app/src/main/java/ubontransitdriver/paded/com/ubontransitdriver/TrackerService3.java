package ubontransitdriver.paded.com.ubontransitdriver;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.Manifest;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TrackerService3 extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    FusedLocationProviderClient mFusedLocationClient;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    String TAG = "trackService";
    private String user_id;
    private String bus_id;
    private String destination;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("dddd", "onStartCommand: ");
       user_id = intent.getStringExtra("user_id");
       bus_id = intent.getStringExtra("bus_id");
       destination = intent.getStringExtra("destination");
        Log.d(TAG, "onStartCommand: "+user_id+" "+bus_id);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (mGoogleApiClient != null && mFusedLocationClient != null) {
            requestLocationUpdates();
        } else {
            buildGoogleApiClient();
        }
        return START_STICKY;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFusedLocationClient != null) {
            Log.d(TAG, "Service Stop");
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    public void requestLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000); // two minute interval
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
    }

    LocationCallback mLocationCallback = new LocationCallback(){

        @Override
        public void onLocationResult(LocationResult locationResult) {
            final String path ="active_bus/"+bus_id+"/"+user_id;
            for (Location location : locationResult.getLocations()) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
                ref.child("latitude").setValue(location.getLatitude());
                ref.child("longitude").setValue(location.getLongitude());

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());

                ref.child("time").setValue(formattedDate);

                ref.child("destination").setValue(destination);
//                Date currentTime = Calendar.getInstance().getTime();
//                Log.d(TAG, "Location: " + location.getLatitude() + " " + location.getLongitude()+" "+location.getTime()+" "+currentTime);

            }
        };

    };

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
