package tusu.develop.com.bmpweather;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tusu.develop.com.bmpweather.Adapter.DailyAdapter;
import tusu.develop.com.bmpweather.Api.WUnderGroundsAPI;
import tusu.develop.com.bmpweather.UnderGrounds_f_weather.UnForecast;

import static tusu.develop.com.bmpweather.MainActivity.WUnderGrounds_BASE_URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class Another_fragment extends Fragment{

    private static final String TAG = "Another_fragment";
    private FusedLocationProviderClient fusedLocationProviderClient;
    ImageView viewRadar,viewSatellite,view_radarSatellite;
    private GoogleMap map;

    private SupportMapFragment mapFragment;
    private LatLng latLng;

    public Another_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_another, container, false);
        view_radarSatellite=view.findViewById(R.id.viewRadarPlusSatellite);
        viewRadar=view.findViewById(R.id.viewRadar);
        viewSatellite=view.findViewById(R.id.viewSatellite);


        getdeviceLocations();

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map=googleMap;
                    map.addMarker(new MarkerOptions().position(latLng)
                            .title(latLng.latitude+","+latLng.longitude+"   My Locations "));
                    map.getUiSettings().setZoomControlsEnabled(true);
                    map.getUiSettings().setZoomGesturesEnabled(true);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                }
            });
        }

        // R.id.map is a FrameLayout, not a Fragment
        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();


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

                        String url[]={
                        "http://api.wunderground.com/api/8ffec187b0d5a8ad//radar/satellite/image.gif?rad.maxlat="+currentLocation   .getLatitude()+"&rad.maxlon="+currentLocation.getLongitude()+"&rad.minlat=31.596&rad.minlon=-97.388&rad.width=640&rad.height=480&rad.rainsnow=1&rad.reproj.automerc=1&sat.maxlat=47.709&sat.maxlon=-69.263&sat.minlat=31.596&sat.minlon=-97.388&sat.width=640&sat.height=480&sat.key=sat_ir4_bottom&sat.gtt=107&sat.proj=me&sat.timelabel=0",
                                "http://api.wunderground.com/api/8ffec187b0d5a8ad/animatedsatellite/q/MI/Ann_Arbor.gif?basemap=1&timelabel=1&timelabel.y=10&num=5&delay=50",
                                "http://api.wunderground.com/api/8ffec187b0d5a8ad/radar/image.gif?maxlat=47.709&maxlon=-69.263&minlat=31.596&minlon=-97.388&width=640&height=480&rainsnow=1&timelabel=1&timelabel.x=525&timelabel.y=41&reproj.automerc=1"
                        };

                        Glide.with(getContext()).load(url[0]).into(view_radarSatellite);
                        Glide.with(getContext()).load(url[1]).into(viewSatellite);
                        Glide.with(getContext()).load(url[2]).into(viewRadar);

                        latLng=new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());

                    }


                }

            });


        }catch (Exception e){

        }
    }

}
