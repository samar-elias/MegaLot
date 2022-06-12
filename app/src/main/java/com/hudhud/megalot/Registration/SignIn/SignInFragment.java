package com.hudhud.megalot.Registration.SignIn;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.hudhud.megalot.R;
import com.hudhud.megalot.Registration.Intro.IntroFragmentDirections;
import com.hudhud.megalot.Registration.RegistrationActivity;

public class SignInFragment extends Fragment {

    NavController navController;
    RegistrationActivity registrationActivity;
    MaterialButton signIn;
    TextView signUp;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof RegistrationActivity) {
            registrationActivity = (RegistrationActivity) context;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        onClick();
    }

    private void initViews(View view){
        navController = Navigation.findNavController(view);
        signIn = view.findViewById(R.id.sign_in);
        signUp = view.findViewById(R.id.sign_up);
    }

    private void onClick(){
        signUp.setOnClickListener(view -> navController.navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment()));
    }
}