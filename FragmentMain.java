package edu.nuzp.lightparser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

enum ParsingStatus {
    CONNECTION_FAILED,
    WRONG_URL,
    SUCCESS
}

public class FragmentMain extends Fragment implements OnActivityBackListener {

    private TextView promptURL;
    private EditText enterURL;
    private Button controlProcess;
    private ProgressBar spinnerRing;
    private Button getInfo;
    private FrameLayout fragmentContainerResult;

    private Boolean buttonActiveFlag;

    private OnFragmentResultListener fragmentListener;

    public FragmentMain() {
    }

    class Parsing extends AsyncTask<String, Void, ArrayList<String>> {
        private ParsingStatus status = ParsingStatus.SUCCESS;
        @Override
        protected ArrayList<String> doInBackground(String... url) {
            ArrayList<String> receivedData = new ArrayList<>();
            String dataPart;
            Document doc;
            try {
                doc = Jsoup.connect(url[0]).get();
            } catch (Throwable t) {
                status = ParsingStatus.CONNECTION_FAILED;
                return null;
            }
            dataPart = doc.select("img.picture-container__picture").attr("src");
            if (dataPart.equals("")) { status = ParsingStatus.WRONG_URL; return null; }
            receivedData.add(dataPart);
            dataPart = doc.select("h1.product__title").text();
            if (dataPart.equals("")) { status = ParsingStatus.WRONG_URL; return null; }
            receivedData.add(dataPart);
            dataPart = doc.select("p.product-prices__big").text();
            if (dataPart.equals("")) { status = ParsingStatus.WRONG_URL; return null; }
            receivedData.add(dataPart);
            dataPart = doc.select("p.product-about__brief").text();
            if (dataPart.equals("")) { status = ParsingStatus.WRONG_URL; return null; }
            receivedData.add(dataPart);
            return receivedData;
        }
        @Override
        protected void onPostExecute(@Nullable ArrayList<String> result) {
            super.onPostExecute(result);

            controlProcess.setText(getResources().getString(R.string.parseIt));
            Toast.makeText(getActivity(), "Parsed",
                    Toast.LENGTH_SHORT).show();
            spinnerRing.setVisibility(View.INVISIBLE);
            controlProcess.setBackground(
                    getResources().getDrawable(R.drawable.button_rectangle, getActivity().getTheme()));
            controlProcess.setTextColor(getResources().getColor(R.color.green, getActivity().getTheme()));
            buttonActiveFlag = true;

            switch(status) {
                case CONNECTION_FAILED:
                    Toast.makeText(getActivity(), "Error: Connection failed!",
                            Toast.LENGTH_LONG).show();
                    break;
                case WRONG_URL:
                    Toast.makeText(getActivity(), "Error: Wrong URL!",
                            Toast.LENGTH_LONG).show();
                    break;
                default:

                    FragmentManager fm = getChildFragmentManager();

                    Fragment fragment = fm.findFragmentById(R.id.fragmentContainerResult);
                    if (fragment instanceof FragmentWithoutResult) {

                        fragment = new FragmentWithResult();

                        Bundle bundle = new Bundle();
                        bundle.putString(FragmentWithResult.IMAGE, result.get(0));
                        bundle.putString(FragmentWithResult.TITLE, result.get(1));
                        bundle.putString(FragmentWithResult.PRICE, result.get(2));
                        bundle.putString(FragmentWithResult.DESC, result.get(3));
                        fragment.setArguments(bundle);

                        fm.beginTransaction()
                                .replace(R.id.fragmentContainerResult, fragment)
                                .commit();
                       } else {
                         fragmentListener.sendParsedData(result, fragment);
                    }
                    break;
            }
            parseURL = null;
        }
    }

    Parsing parseURL;

    @Override
    public void cancelAsyncTask() {
        if(parseURL != null) {
            if (parseURL.getStatus() == AsyncTask.Status.RUNNING) {
                parseURL.cancel(true);
                Toast.makeText(getActivity(), "Warning: process has been aborted!",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        promptURL = view.findViewById(R.id.promptURL);
        enterURL = view.findViewById(R.id.enterURL);
        controlProcess = view.findViewById(R.id.controlProcess);
        spinnerRing = view.findViewById(R.id.spinnerRing);
        getInfo = view.findViewById(R.id.getInfo);
        fragmentContainerResult = view.findViewById(R.id.fragmentContainerResult);

        buttonActiveFlag = true;

        getChildFragmentManager().beginTransaction()
           .add(R.id.fragmentContainerResult, new FragmentWithoutResult())
           .commit();

        controlProcess.setOnClickListener(v -> {
            if(buttonActiveFlag) {
                parseURL = new Parsing();
                parseURL.execute(enterURL.getText().toString());
                controlProcess.setText(getResources().getString(R.string.interrupt));
                Toast.makeText(getActivity(), "In process...",
                        Toast.LENGTH_SHORT).show();
                spinnerRing.setVisibility(View.VISIBLE);
                controlProcess.setBackground(
                        getResources().getDrawable(R.drawable.button_with_border, getActivity().getTheme()));
                controlProcess.setTextColor(getResources().getColor(R.color.white, getActivity().getTheme()));
                buttonActiveFlag = false;
            } else {
                parseURL.cancel(true);
                controlProcess.setText(getResources().getString(R.string.parseIt));
                Toast.makeText(getActivity(), "Canceled",
                        Toast.LENGTH_SHORT).show();
                spinnerRing.setVisibility(View.INVISIBLE);
                controlProcess.setBackground(
                        getResources().getDrawable(R.drawable.button_rectangle, getActivity().getTheme()));
                controlProcess.setTextColor(getResources().getColor(R.color.green, getActivity().getTheme()));
                buttonActiveFlag = true;
            }
        });

        getInfo.setOnClickListener(v -> fragmentListener.openFragmentInfo());

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


