package com.example.chitchatapp.Adapters;

import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.chitchatapp.Fragments.ChatFragment;
import com.example.chitchatapp.Fragments.ProfileFragment;
import com.example.chitchatapp.Fragments.UserFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {




    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ChatFragment();
            case 1:
                return new UserFragment();

        }
        return new ProfileFragment();



    }

    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:
                return "Chats";
            case 1:
                return "Users";
        }
        return "Profile";



    }




}
