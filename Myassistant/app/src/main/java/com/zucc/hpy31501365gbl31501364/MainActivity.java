package com.zucc.hpy31501365gbl31501364;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.zucc.hpy31501365gbl31501364.JavaBean.Richeng.ClockResult;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager=(ViewPager) findViewById(R.id.mViewPager);
        bottomNavigationView=(BottomNavigationView) findViewById(R.id.mBottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.navigation_dashboard:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.navigation_notifications:
                        mViewPager.setCurrentItem(2);
                        break;
                }
                return false;
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position){
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                }
        });

        final ArrayList<Fragment> fgLists=new ArrayList<>(3);
        fgLists.add(new MyFragment());
        fgLists.add(new MyFragment2());
        fgLists.add(new MyFragment3());

        FragmentPagerAdapter mPagerAdapter=new FragmentPagerAdapter(getSupportFragmentManager()){
            @Override
            public Fragment getItem(int position) {
                return fgLists.get(position);
            }
            @Override
            public int getCount() {
                return fgLists.size();
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        Intent intent = getIntent();
        final String page = intent.getStringExtra("page");
        if(page=="1"){
            mViewPager.setCurrentItem(1);
        }
    }
    public MainActivity() {mainActivity = this;}
    public static MainActivity getMainActivity(){return mainActivity;}
    private static MainActivity mainActivity;
}

