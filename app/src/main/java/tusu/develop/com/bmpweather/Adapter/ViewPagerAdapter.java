package tusu.develop.com.bmpweather.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

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
            return new TodayWeatherFragment();
        }else if (position==1){
            return new ZilaWeatherFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
