package tusu.develop.com.bmpweather;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;

import tusu.develop.com.bmpweather.Api.WUnderGroundsAPI;
import tusu.develop.com.bmpweather.ShareperencesData.UserPreferences;

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
    private UserPreferences userPreferences;


    private WUnderGroundsAPI wUnderGroundsAPI;

    public static final String YAHOO_BASE_URL="https://query.yahooapis.com/v1/public/";
    public static final String WUnderGrounds_BASE_URL="http://api.wunderground.com/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashScreen.this,MainActivity.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, 5000);

    }
}
