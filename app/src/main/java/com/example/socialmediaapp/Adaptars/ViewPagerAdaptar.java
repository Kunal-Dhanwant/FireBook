package com.example.socialmediaapp.Adaptars;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.socialmediaapp.Fragments.Notification2Fragment;
import com.example.socialmediaapp.Fragments.RequestFragment;

public class ViewPagerAdaptar extends FragmentPagerAdapter {
    public ViewPagerAdaptar(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {


        switch (position){

            case 0:
                return  new Notification2Fragment();
            case 1:
                return  new RequestFragment();

            default:
                return new Notification2Fragment();
        }

    }

    @Override
    public int getCount() {
        return 2;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        String title = null;

        switch (position){
            case 0:
                title = "NOTIFICATION";
                return title;
            case 1:
                title = " REQUESTS";
                return title;


        }
        return title;
    }
}
