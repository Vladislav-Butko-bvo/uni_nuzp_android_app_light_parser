package edu.nuzp.lightparser;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FragmentInfo extends Fragment {

    private TextView textInfo;
    private Button cancelInfo;
    private OnFragmentResultListener fragmentListener;

    public FragmentInfo() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        textInfo = view.findViewById(R.id.textInfo);
        cancelInfo = view.findViewById(R.id.cancelInfo);

        cancelInfo.setOnClickListener(v -> fragmentListener.closeFragmentInfo());

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentResultListener) {
            fragmentListener = (OnFragmentResultListener) context;
        } else {
            throw new RuntimeException(context
                    + " must implement OnFragmentResultListener");
        }
    }
}

