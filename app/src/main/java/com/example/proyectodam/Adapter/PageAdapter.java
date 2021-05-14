package com.example.proyectodam.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.proyectodam.Fragments.chatsFragment;
import com.example.proyectodam.Fragments.usersFragment;

public class PageAdapter extends FragmentStateAdapter {

    public PageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0:
                return new usersFragment();
            case 1:
                return new chatsFragment();
            default:
                return new usersFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
