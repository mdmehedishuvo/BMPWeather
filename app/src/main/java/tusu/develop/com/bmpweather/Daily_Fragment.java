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
import tusu.develop.com.bmpweather.Adapter.HourAdapter;
import tusu.develop.com.bmpweather.Api.WUnderGroundsAPI;
import tusu.develop.com.bmpweather.WnderGroundsHourWeather.WnderGroundsHourWeather;

import static tusu.develop.com.bmpweather.MainActivity.WUnderGrounds_BASE_URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class Daily_Fragment extends Fragment {

    private static final String TAG = "Daily_Fragment";

    private FusedLocationProviderClient fusedLocationProviderClient;

    private HourAdapter hourAdapter;
    private RecyclerView recyclerView;

    public Daily_Fragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_daily_, container, false);

        //initialize all

        recyclerView=view.findViewById(R.id.recyclerView);


        getDeviceLocation();
        return view;
    }


    private void getDeviceLocation() {

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

                        String wUnderGround_HOUR_Forecast_url = "api/8ffec187b0d5a8ad/hourly/q/" + currentLocation.getLatitude() + "," + currentLocation.getLongitude() + ".json";

                        Retrofit retrofitOpenWeather = new Retrofit.Builder()
                                .baseUrl(WUnderGrounds_BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        WUnderGroundsAPI wUnderGroundsAPIHour = retrofitOpenWeather.create(WUnderGroundsAPI.class);
                        Call<WnderGroundsHourWeather> wnderGroundsHourWeatherCall = wUnderGroundsAPIHour.underGroundsHourWeather(wUnderGround_HOUR_Forecast_url);
                        wnderGroundsHourWeatherCall.enqueue(new Callback<WnderGroundsHourWeather>() {
                            @Override
                            public void onResponse(Call<WnderGroundsHourWeather> call, Response<WnderGroundsHourWeather> response) {
                                if (response.code() == 200) {
                                    WnderGroundsHourWeather wnderGroundsHourWeather = response.body();

                                    hourAdapter=new HourAdapter(wnderGroundsHourWeather.getHourlyForecast(),getContext());

                                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
                                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    recyclerView.setAdapter(hourAdapter);


                                }
                            }

                            @Override
                            public void onFailure(Call<WnderGroundsHourWeather> call, Throwable t) {
                                Toast.makeText(getContext(), "sdhsuhdowhsodhwidhwodhwodhswuodwbd dduo", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }


                }

            });


        }catch (Exception e){

        }
    }}
