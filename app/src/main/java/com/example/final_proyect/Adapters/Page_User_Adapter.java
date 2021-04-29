package com.example.final_proyect.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.final_proyect.Fragments.Consultas_Fragment;
import com.example.final_proyect.Fragments.Noticias_Fragment;
import com.example.final_proyect.Fragments.Perfil_Fragment;

public class Page_User_Adapter extends FragmentStateAdapter {
    public Page_User_Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0:
                return new Noticias_Fragment();
            case 1:
                return new Consultas_Fragment();
            case 2:
                return new Perfil_Fragment();
            case 3:
                return new Perfil_Fragment();
            default:
                return new Noticias_Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
