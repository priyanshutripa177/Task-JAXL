package com.example.taskjaxl.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taskjaxl.MainActivity;
import com.example.taskjaxl.R;
import com.example.taskjaxl.databinding.FragmentHomeBinding;
import com.example.taskjaxl.databinding.FragmentLoginBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        binding.emailHome.setText(MainActivity.loginEmail);

        binding.logOutHome.setOnClickListener(v -> {
            editor.putString(MainActivity.LOGINCRED, "");
            editor.apply();

            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_loginFragment);
        });
    }
}