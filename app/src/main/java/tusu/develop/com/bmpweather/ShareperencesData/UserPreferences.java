package tusu.develop.com.bmpweather.ShareperencesData;

import android.content.Context;
import android.content.SharedPreferences;

import tusu.develop.com.bmpweather.R;

public class UserPreferences  {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    private static final String CurrentPlacename="Current_place";
    private static final String CurrentWeatherName="Weaher_name";
    private static final String Currenttemp="currentTemp";
    private static final String min="min";
    private static final String max="max";
    private static final String todayDay="TodayDay";

    private static final String Hour0="hour0";
    private static final String HourImage0="hourimag0";
    private static final String HourPercent0="hourPercent";
    private static final String HourTemp0="HourTemp";

    private static final String Day0="Day0";
    private static final String Dayimage0="dayImage0";
    private static final String DayMin0="dayMin0";
    private static final String DayMax0="dayMax0";


    private static final String sunset="sunset";
    private static final String sunrise="sunrise";

    private static final String DetailsImage="DetailsImage400";
    private static final String FeelsLike="feelsLike";
    private static final String Humidity="humidity";
    private static final String Visibility="visibility";
    private static final String UvIndex="index";
    private static final String DetailsCondition0="condition1";
    private static final String DetailsCondition1="condition2";

    public UserPreferences(Context context) {

        sharedPreferences=context.getSharedPreferences("WeatherPreferences",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.clear();
    }

    public void setPlaceName(String placename){
        editor.putString(CurrentPlacename,placename);
        editor.commit();
    }

    public String getPlaceName(){
        return sharedPreferences.getString(CurrentPlacename,"Dhaka,Bangladesh");
    }

    public void setCurrentWeatherCondition(String condition){
        editor.putString(CurrentWeatherName,condition);
        editor.commit();
    }

    public String getCurrentWeatherCondition(){
        return sharedPreferences.getString(CurrentWeatherName,"Rain");
    }


    public void setCurrentTemp(String Temp){
        editor.putString(Currenttemp,Temp);
        editor.commit();
    }

    public String getCurrentTemp(){
        return sharedPreferences.getString(Currenttemp,"20°c");
    }


    public void setCurrentMin(String min1){
        editor.putString(min,min1);
        editor.commit();
    }

    public String getCurrentMin(){
        return sharedPreferences.getString(min,"14°c");
    }

    public void setCurrentMax(String max0){
        editor.putString(max,max0);
        editor.commit();
    }

    public String getCurrentMax(){
        return sharedPreferences.getString(max,"25°c");
    }

    public void setTodayDay(String tda){
        editor.putString(todayDay,tda);
        editor.commit();
    }

    public String getTodayDay(){
        return sharedPreferences.getString(todayDay,"Wednesday");
    }

    public void setHour1(String hr0){
        editor.putString(Hour0,hr0);
        editor.commit();
    }

    public String getHour1(){
        return sharedPreferences.getString(Hour0,"11 pm");
    }

    public void setHourImage1(int hourImage0){
        editor.putInt(HourImage0,hourImage0);
        editor.commit();
    }

    public int getHourImage1(){
        return sharedPreferences.getInt(HourImage0, R.drawable.weather_image);
    }

    public void setHourPercent1(String percent){
        editor.putString(HourPercent0,percent);
        editor.commit();
    }

    public String getHourPercent1(){
        return sharedPreferences.getString(HourPercent0," ");
    }

    public void setHourTemp1(String temp1){
        editor.putString(HourTemp0,temp1);
        editor.commit();
    }

    public String getHourTemp1(){
        return sharedPreferences.getString(HourTemp0,"23°c");
    }

     public void setDay0(String day0){
        editor.putString(Day0,day0);
        editor.commit();
    }

    public String getDay0(){
        return sharedPreferences.getString(Day0,"Wednesday");
    }


     public void setDayImage0(int dayImage0){
        editor.putInt(Dayimage0,dayImage0);
        editor.commit();
    }

    public int getDayImage0(){
        return sharedPreferences.getInt(Dayimage0,R.drawable.weather_image);
    }


     public void setDayMin0(String min0){
        editor.putString(DayMin0,min0);
        editor.commit();
    }

    public String getDayMin0(){
        return sharedPreferences.getString(DayMin0,"20°c");
    }


     public void setDayMax0(String max0){
        editor.putString(DayMax0,max0);
        editor.commit();
    }

    public String getDayMax0(){
        return sharedPreferences.getString(DayMax0,"25°c");
    }

     public void setSunset(String sunset2){
        editor.putString(sunset,sunset2);
        editor.commit();
    }

    public String getSunset(){
        return sharedPreferences.getString(sunset,"6.30 pm");
    }

     public void setSunrise(String sunrise2){
        editor.putString(sunrise,sunrise2);
        editor.commit();
    }

    public String getSunrise(){
        return sharedPreferences.getString(sunrise,"5.10 am");
    }

     public void setDetailsImage(int DetailsImage2){
        editor.putInt(DetailsImage,DetailsImage2);
        editor.commit();
    }

    public int getDetailsImage(){
        return sharedPreferences.getInt(DetailsImage,R.drawable.weather_image);
    }

     public void setFeelsLike(String flike){
        editor.putString(FeelsLike,flike);
        editor.commit();
    }

    public String getFeelsLike(){
        return sharedPreferences.getString(FeelsLike,"100°");
    }

     public void setHumidity(String humidity1){
        editor.putString(Humidity,humidity1);
        editor.commit();
    }

    public String getHumidity(){
        return sharedPreferences.getString(Humidity,"75%");
    }

     public void setVisibility(String visibility){
        editor.putString(Visibility,visibility);
        editor.commit();
    }

    public String getVisibility(){
        return sharedPreferences.getString(Visibility,"10 ms");
    }

     public void setUvIndex(String uvIndex){
        editor.putString(UvIndex,uvIndex);
        editor.commit();
    }

    public String getUvIndex(){
        return sharedPreferences.getString(UvIndex,"5 (Moderate)");
    }

     public void setDetailsCondition0(String detailsCondition0){
        editor.putString(DetailsCondition0,detailsCondition0);
        editor.commit();
    }

    public String getDetailsCondition0(){
        return sharedPreferences.getString(DetailsCondition0,"Today - Thunderstorms with a high of 94 °F (34.4 °C). Winds variable.");
    }

     public void setDetailsCondition1(String detailsCondition1){
        editor.putString(DetailsCondition1,detailsCondition1);
        editor.commit();
    }

    public String getDetailsCondition1(){
        return sharedPreferences.getString(DetailsCondition1,"Tonight - Thunderstorms with a 60% chance of precipitation. Winds variable at 2 to 7 mph (3.2 to 11.3 kph). The overnight low will be 81 °F (27.2 °C).");
    }



}
