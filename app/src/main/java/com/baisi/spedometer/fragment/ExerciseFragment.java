package com.baisi.spedometer.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baisi.spedometer.R;
import com.baisi.spedometer.base.BaseFragment;

/**
 * Created by hanwenmao on 2018/1/20.
 */

public class ExerciseFragment extends BaseFragment {

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.activity_sport_history ,null );
        return view;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setDefaultFragmentTitle(String title) {

    }
}
