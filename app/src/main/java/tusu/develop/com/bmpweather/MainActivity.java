package tusu.develop.com.bmpweather;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tusu.develop.com.bmpweather.Adapter.PlaceAutocompleteAdapter;
import tusu.develop.com.bmpweather.Adapter.ViewPagerAdapter;
import tusu.develop.com.bmpweather.Api.WUnderGroundsAPI;
import tusu.develop.com.bmpweather.ShareperencesData.UserPreferences;
import tusu.develop.com.bmpweather.UnderGrounds_f_weather.UnForecast;
import tusu.develop.com.bmpweather.WunderGroundsSky.WunderGroundsSky;
import tusu.develop.com.bmpweather.WundergroundWeather.UnderGroundsWeather;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private Toolbar toolbar;
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private TextView txtPlaceName,txtWeatherCondition,txtCurrentTemp,txtMin,txtMax,txtTodayDay;
    private UserPreferences userPreferences;
    private TextView txtSunrise,txtSunset;
    private ImageView imageCurrent;


    private static final String TAG = "MainActivity";
    private static final String FINE_LOCATIONS= Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATIONS=Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSIN_REQUEST_CODE=1234;
    private AutoCompleteTextView autoCompleteText;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(23.0676199,90.2708762), new LatLng(23.750854, 90.393527));



    //vars
    private Boolean mLocatiionsPermissionGranted=false;
    private FusedLocationProviderClient fusedLocationProviderClient;


    private WUnderGroundsAPI wUnderGroundsAPI;

    private String condition;


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

        txtSunrise=findViewById(R.id.txtSunrise);
        txtSunset=findViewById(R.id.txtSunSet);
        imageCurrent=findViewById(R.id.imageCurrent);


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
        autoCompleteText =  findViewById(R.id.input_search);
        getDeviceLocation();
        init1();



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
        String wUnderGround_Sky_url="api/8ffec187b0d5a8ad/astronomy/q/"+latLng.latitude+","+latLng.longitude+".json";



        //WunderGroundsSky Sections;

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(WUnderGrounds_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WUnderGroundsAPI wUnderGroundsAPISky=retrofit.create(WUnderGroundsAPI.class);
        Call<WunderGroundsSky> underGroundsSky=wUnderGroundsAPISky.wunderGroundsSky(wUnderGround_Sky_url);
        underGroundsSky.enqueue(new Callback<WunderGroundsSky>() {
            @Override
            public void onResponse(Call<WunderGroundsSky> call, Response<WunderGroundsSky> response) {
                if (response.code()==200){
                    WunderGroundsSky wunderGroundsSky=response.body();

                    String sunrise=wunderGroundsSky.getSunPhase().getSunrise().getHour()+":"+wunderGroundsSky.getSunPhase().getSunrise().getMinute()+" am";

                    int sunsetHour= Integer.parseInt((wunderGroundsSky.getMoonPhase().getSunset().getHour()))-12;
                    String sunset=String.valueOf(sunsetHour)+":"+wunderGroundsSky.getMoonPhase().getSunset().getMinute()+" pm";



                    txtSunrise.setText("সূর্যোদয়: "+sunrise);
                    txtSunset.setText("   সূর্যাস্ত: "+sunset);

                }
            }

            @Override
            public void onFailure(Call<WunderGroundsSky> call, Throwable t) {

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

                    String imageurl=underGroundsWeather.getCurrentObservation().getIconUrl();

                    if (underGroundsWeather.getCurrentObservation().getWeather().toString()=="Overcast"){
                        imageCurrent.setBackgroundResource((R.drawable.cloudy));
                    }else {
                        Picasso.get().load(imageurl).into(imageCurrent);
                    }


                    txtPlaceName.setText(underGroundsWeather.getCurrentObservation().getDisplayLocation().getFull().toString()+" ");
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

                    txtMin.setText("সর্বোনিম্ন:  "+underGroundsForecastWeather.getForecast().getSimpleforecast().getForecastday().get(0).getLow().getCelsius()+"°c");
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
            case R.id.itemAbout:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void init1() {
        Log.d(TAG, "init: initializing");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        autoCompleteText.setOnItemClickListener(mAutocompleteClickListener);

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                LAT_LNG_BOUNDS, null);

        autoCompleteText.setAdapter(mPlaceAutocompleteAdapter);

        autoCompleteText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {

                    //execute our method for searching
                    geoLocate();
                }

                return false;

            }
        });

    }

    private void geoLocate() {
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = autoCompleteText.getText().toString();

        Geocoder geocoder = new Geocoder(MainActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            //getLatLon(new LatLng(address.getLatitude(),address.getLongitude()));

        }
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);

            try{

                String name=place.getName().toString();
                Toast.makeText(MainActivity.this, "দুঃক্ষিত আপনি লোকেশন Search দিয়েছেন "+name+" কিন্তু Google আপনার Search কৃত জায়গাটি ঠিক নামে দেখাতে পারছে না। একটু ভাল ভাবে খেয়াল করলে দেখবেন আপনার লোকেশটিই দেখাচ্ছে।  'Thanks ", Toast.LENGTH_SHORT).show();

                getLatLon(place.getLatLng());

            }catch (NullPointerException e){
                Log.e(TAG, "onResult: NullPointerException: " + e.getMessage() );
            }


            places.release();
        }
    };
}
