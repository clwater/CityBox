package com.uit.uit2013.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.FanEffect;
import com.uit.uit2013.R;
import com.uit.uit2013.ui.activity.LifeRestauranActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soul on 2016/1/19.
 */
public class FragmentLife extends Fragment implements View.OnClickListener {
    private TextView tv , life_restaurant , life_songcan ,updatatv;
    private JazzyListView jazzylistview_life ;
    private View view;
    private Activity activity;
    private Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_life, container, false);
        activity = getActivity();
        context = activity.getApplicationContext();

        create();

       // test();

        return view;
    }

    private void create() {
        life_restaurant = (TextView) view.findViewById(R.id.life_restaurant);
        life_songcan = (TextView) view.findViewById(R.id.life_songcan);
        updatatv = (TextView) view.findViewById(R.id.updatatv);
        updatatv.setText("");

        life_restaurant.setOnClickListener(this);
        life_songcan.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.life_restaurant:
                startActivity(new Intent(activity , LifeRestauranActivity.class));
                break;
            case R.id.life_songcan:
                break;
        }

    }

//    private void test() {
//        jazzylistview_life = (JazzyListView) view.findViewById(R.id.jazzylistview_life);
//        jazzylistview_life.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_expandable_list_item_1,getData()));
//        jazzylistview_life.setTransitionEffect(new FanEffect());
//        //activity.setContentView(jazzylistview_life);
//    }
//    private List<String> getData(){
//
//        List<String> data = new ArrayList<String>();
//        for (int i = 0 ; i < 50 ; i++) {
//            String a =  "测试数据"+i;
//            data.add(a);
//        }
//
//
//        return data;
//    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv = (TextView) getView().findViewById(R.id.titleTv);
        tv.setText("生活");
    }


}