package tusu.develop.com.bmpweather;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;

public class SplashScreen extends AppCompatActivity {


    private static final String TAG = "SplashScreen";
    private static final String FINE_LOCATIONS= Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATIONS=Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSIN_REQUEST_CODE=1234;
    private static final float DEFAULT_ZOOM=14f;


    //vars
    private Boolean mLocatiionsPermissionGranted=false;
    private GoogleMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }
}
