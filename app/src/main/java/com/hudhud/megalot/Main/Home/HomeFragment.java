package com.hudhud.megalot.Main.Home;

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
import com.hudhud.megalot.Main.MainActivity;
import com.hudhud.megalot.R;

public class HomeFragment extends Fragment {

    NavController navController;
    MainActivity mainActivity;
    MaterialButton withdrawBtn;

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
    }

    private void onClick(){
        withdrawBtn.setOnClickListener(view -> {
            WithdrawBottomSheetDialogFragment withdrawBottomSheetDialogFragment = new WithdrawBottomSheetDialogFragment();
            withdrawBottomSheetDialogFragment.show(getChildFragmentManager(),
                    "withdraw_dialog_fragment");
        });
    }
}