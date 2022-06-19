package com.hudhud.megalot.Views.Main.Home;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.hudhud.megalot.Views.Main.MainActivity;
import com.hudhud.megalot.R;

public class HomeFragment extends Fragment {

    NavController navController;
    MainActivity mainActivity;
    MaterialButton withdrawBtn;
    MaterialCardView previousWins, myTickets, myWithdrawals;
    LinearLayoutCompat drawResults;

    public HomeFragment() {
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
        return inflater.inflate(R.layout.fragment_home, container, false);
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
        withdrawBtn = view.findViewById(R.id.withdraw);
        previousWins = view.findViewById(R.id.previous_wins);
        drawResults = view.findViewById(R.id.draw_results);
        myTickets = view.findViewById(R.id.my_participations);
        myWithdrawals = view.findViewById(R.id.my_withdrawals);
    }

    private void onClick(){
        withdrawBtn.setOnClickListener(view -> {
            WithdrawBottomSheetDialogFragment withdrawBottomSheetDialogFragment = new WithdrawBottomSheetDialogFragment();
            withdrawBottomSheetDialogFragment.show(getChildFragmentManager(),
                    "withdraw_dialog_fragment");
        });
        previousWins.setOnClickListener(view -> navController.navigate(HomeFragmentDirections.actionHomeFragmentToPreviousWinsFragment()));
        drawResults.setOnClickListener(view -> navController.navigate(HomeFragmentDirections.actionHomeFragmentToDrawResultsFragment()));
        myTickets.setOnClickListener(view -> navController.navigate(HomeFragmentDirections.actionHomeFragmentToMyTicketsFragment2()));
        myWithdrawals.setOnClickListener(view -> navController.navigate(HomeFragmentDirections.actionHomeFragmentToMyWithdrawalsFragment()));
    }
}