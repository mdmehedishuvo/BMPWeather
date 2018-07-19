package tusu.develop.com.bmpweather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SplashScreen extends AppCompatActivity {


    private static final String TAG = "SplashScreen";
    private static final String FINE_LOCATIONS= Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATIONS=Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSIN_REQUEST_CODE=1234;
    private static final float DEFAULT_ZOOM=14f;


    //vars
    private Boolean mLocatiionsPermissionGranted=false;
    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getLocationPermission();

    }

    private void getLocationPermission() {

        Log.d(TAG, "getLocationPermission: getting Locations Permissions");
        String[] permissions={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATIONS)== PackageManager.PERMISSION_GRANTED){

            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),COURSE_LOCATIONS)==PackageManager.PERMISSION_GRANTED){
                mLocatiionsPermissionGranted=true;
            }else {
                ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSIN_REQUEST_CODE);
            }

        }else {

            ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSIN_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d(TAG, "onRequestPermissionsResult: Called");
        mLocatiionsPermissionGranted=false;

        switch (requestCode){
            case LOCATION_PERMISSIN_REQUEST_CODE:{
                if (grantResults.length>0){
                    for (int i=0;i<grantResults.length;i++){
                        if (grantResults[i] !=PackageManager.PERMISSION_GRANTED){
                            mLocatiionsPermissionGranted=false;
                            Log.d(TAG, "onRequestPermissionsResult: Permission Faield");
                            return;
                        }
                    }

                    Log.d(TAG, "onRequestPermissionsResult: Permission Granred");
                    mLocatiionsPermissionGranted=true;
                }
            }
        }

    }

    private void getDeviceLocation(){

        Log.d(TAG, "getDeviceLocation: getting the Device Current Locations");

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

        try {

            if (mLocatiionsPermissionGranted){

                Task location=fusedLocationProviderClient.getLastLocation();

                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){

                            Log.d(TAG, "onComplete: found Locations");
                            Location currentLocation= (Location) task.getResult();
                        }else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(SplashScreen.this, "unable to get current locations", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }


        }catch (SecurityException n){
            Log.d(TAG, "getDeviceLocation: "+n.getMessage());
        }

    }
}
