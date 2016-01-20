package com.uit.uit2013.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uit.uit2013.R;

/**
 * Created by soul on 2016/1/19.
 */
public class FragmentSchedule extends Fragment implements View.OnClickListener {
    private TextView tv,updatatv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv = (TextView) getView().findViewById(R.id.titleTv);
        updatatv = (TextView)getView().findViewById(R.id.updatatv);
        updatatv.setOnClickListener(this);
        tv.setText("课表");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.updatatv:
                break;
        }
    }
}