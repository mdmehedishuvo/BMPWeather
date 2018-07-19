package tusu.develop.com.bmpweather;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import tusu.develop.com.bmpweather.Adapter.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initCollapsingToolbar();
        viewPager=findViewById(R.id.viewPager);
        adapter=new ViewPagerAdapter(MainActivity.this,getSupportFragmentManager());
        tabLayout=findViewById(R.id.tabs);

        tabLayout.addTab(tabLayout.newTab().setText("আজ"));
        tabLayout.addTab(tabLayout.newTab().setText("জিলা"));
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbarLayout=findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout=findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            boolean isShow=false;
            int scrollRange=-1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange==-1){
                    scrollRange=appBarLayout.getTotalScrollRange();
                }

                if (scrollRange+verticalOffset==0){
                    collapsingToolbarLayout.setTitle("Shuvo Khan");
                    isShow=true;
                }else if (isShow){
                    collapsingToolbarLayout.setTitle(" ");
                    isShow=false;
                }
            }
        });

    }
}
