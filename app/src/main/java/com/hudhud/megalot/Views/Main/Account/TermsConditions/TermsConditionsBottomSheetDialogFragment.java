package com.hudhud.megalot.Views.Main.Account.TermsConditions;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hudhud.megalot.R;

public class TermsConditionsBottomSheetDialogFragment extends Fragment {

    public TermsConditionsBottomSheetDialogFragment() {
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
        return inflater.inflate(R.layout.fragment_terms_conditions_bottom_sheet_dialog, container, false);
    }
}