package tusu.develop.com.bmpweather;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tusu.develop.com.bmpweather.Adapter.DailyAdapter;
import tusu.develop.com.bmpweather.Adapter.HourAdapter;
import tusu.develop.com.bmpweather.Api.WUnderGroundsAPI;
import tusu.develop.com.bmpweather.UnderGrounds_f_weather.UnForecast;
import tusu.develop.com.bmpweather.WnderGroundsHourWeather.WnderGroundsHourWeather;

import static tusu.develop.com.bmpweather.MainActivity.WUnderGrounds_BASE_URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class Next_fragment extends Fragment {


    private static final String TAG = "Next_fragment";
    private FusedLocationProviderClient fusedLocationProviderClient;
    private RecyclerView recyclerView;
    private DailyAdapter dailyAdapter;
    private WUnderGroundsAPI wUnderGroundsAPI;


    public Next_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_next, container, false);

        recyclerView=view.findViewById(R.id.recyclerView);

        getdeviceLocations();

        return view;
    }

    private void getdeviceLocations() {
        Log.d(TAG, "getDeviceLocation: getting the Device Current Locations");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        try {


            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }

            Task location = fusedLocationProviderClient.getLastLocation();

            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {

                        Log.d(TAG, "onComplete: found Locations");
                        Location currentLocation = (Location) task.getResult();


                        //getLatLon(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()));

                        //UnderGrounds Hourly Weather

                        String wUnderGround_Forecast_url="api/8ffec187b0d5a8ad/forecast10day/q/"+currentLocation.getLatitude()+","+currentLocation.getLongitude()+".json";

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
                                    Toast.makeText(getContext(), "Data Founds", Toast.LENGTH_SHORT).show();
                                    UnForecast underGroundsForecastWeather=response.body();

                                    dailyAdapter=new DailyAdapter(getContext(),underGroundsForecastWeather.getForecast().getTxtForecast().getForecastday(),underGroundsForecastWeather.getForecast().getSimpleforecast().getForecastday());

                                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
                                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    recyclerView.setAdapter(dailyAdapter);

                                }
                            }

                            @Override
                            public void onFailure(Call<UnForecast> call, Throwable t) {

                            }
                        });

                    }


                }

            });


        }catch (Exception e){

        }
    }

}
