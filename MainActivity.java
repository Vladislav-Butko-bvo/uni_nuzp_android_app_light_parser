package edu.nuzp.lightparser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.FrameLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnFragmentResultListener {

    private FrameLayout fragmentContainerMain;
    private Fragment fragmentMain;

    private OnActivityResultListener transferListener;
    private OnActivityBackListener backListener;

    @Override
    public void sendParsedData(ArrayList<String> result, Fragment fragment){
        if (fragment instanceof OnActivityResultListener) {
            transferListener = (OnActivityResultListener) fragment;
        } else {
            throw new RuntimeException(fragment.toString()
                    + " must implement OnActivityResultListener");
        }
        transferListener.transferParsedData(result);
    }

    @Override
    public void openFragmentInfo(){
        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = new FragmentInfo();
        fm.beginTransaction()
              .add(R.id.fragmentContainerMain, fragment)
              .addToBackStack(null)
              .commit();
    }

    @Override
    public void closeFragmentInfo(){
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentContainerMain = findViewById(R.id.fragmentContainerMain);

        FragmentManager fm = getSupportFragmentManager();

        fragmentMain = new FragmentMain();
        fm.beginTransaction()
             .add(R.id.fragmentContainerMain, fragmentMain)
             .commit();
    }

    @Override
    public void onBackPressed() {
       if (fragmentMain instanceof OnActivityBackListener) {
            backListener = (OnActivityBackListener) fragmentMain;
       } else {
            throw new RuntimeException(fragmentMain.toString()
                + " must implement OnActivityBackListener");
       }
       backListener.cancelAsyncTask();
       super.onBackPressed();
    }
}
