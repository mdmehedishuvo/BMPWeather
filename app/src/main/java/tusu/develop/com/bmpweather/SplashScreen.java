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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tusu.develop.com.bmpweather.Api.WUnderGroundsAPI;
import tusu.develop.com.bmpweather.Api.YahooApi;
import tusu.develop.com.bmpweather.ShareperencesData.UserPreferences;
import tusu.develop.com.bmpweather.UnderGrounds_f_weather.UnForecast;
import tusu.develop.com.bmpweather.WundergroundWeather.UnderGroundsWeather;
import tusu.develop.com.bmpweather.YahooWeather.YahooWeather;

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


    private YahooApi yahooApi;
    private WUnderGroundsAPI wUnderGroundsAPI;

    public static final String YAHOO_BASE_URL="https://query.yahooapis.com/v1/public/";
    public static final String WUnderGrounds_BASE_URL="http://api.wunderground.com/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getLocationPermission();
        getDeviceLocation();
        userPreferences=new UserPreferences(SplashScreen.this);

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

                            getLatLon(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()));

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


    private void getLatLon(LatLng latLng){

        String yahoo_url="yql?q=select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"(" + latLng.latitude + "," + latLng.longitude + ")\")&format=json";

        String wUnderGround_url="api/8ffec187b0d5a8ad/conditions/q/"+latLng.latitude+","+latLng.longitude+".json";
        String wUnderGround_Forecast_url="api/8ffec187b0d5a8ad/forecast10day/q/"+latLng.latitude+","+latLng.longitude+".json";

        //Yahoo Sections

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(YAHOO_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        yahooApi=retrofit.create(YahooApi.class);
        Call<YahooWeather> yahooWeatherCall=yahooApi.yahoowearher(yahoo_url);
        yahooWeatherCall.enqueue(new Callback<YahooWeather>() {
            @Override
            public void onResponse(Call<YahooWeather> call, Response<YahooWeather> response) {
                if (response.code()==200){
                    Toast.makeText(SplashScreen.this, "Data Founds", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: Data Founds");

                    YahooWeather yahooWeather=response.body();

                    String sunrise=yahooWeather.getQuery().getResults().getChannel().getAstronomy().getSunrise().toString()+" ";
                    String sunset=yahooWeather.getQuery().getResults().getChannel().getAstronomy().getSunrise().toString()+" ";
                    String humidity=yahooWeather.getQuery().getResults().getChannel().getAtmosphere().getHumidity().toString()+"%";

                    userPreferences.setSunrise(sunrise);
                    userPreferences.setSunset(sunset);
                    userPreferences.setHumidity(humidity);
                }
            }

            @Override
            public void onFailure(Call<YahooWeather> call, Throwable t) {
                Toast.makeText(SplashScreen.this, "Data Not Found", Toast.LENGTH_SHORT).show();
            }
        });



        ///UnderGrounds Weather

        Retrofit retrofitUnderGrounds=new Retrofit.Builder()
                .baseUrl(WUnderGrounds_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        wUnderGroundsAPI=retrofitUnderGrounds.create(WUnderGroundsAPI.class);
        Call<UnderGroundsWeather> underGroundsWeatherCall=wUnderGroundsAPI.underGroundsWeather(wUnderGround_url);
        underGroundsWeatherCall.enqueue(new Callback<UnderGroundsWeather>() {
            @Override
            public void onResponse(Call<UnderGroundsWeather> call, Response<UnderGroundsWeather> response) {
                if (response.code()==200){
                    Toast.makeText(SplashScreen.this, "Data Found", Toast.LENGTH_SHORT).show();
                    UnderGroundsWeather underGroundsWeather=response.body();

                    String placeName=underGroundsWeather.getCurrentObservation().getDisplayLocation().getCity().toString()+" ";
                    String currentTemp=underGroundsWeather.getCurrentObservation().getTempC().toString()+" ";
                    String WeatherCondition=underGroundsWeather.getCurrentObservation().getWeather().toString()+" ";
                    String visibility=underGroundsWeather.getCurrentObservation().getVisibilityKm().toString()+"Km";
                    String feelsLike=underGroundsWeather.getCurrentObservation().getFeelslikeC().toString()+"°";
                    String UvIndex=underGroundsWeather.getCurrentObservation().getDewpointC().toString()+" ";

                    userPreferences.setPlaceName(placeName);
                    userPreferences.setCurrentTemp(currentTemp);
                    userPreferences.setCurrentWeatherCondition(WeatherCondition);
                    userPreferences.setVisibility(visibility);
                    userPreferences.setFeelsLike(feelsLike);
                    userPreferences.setUvIndex(UvIndex);

                }
            }

            @Override
            public void onFailure(Call<UnderGroundsWeather> call, Throwable t) {

            }
        });

        //UnderGrounds Forecast Weather

        Retrofit retrofit_forecats_weather=new Retrofit.Builder()
                .baseUrl(WUnderGrounds_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        wUnderGroundsAPI=retrofit_forecats_weather.create(WUnderGroundsAPI.class);
        Call<UnForecast> underGroundsForecastWeatherCall=wUnderGroundsAPI.underGroundsForestWeather(wUnderGround_Forecast_url);

        underGroundsForecastWeatherCall.enqueue(new Callback<UnForecast>() {
            @Override
            public void onResponse(Call<UnForecast> call, Response<UnForecast> response) {
                if (response.code()==200){
                    Toast.makeText(SplashScreen.this, "Data Founds", Toast.LENGTH_SHORT).show();
                    UnForecast underGroundsForecastWeather=response.body();

                    String min=underGroundsForecastWeather.getForecast().getSimpleforecast().getForecastday().get(0).getLow().getCelsius()+"°c";
                    String max=underGroundsForecastWeather.getForecast().getSimpleforecast().getForecastday().get(0).getHigh().getCelsius()+"°c";
                    String todayDay=underGroundsForecastWeather.getForecast().getSimpleforecast().getForecastday().get(0).getDate().getWeekday().toString()+" ";

                    String DetilsCOnditions=underGroundsForecastWeather.getForecast().getTxtForecast().getForecastday().get(0).getFcttext().toString()+" ";
                    String DetailsConditons1=underGroundsForecastWeather.getForecast().getTxtForecast().getForecastday().get(0).getFcttextMetric().toString()+" ";


                    userPreferences.setCurrentMin(min);
                    userPreferences.setCurrentMax(max);
                    userPreferences.setTodayDay(todayDay);
                    userPreferences.setDetailsCondition0(DetilsCOnditions);
                    userPreferences.setDetailsCondition1(DetailsConditons1);

                }
            }

            @Override
            public void onFailure(Call<UnForecast> call, Throwable t) {

            }
        });






    }

}
