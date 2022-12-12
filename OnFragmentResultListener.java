package edu.nuzp.lightparser;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public interface OnFragmentResultListener {
    void sendParsedData(ArrayList<String> result, Fragment fragment);
    void openFragmentInfo();
    void closeFragmentInfo();
}
