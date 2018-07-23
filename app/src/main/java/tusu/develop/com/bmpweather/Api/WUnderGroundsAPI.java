package tusu.develop.com.bmpweather.Api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import tusu.develop.com.bmpweather.UnderGrounds_f_weather.UnForecast;
import tusu.develop.com.bmpweather.WnderGroundsHourWeather.WnderGroundsHourWeather;
import tusu.develop.com.bmpweather.WunderGroundsSky.WunderGroundsSky;
import tusu.develop.com.bmpweather.WundergroundWeather.UnderGroundsWeather;

public interface WUnderGroundsAPI {

    @GET()
    Call<UnderGroundsWeather> underGroundsWeather(@Url String underGroundsUrl);

    @GET()
    Call<UnForecast> underGroundsForestWeather(@Url String underGroundsForestUrl);

    @GET()
    Call<WnderGroundsHourWeather> underGroundsHourWeather(@Url String UnderGrundsHourWeather);

    @GET()
    Call<WunderGroundsSky> wunderGroundsSky(@Url String UnderGroundsSkyUrl);

}
