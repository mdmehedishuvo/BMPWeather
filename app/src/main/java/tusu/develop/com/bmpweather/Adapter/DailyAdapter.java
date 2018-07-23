package tusu.develop.com.bmpweather.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import tusu.develop.com.bmpweather.R;
import tusu.develop.com.bmpweather.UnderGrounds_f_weather.Forecastday;
import tusu.develop.com.bmpweather.UnderGrounds_f_weather.Forecastday_;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.MyViewHolder>{

    private Context context;
    List<Forecastday> forecastdays;
    List<Forecastday_> forecastday_s;
    Forecastday forecastday;
    Forecastday_ forecastday_;

    public DailyAdapter(Context context, List<Forecastday> forecastdays, List<Forecastday_> forecastday_s) {
        this.context = context;
        this.forecastdays = forecastdays;
        this.forecastday_s = forecastday_s;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.day_fragment,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        forecastday=forecastdays.get(position);
        forecastday_=forecastday_s.get(position);

        String day=forecastday_.getDate().getWeekday();
        holder.txtDay.setText(day);


        String date=forecastday_.getDate().getDay()+" "+forecastday_.getDate().getMonthname()+" "
                +forecastday_.getDate().getYear().toString();

        holder.txtDate.setText(date);
        holder.txtCondition.setText(forecastday_.getConditions());
        holder.txtMin.setText("সর্বোনিম্ন:  "+forecastday_.getLow().getCelsius().toString()+"°c");
        holder.txtMax.setText("সর্বোচ্চ:    "+forecastday_.getHigh().getCelsius().toString()+"°c");
        holder.txtDetails1.setText(forecastday.getFcttext().toString());
        holder.txtDetails2.setText(forecastday.getFcttextMetric().toString());

        String  imageurl=forecastday_.getIconUrl();
        Picasso.get().load(imageurl).into(holder.imageDaily);

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtDay;
        TextView txtDate;
        TextView txtCondition;
        TextView txtMin;
        TextView txtMax;
        TextView txtDetails1;
        TextView txtDetails2;
        ImageView imageDaily;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDay=itemView.findViewById(R.id.txtDailyDay);
            txtDate=itemView.findViewById(R.id.txtDailyDate);
            txtCondition=itemView.findViewById(R.id.txtDailyCondition);
            txtMin=itemView.findViewById(R.id.txtDailyLow);
            txtMax=itemView.findViewById(R.id.txtDailyHigh);
            txtDetails1=itemView.findViewById(R.id.txtDailyDetails1);
            txtDetails2=itemView.findViewById(R.id.txtDailyDetails2);
            imageDaily=itemView.findViewById(R.id.imageDaily);
        }
    }


}
