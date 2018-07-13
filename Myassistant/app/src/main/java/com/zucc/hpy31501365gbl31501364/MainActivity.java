package com.zucc.hpy31501365gbl31501364;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.MissingFormatArgumentException;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("个人助手");
        Button tv_back = (Button)findViewById(R.id.tv_back);
        tv_back.setVisibility(View.GONE);
        Button tv_edit = (Button)findViewById(R.id.tv_edit);
        tv_edit.setText("注销");
        mViewPager=(ViewPager) findViewById(R.id.mViewPager);
        bottomNavigationView=(BottomNavigationView) findViewById(R.id.mBottom);

        tv_edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, logon.class);
                startActivity(intent);
            }
        });

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
            } };
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
    }
}

