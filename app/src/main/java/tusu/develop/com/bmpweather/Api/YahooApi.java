package tusu.develop.com.bmpweather.Api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import tusu.develop.com.bmpweather.YahooWeather.YahooWeather;

public interface YahooApi {

    @GET()
    Call<YahooWeather> yahoowearher(@Url String yahooUrl);

}
