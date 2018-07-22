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

        String url=hourWeather.getIconUrl();
        Picasso.get().load(url).into(holder.imageHour);


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
