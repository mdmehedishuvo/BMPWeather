package tusu.develop.com.bmpweather;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tusu.develop.com.bmpweather.Adapter.ViewPagerAdapter;
import tusu.develop.com.bmpweather.Api.OpenWeatherAPI;
import tusu.develop.com.bmpweather.Api.WUnderGroundsAPI;
import tusu.develop.com.bmpweather.Api.YahooApi;
import tusu.develop.com.bmpweather.OpenWeather.OpenWeatherMain;
import tusu.develop.com.bmpweather.ShareperencesData.UserPreferences;
import tusu.develop.com.bmpweather.UnderGrounds_f_weather.UnForecast;
import tusu.develop.com.bmpweather.WnderGroundsHourWeather.WnderGroundsHourWeather;
import tusu.develop.com.bmpweather.WundergroundWeather.UnderGroundsWeather;
import tusu.develop.com.bmpweather.YahooWeather.YahooWeather;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private TextView txtPlaceName,txtWeatherCondition,txtCurrentTemp,txtMin,txtMax,txtTodayDay;
    private UserPreferences userPreferences;


    private static final String TAG = "MainActivity";
    private static final String FINE_LOCATIONS= Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATIONS=Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSIN_REQUEST_CODE=1234;
    private static final float DEFAULT_ZOOM=14f;


    //vars
    private Boolean mLocatiionsPermissionGranted=false;
    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;


    private YahooApi yahooApi;
    private WUnderGroundsAPI wUnderGroundsAPI;

    private String condition;

    String hour0,hour1;

    public static final String YAHOO_BASE_URL="https://query.yahooapis.com/v1/public/";
    public static final String OPEN_WEATHER_BASE_URL="https://api.openweathermap.org/data/2.5/";
    public static final String WUnderGrounds_BASE_URL="http://api.wunderground.com/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        txtPlaceName=findViewById(R.id.txtCurrentPlactName);
        txtWeatherCondition=findViewById(R.id.txtCurrentWeatherCondition);
        txtCurrentTemp=findViewById(R.id.txtCurrentTemp);
        txtMax=findViewById(R.id.txtCurrentMax);
        txtMin=findViewById(R.id.txtCurrentMin);
//        txtTodayDay=findViewById(R.id.txtToday);


        initCollapsingToolbar();
        getLocationPermission();




        viewPager=findViewById(R.id.viewPager);
        adapter=new ViewPagerAdapter(MainActivity.this,getSupportFragmentManager());
        tabLayout=findViewById(R.id.tabs);

        tabLayout.addTab(tabLayout.newTab().setText("প্রতি ঘন্টা"));
        tabLayout.addTab(tabLayout.newTab().setText("পরের দিনগুলো"));
        tabLayout.addTab(tabLayout.newTab().setText("অন্যান্য"));
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        userPreferences=new UserPreferences(MainActivity.this);
        getDeviceLocation();

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

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbarLayout=findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout=findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);




        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            boolean isShow=false;
            int scrollRange=-1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange==-1){
                    scrollRange=appBarLayout.getTotalScrollRange();
                }

                if (scrollRange+verticalOffset==0){
                    collapsingToolbarLayout.setTitle(condition+"        BMP Weather");
                    isShow=true;
                }else if (isShow){
                    collapsingToolbarLayout.setTitle(" ");
                    isShow=false;
                }
            }
        });

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
                            Toast.makeText(MainActivity.this, "unable to get current locations", Toast.LENGTH_SHORT).show();
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
        String wUnderGround_HOUR_Forecast_url="api/8ffec187b0d5a8ad/hourly/q/"+latLng.latitude+","+latLng.longitude+".json";


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
                    Toast.makeText(MainActivity.this, "Data Founds", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "Data Not Found", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this, "Data Found", Toast.LENGTH_SHORT).show();
                    UnderGroundsWeather underGroundsWeather=response.body();



                    String visibility=underGroundsWeather.getCurrentObservation().getVisibilityKm().toString()+"Km";
                    String feelsLike=underGroundsWeather.getCurrentObservation().getFeelslikeC().toString()+"°";
                    String UvIndex=underGroundsWeather.getCurrentObservation().getDewpointC().toString()+" ";

                    txtPlaceName.setText(underGroundsWeather.getCurrentObservation().getDisplayLocation().getCity().toString()+" ");
                    txtWeatherCondition.setText(underGroundsWeather.getCurrentObservation().getWeather().toString()+" ");
                    txtCurrentTemp.setText(underGroundsWeather.getCurrentObservation().getTempC().toString()+"°c");

                    condition=underGroundsWeather.getCurrentObservation().getTempC().toString()+"°c";

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
                    Toast.makeText(MainActivity.this, "Data Founds", Toast.LENGTH_SHORT).show();
                    UnForecast underGroundsForecastWeather=response.body();

                    String DetilsCOnditions=underGroundsForecastWeather.getForecast().getTxtForecast().getForecastday().get(0).getFcttext().toString()+" ";
                    String DetailsConditons1=underGroundsForecastWeather.getForecast().getTxtForecast().getForecastday().get(0).getFcttextMetric().toString()+" ";


                    txtMin.setText("সর্বোনিম্ন: "+underGroundsForecastWeather.getForecast().getSimpleforecast().getForecastday().get(0).getLow().getCelsius()+"°c");
                    txtMax.setText("সর্বোচ্চ:    "+underGroundsForecastWeather.getForecast().getSimpleforecast().getForecastday().get(0).getHigh().getCelsius()+"°c");

                }
            }

            @Override
            public void onFailure(Call<UnForecast> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            
        }
        return super.onOptionsItemSelected(item);
    }
}
