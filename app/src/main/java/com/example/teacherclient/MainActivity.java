package com.example.teacherclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.TextView;

import com.example.teacherclient.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.litepal.LitePal;


public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private BottomNavigationView navigationView;
    private ViewPager viewPager;
    private TextView topText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LitePal.getDatabase();
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        navigationView = (BottomNavigationView)findViewById(R.id.bottom_menu);
        viewPager = (ViewPager)findViewById(R.id.main_viewpager);
        topText = (TextView) findViewById(R.id.top_text);


        BottomAdapter adapter = new BottomAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new TalkFragment());
        adapter.addFragment(new PersonalFragment());
        viewPager.setAdapter(adapter);


        /**
         * TODO change view
         */
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.bottom_home:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.talk_area:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.personal_info:
                        viewPager.setCurrentItem(2);
                        break;
                }

                return false;
            }
        });


        /**
         * TODO do something when page changed
         */
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                navigationView.getMenu().getItem(position).setChecked(true);
                topText.setText(navigationView.getMenu().getItem(position).getTitle());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.tool_bar_menu,menu);
        return true;
    }
}
