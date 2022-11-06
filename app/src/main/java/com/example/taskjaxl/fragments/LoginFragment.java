package com.example.taskjaxl.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.taskjaxl.APIRepository;
import com.example.taskjaxl.MainActivity;
import com.example.taskjaxl.R;
import com.example.taskjaxl.databinding.FragmentLoginBinding;
import com.example.taskjaxl.model.Email;
import com.example.taskjaxl.model.OTP;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private APIRepository apiRepository;
    private String email;

    private boolean validOTP = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        apiRepository = APIRepository.getInstance();

        binding.logInBtn.setOnClickListener(v -> {
            email = binding.emailFieldLogin.getEditText().getText().toString();

            apiRepository.getApiService().login(new Email(email)).enqueue(new Callback<Email>() {
                @Override
                public void onResponse(Call<Email> call, Response<Email> response) {
                    if (response.isSuccessful()) {
                        binding.otpFieldLogin.setVisibility(View.VISIBLE);
                        binding.logInBtn.setVisibility(View.GONE);
                        binding.verifyLogin.setVisibility(View.VISIBLE);
                        binding.expireTimeFieldLogin.setVisibility(View.VISIBLE);
                        startCountDown();
                    } else {
                        Toast.makeText(requireContext(), "Failed to request " + response.message(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Email> call, Throwable t) {
                    Toast.makeText(requireContext(), "Failed to login" + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        binding.verifyLogin.setOnClickListener(v -> {
            String otp = binding.otpFieldLogin.getEditText().getText().toString();

            if (validOTP) {
                apiRepository.getApiService().verifyOtp(new OTP(otp)).enqueue(new Callback<OTP>() {
                    @Override
                    public void onResponse(Call<OTP> call, Response<OTP> response) {
                        if (response.isSuccessful()) {
                            MainActivity.loginEmail = email;
                            editor.putString(MainActivity.LOGINCRED, email);
                            editor.apply();
                            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment);
                        } else {
                            Toast.makeText(requireContext(), "Wrong otp " + response.message(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<OTP> call, Throwable t) {
                        Toast.makeText(requireContext(), "Failed to verify otp " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(requireContext(), "Request new OTP", Toast.LENGTH_LONG).show();
            }
        });

        binding.requestOtpLogin.setOnClickListener(v -> {
            binding.requestOtpLogin.setVisibility(View.GONE);
            startCountDown();
            apiRepository.getApiService().requestAnotherOtp(new Email(email)).enqueue(new Callback<Email>() {
                @Override
                public void onResponse(Call<Email> call, Response<Email> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(requireContext(), "Sent request", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Email> call, Throwable t) {

                }
            });
        });
    }

    private void startCountDown() {
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                validOTP = true;
                long sec = (millisUntilFinished / 1000) % 60;

                binding.expireTimeFieldLogin.setText("OTP expires in "+sec+" sec");
            }

            public void onFinish() {
                validOTP = false;
                binding.expireTimeFieldLogin.setText("Request new otp");
                binding.requestOtpLogin.setVisibility(View.VISIBLE);
            }

        }.start();
    }
}