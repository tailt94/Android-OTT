package com.terralogic.alexle.ott.controller;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.terralogic.alexle.ott.R;
import com.terralogic.alexle.ott.model.Device;
import com.terralogic.alexle.ott.model.User;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bindViews();
        setupActionBar();
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void bindViews() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setElevation(0);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DialFragment(), "DIAL");
        adapter.addFragment(new CallLogsFragment(), "CALLLOGS");
        adapter.addFragment(new SettingsFragment(), "SETTINGS");
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList = new ArrayList<>();
        private List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

    /*private void showUserInfo() {
        User user = (User) getIntent().getSerializableExtra(SplashActivity.EXTRA_USER);
        String info = "Token user: " + user.getTokenUser() + "\n"
                + "Token: " + user.getToken() + "\n"
                + "Email: " + user.getEmail() + "\n"
                + "Password: " + user.getPassword() + "\n"
                + "Phone: " + user.getPhoneNumber() + "\n"
                + "First name: " + user.getName().getFirstName() + "\n"
                + "Last name: " + user.getName().getLastName() + "\n"
                + "Sex: " + user.getSex() + "\n"
                + "Birthday: " + user.getBirthday() + "\n"
                + "City: " + user.getCity()  + "\n"
                + "Country: " + user.getCountry() + "\n"
                + "Devices: " + "\n";
        for (Device device : user.getDevices()) {
            info += device.getTokenUser() + "\n"
                    + device.getType() + "\n"
                    + device.getPort() + "\n"
                    + device.getChipID() + "\n\n";
        }
        textContent.setText(info);
    }*/
}
