package com.chandlersystem.chandler.ui.create_request;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.databinding.FragmentSelectDateBinding;
import com.chandlersystem.chandler.databinding.FragmentSelectDeadlineBinding;
import com.chandlersystem.chandler.ui.adapters.SelectDateAdapter;
import com.chandlersystem.chandler.utilities.ViewUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import io.reactivex.disposables.CompositeDisposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectDeadlineFragment extends Fragment {
    private static final String ARGUMENT_TITLE = "argument-title";

    private FragmentSelectDeadlineBinding mBinding;

    private SelectDeadlineListener mListener;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private String mTitle;

    public interface SelectDeadlineListener {
        void onDeadlineSelected(String date);
    }

    public SelectDeadlineFragment() {
        // Required empty public constructor
    }

    public static SelectDeadlineFragment getInstance() {
        return new SelectDeadlineFragment();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mCompositeDisposable.clear();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SelectDeadlineListener) {
            mListener = (SelectDeadlineListener) context;
        } else {
            throw new RuntimeException("Please make the context implement SelectDateListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_deadline, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            mTitle = getArguments().getString(ARGUMENT_TITLE);
        }

        setupViews();
        handleEvents();
    }

    private void handleEvents() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        mBinding.datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                (datePicker, year, month, dayOfMonth) ->
                {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    String formattedDate = dateFormat.format(new Date(year - 1900, month, dayOfMonth));
                    mListener.onDeadlineSelected(formattedDate);
                });
    }

    private void setupViews() {
        setupTitle();
        setupDatePicker();
    }

    private void setupDatePicker() {
        mBinding.datePicker.setMinDate(System.currentTimeMillis());

    }

    private void setupTitle() {
        ViewUtil.setText(mBinding.tvTitle, getString(R.string.content_select_deadline));
    }
}
