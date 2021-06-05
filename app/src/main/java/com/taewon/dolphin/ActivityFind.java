package com.taewon.dolphin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class ActivityFind extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);


        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        pager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(pager);
        pager.setAdapter(new FindPageAdapter(getSupportFragmentManager(), this));
    }
}
