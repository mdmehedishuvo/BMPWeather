package tusu.develop.com.bmpweather.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import tusu.develop.com.bmpweather.R;
import tusu.develop.com.bmpweather.WnderGroundsHourWeather.HourlyForecast;
import tusu.develop.com.bmpweather.WnderGroundsHourWeather.WnderGroundsHourWeather;

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.MYViewHolder>{

    List<HourlyForecast> wnderGroundsHourWeathers;
    private Context context;
    private HourlyForecast hourWeather;

    public HourAdapter(List<HourlyForecast> wnderGroundsHourWeathers, Context context) {
        this.wnderGroundsHourWeathers = wnderGroundsHourWeathers;
        this.context = context;
    }


    @NonNull
    @Override
    public MYViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.hour_fragment,parent,false);
        return new MYViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MYViewHolder holder, int position) {

        hourWeather=wnderGroundsHourWeathers.get(position);

        String hour=hourWeather.getFCTTIME().getMonthName()+" "+hourWeather.getFCTTIME().getMday()+" "+hourWeather.getFCTTIME().getCivil();
        holder.txtHour.setText(hour);

        String temp=("তাপমাত্রা: "+hourWeather.getTemp().getMetric()+"°c");
        holder.txtTemp.setText(temp);

        String condition=("অবস্তা: "+hourWeather.getCondition().toString()+" ");
        holder.txtCondition.setText(condition);

        String dewPoint=("DEW POINT: "+hourWeather.getDewpoint().getMetric().toString()+"°");
        holder.txtDEWPOINT.setText(dewPoint);


        int hourfctcode= Integer.parseInt(hourWeather.getFctcode());
        if (hourfctcode==1){
            String urlClear="https://raw.githubusercontent.com/manifestinteractive/weather-underground-icons/HEAD/dist/icons/black/png/256x256/clear.png";
            Picasso.get().load(urlClear).into(holder.imageHour);
        }else if (hourfctcode==2){
            String urlClear="https://raw.githubusercontent.com/manifestinteractive/weather-underground-icons/HEAD/dist/icons/black/png/256x256/partlycloudy.png";
            Picasso.get().load(urlClear).into(holder.imageHour);
        }else if (hourfctcode==3){
            String urlClear="https://raw.githubusercontent.com/manifestinteractive/weather-underground-icons/HEAD/dist/icons/black/png/256x256/mostlycloudy.png";
            Picasso.get().load(urlClear).into(holder.imageHour);
        }else if (hourfctcode==4){
            String urlClear="https://raw.githubusercontent.com/manifestinteractive/weather-underground-icons/HEAD/dist/icons/black/png/256x256/cloudy.png";
            Picasso.get().load(urlClear).into(holder.imageHour);
        }else if (hourfctcode==5){
            String urlClear="https://raw.githubusercontent.com/manifestinteractive/weather-underground-icons/HEAD/dist/icons/black/png/256x256/hazy.png";
            Picasso.get().load(urlClear).into(holder.imageHour);
        }else if (hourfctcode==6){
            String urlClear="https://raw.githubusercontent.com/manifestinteractive/weather-underground-icons/HEAD/dist/icons/black/png/256x256/fog.png";
            Picasso.get().load(urlClear).into(holder.imageHour);
        }else if (hourfctcode==7){
            String urlClear="https://raw.githubusercontent.com/manifestinteractive/weather-underground-icons/HEAD/dist/icons/black/png/256x256/clear.png";
            Picasso.get().load(urlClear).into(holder.imageHour);
        }else if (hourfctcode==8){
            String urlClear="https://raw.githubusercontent.com/manifestinteractive/weather-underground-icons/HEAD/dist/icons/black/png/256x256/clear.png";
            Picasso.get().load(urlClear).into(holder.imageHour);
        }else if (hourfctcode==9){
            String urlClear="https://raw.githubusercontent.com/manifestinteractive/weather-underground-icons/HEAD/dist/icons/black/png/256x256/clear.png";
            Picasso.get().load(urlClear).into(holder.imageHour);
        }else if (hourfctcode==10){
            String urlClear="https://raw.githubusercontent.com/manifestinteractive/weather-underground-icons/HEAD/dist/icons/black/png/256x256/chancesnow.png";
            Picasso.get().load(urlClear).into(holder.imageHour);
        }else if (hourfctcode==11){
            String urlClear="https://raw.githubusercontent.com/manifestinteractive/weather-underground-icons/HEAD/dist/icons/black/png/256x256/chancesnow.png";
            Picasso.get().load(urlClear).into(holder.imageHour);
        }else if (hourfctcode==12){
            String urlClear="https://raw.githubusercontent.com/manifestinteractive/weather-underground-icons/HEAD/dist/icons/black/png/256x256/chancerain.png";
            Picasso.get().load(urlClear).into(holder.imageHour);
        }else if (hourfctcode==13){
            String urlClear="https://raw.githubusercontent.com/manifestinteractive/weather-underground-icons/HEAD/dist/icons/black/png/256x256/rain.png";
            Picasso.get().load(urlClear).into(holder.imageHour);
        }else if (hourfctcode==14){
            String urlClear="https://raw.githubusercontent.com/manifestinteractive/weather-underground-icons/HEAD/dist/icons/black/png/256x256/chancetstorms.png";
            Picasso.get().load(urlClear).into(holder.imageHour);
        }else if (hourfctcode==15){
            String urlClear="https://raw.githubusercontent.com/manifestinteractive/weather-underground-icons/HEAD/dist/icons/black/png/256x256/tstorms.png";
            Picasso.get().load(urlClear).into(holder.imageHour);
        }else if (hourfctcode==16){
            String urlClear="https://raw.githubusercontent.com/manifestinteractive/weather-underground-icons/HEAD/dist/icons/black/png/256x256/flurries.png";
            Picasso.get().load(urlClear).into(holder.imageHour);
        }else if (hourfctcode==17){
            String urlClear="https://raw.githubusercontent.com/manifestinteractive/weather-underground-icons/HEAD/dist/icons/black/png/256x256/clear.png";
            Picasso.get().load(urlClear).into(holder.imageHour);
        }else if (hourfctcode==18){
            String urlClear="https://raw.githubusercontent.com/manifestinteractive/weather-underground-icons/HEAD/dist/icons/black/png/256x256/chancesnow.png";
            Picasso.get().load(urlClear).into(holder.imageHour);
        }else if (hourfctcode==19){
            String urlClear="https://raw.githubusercontent.com/manifestinteractive/weather-underground-icons/HEAD/dist/icons/black/png/256x256/chancesnow.png";
            Picasso.get().load(urlClear).into(holder.imageHour);
        }else if (hourfctcode==20){
            String urlClear="https://raw.githubusercontent.com/manifestinteractive/weather-underground-icons/HEAD/dist/icons/black/png/256x256/snow.png";
            Picasso.get().load(urlClear).into(holder.imageHour);
        }else if (hourfctcode==21){
            String urlClear="https://raw.githubusercontent.com/manifestinteractive/weather-underground-icons/HEAD/dist/icons/black/png/256x256/snow.png";
            Picasso.get().load(urlClear).into(holder.imageHour);
        }else if (hourfctcode==22){
            String urlClear="https://raw.githubusercontent.com/manifestinteractive/weather-underground-icons/HEAD/dist/icons/black/png/256x256/chanceflurries.png";
            Picasso.get().load(urlClear).into(holder.imageHour);
        }else if (hourfctcode==23){
            String urlClear="https://raw.githubusercontent.com/manifestinteractive/weather-underground-icons/HEAD/dist/icons/black/png/256x256/flurries.png";
            Picasso.get().load(urlClear).into(holder.imageHour);
        }else if (hourfctcode==24){
            String urlClear="https://raw.githubusercontent.com/manifestinteractive/weather-underground-icons/HEAD/dist/icons/black/png/256x256/unknown.png";
            Picasso.get().load(urlClear).into(holder.imageHour);
        }


    }

    @Override
    public int getItemCount() {
        return wnderGroundsHourWeathers.size();
    }
    public class MYViewHolder extends RecyclerView.ViewHolder{

        TextView txtHour;
        TextView txtTemp;
        TextView txtCondition;
        TextView txtDEWPOINT;

        ImageView imageHour;

        public MYViewHolder(@NonNull View itemView) {
            super(itemView);
            txtHour=itemView.findViewById(R.id.txtDailyHour);
            txtTemp=itemView.findViewById(R.id.txtHourTemp);
            txtCondition=itemView.findViewById(R.id.txtHourCondition);
            txtDEWPOINT=itemView.findViewById(R.id.txtHourWind);
            imageHour=itemView.findViewById(R.id.imageHourly);
        }
    }


}
