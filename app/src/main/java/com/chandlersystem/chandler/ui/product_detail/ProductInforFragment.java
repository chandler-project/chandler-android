package com.chandlersystem.chandler.ui.product_detail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductInforFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductInforFragment extends Fragment {


    public ProductInforFragment() {
        // Required empty public constructor
    }

    public static ProductInforFragment newInstance() {
        return new ProductInforFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_infor, container, false);
    }

}
