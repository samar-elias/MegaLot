package com.hudhud.megalot.Views.Main.Home.PreviousWins;

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
import com.hudhud.megalot.Views.Main.MainActivity;
import com.hudhud.megalot.R;

public class PreviousWinsFragment extends Fragment {

    NavController navController;
    MainActivity mainActivity;
    MaterialButton viewMyTicket;

    public PreviousWinsFragment() {
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
        return inflater.inflate(R.layout.fragment_previous_wins, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
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
        viewMyTicket = view.findViewById(R.id.view_my_ticket);
    }

    private void onClick(){
        viewMyTicket.setOnClickListener(view -> navController.navigate(PreviousWinsFragmentDirections.actionPreviousWinsFragmentToMyTicketsFragment()));
    }
}