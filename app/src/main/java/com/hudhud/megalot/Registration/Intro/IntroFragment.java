package com.hudhud.megalot.Registration.Intro;

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

import com.google.android.material.button.MaterialButton;
import com.hudhud.megalot.R;
import com.hudhud.megalot.Registration.RegistrationActivity;

public class IntroFragment extends Fragment {

    NavController navController;
    RegistrationActivity registrationActivity;
    MaterialButton getStarted;

    public IntroFragment() {
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
        return inflater.inflate(R.layout.fragment_intro, container, false);
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
        getStarted = view.findViewById(R.id.get_started);
    }

    private void onClick(){
        getStarted.setOnClickListener(view -> navController.navigate(IntroFragmentDirections.actionIntroFragmentToSignInFragment()));
    }
}