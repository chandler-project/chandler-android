package com.chandlersystem.chandler.ui.product_detail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductCommentActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductCommentActivity extends Fragment {


    public ProductCommentActivity() {
        // Required empty public constructor
    }

    public static ProductCommentActivity newInstance() {
        return new ProductCommentActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_comment, container, false);
    }

}
