package tusu.develop.com.bmpweather.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import tusu.develop.com.bmpweather.Another_fragment;
import tusu.develop.com.bmpweather.Daily_Fragment;
import tusu.develop.com.bmpweather.Next_fragment;
import tusu.develop.com.bmpweather.TodayWeatherFragment;
import tusu.develop.com.bmpweather.ZilaWeatherFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;


    public ViewPagerAdapter(Context context,FragmentManager fm) {
        super(fm);
        this.context=context;

    }

    @Override
    public Fragment getItem(int position) {

        if (position==0){
            return new Daily_Fragment();
        }else if (position==1){
            return new Next_fragment();
        }else if (position==2){
            return new Another_fragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
