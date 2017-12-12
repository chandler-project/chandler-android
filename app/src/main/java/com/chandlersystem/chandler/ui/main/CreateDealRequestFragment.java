package com.chandlersystem.chandler.ui.main;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.databinding.FragmentCreateDealRequestBinding;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnCreateDealRequestInteraction} interface
 * to handle interaction events.
 * Use the {@link CreateDealRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateDealRequestFragment extends Fragment {

    private OnCreateDealRequestInteraction mListener;
    private FragmentCreateDealRequestBinding mBinding;

    public CreateDealRequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateDealRequestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateDealRequestFragment newInstance() {
        CreateDealRequestFragment fragment = new CreateDealRequestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_deal_request, container, false);
        mBinding.createDeal.setOnClickListener( view -> mListener.onCreateDeal());
        mBinding.createRequest.setOnClickListener( view -> mListener.onCreateRequest());
        return mBinding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCreateDealRequestInteraction) {
            mListener = (OnCreateDealRequestInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCreateDealRequestInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCreateDealRequestInteraction {

        void onCreateRequest();

        void onCreateDeal();
    }
}
