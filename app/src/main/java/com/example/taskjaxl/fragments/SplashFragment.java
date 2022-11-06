package com.example.taskjaxl.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taskjaxl.MainActivity;
import com.example.taskjaxl.R;

public class SplashFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String email = preferences.getString(MainActivity.LOGINCRED, "");

        new Handler().postDelayed(()->{
            if (email.isEmpty()) {
                Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_loginFragment);
            } else {
                MainActivity.loginEmail = email;
                Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_homeFragment);
            }
        }, 2500);

    }
}