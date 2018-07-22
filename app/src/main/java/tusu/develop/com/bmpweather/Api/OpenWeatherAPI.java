package tusu.develop.com.bmpweather.Api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import tusu.develop.com.bmpweather.OpenWeather.OpenWeatherMain;

public interface OpenWeatherAPI {

    @GET()
    Call<OpenWeatherMain> openWeatherMain(@Url String url);
}
