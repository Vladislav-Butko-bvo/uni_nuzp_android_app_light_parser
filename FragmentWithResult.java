package edu.nuzp.lightparser;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FragmentWithResult extends Fragment implements OnActivityResultListener {

    private ProgressBar spinnerRing;
    private ImageView productImg;
    private TextView productTitle;
    private TextView productPrice;
    private TextView productDesc;

    public static final String IMAGE = "image";
    public static final String TITLE = "title";
    public static final String PRICE = "price";
    public static final String DESC = "description";

    public FragmentWithResult() {
    }

    @Override
    public void transferParsedData(ArrayList<String> result) {
        Picasso.get()
                .load(result.get(0))
                .placeholder(getResources().getDrawable(R.drawable.spinner_ring_green, getActivity().getTheme()))
                .into(productImg, new Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getActivity(), "Error: image loading failed!",
                                Toast.LENGTH_LONG).show();
                    }
                });
        productTitle.setText(result.get(1));
        productPrice.setText(result.get(2));
        productDesc.setText(result.get(3));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        spinnerRing = view.findViewById(R.id.spinnerRing);
        productImg = view.findViewById(R.id.productImg);
        productTitle = view.findViewById(R.id.productTitle);
        productPrice = view.findViewById(R.id.productPrice);
        productDesc = view.findViewById(R.id.productDesc);

        Picasso.get()
                .load(getArguments().getString(IMAGE))
                .placeholder(getResources().getDrawable(R.drawable.spinner_ring_green, getActivity().getTheme()))
                .into(productImg, new Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getActivity(), "Error: image loading failed!",
                                Toast.LENGTH_LONG).show();
                    }
                });
        productTitle.setText(getArguments().getString(TITLE));
        productPrice.setText(getArguments().getString(PRICE));
        productDesc.setText(getArguments().getString(DESC));

        return view;
    }
}
