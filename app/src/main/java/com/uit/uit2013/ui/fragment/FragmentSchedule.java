package com.uit.uit2013.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.uit.uit2013.R;
import com.uit.uit2013.model.KeBiao;
import com.uit.uit2013.model.Schedule;
import com.uit.uit2013.model.User;
import com.uit.uit2013.utils.analysis.LoginAnalysis;
import com.uit.uit2013.utils.analysis.ScheduleAnalysis;
import com.uit.uit2013.utils.db.ScheduleDateCtrl;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by soul on 2016/1/19.
 */
public class FragmentSchedule extends Fragment implements View.OnClickListener {
    private TextView tv,updatatv;
    private View view;
    private Activity activity;
    private int screenWidth ,screenHeight;
    private TextView[] bian;
    private Spinner choose;
    private String[] s_choose;
    private LinearLayout[] day;
    private TextView[][] day_t;
    private TextView[][] day_top;
    private LinearLayout top;
    private ArrayAdapter<String> choose_adapter;
    private TextView top_dq;
    public static ProgressDialog pr;
    public RequestQueue mQueue;
    private String request ;
    private int choose_week = 14 ;
    Context context;
    Vector<KeBiao>  schedule;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_schedule, container, false);
        activity = getActivity();
        context = activity.getApplicationContext();
        genScreen();
        oncreate();
        getSchedule();

        return view;
    }

    private void getSchedule() {
        schedule = Schedule.getins(context);
        drawSchedule();

    }

    private void drawSchedule() {
        cleardrawSchedule();

        for (int i = 0 ; i < 7 ; i++){
            for (int j = 0 ; j < 6 ; j++){
                for (int k = 0 ; k < schedule.size() ; k++){
                    KeBiao kb = schedule.get(k);
                    if ( (kb.getINDEX_T() == j ) && (kb.getINDEX_W() == i ) ){
                        Vector length = new Vector();
                        length = kb.getWeek();
                        for (int m = 0 ; m <length.size() ; m++){
                            if ( Integer.valueOf( length.get(m).toString() )== choose_week){
                                String classtext = "";
                                classtext += kb.getClass_name() + "@"  + kb.getClassroom();
                                day_t[i][j].setText(classtext);
                                day_t[i][j].setBackgroundColor(Color.parseColor(kb.getColors()));

                            }
                        }

                    }
                 }

            }
        }

    }

    private void cleardrawSchedule() {
        for (int i = 0 ; i < 7 ; i++){
            for (int j = 0 ; j < 6 ; j++){
                day_t[i][j].setText("");
                day_t[i][j].setBackgroundColor(Color.parseColor("#ffffff"));
            }
        }
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
                updaataSchedule();
                break;
            case R.id.day0:                                 //0-6是课表的变换 点击的时候选中的变宽
                changeDayWidth(0);
                break;
            case R.id.day1:
                changeDayWidth(1);
                break;
            case R.id.day2:
                changeDayWidth(2);
                break;
            case R.id.day3:
                changeDayWidth(3);
                break;
            case R.id.day4:
                changeDayWidth(4);
                break;
            case R.id.day5:
                changeDayWidth(5);
                break;
            case R.id.day6:
                changeDayWidth(6);
                break;
        }
    }

    private void updaataSchedule() {
        pr = ProgressDialog.show(activity, null, "更新数据中......");
        mQueue = Volley.newRequestQueue(activity);
        CountingTask task=new CountingTask();
        task.execute();
    }



    private class CountingTask extends AsyncTask<Void, Void, Void> {
        boolean pd = false;
        User user = User.getDefault();
        protected Void doInBackground(Void... params) {
            StringRequest postRequest = new StringRequest(Request.Method.POST ,  "http://120.27.53.146:5000/api/schedule",
                    new Response.Listener<String>() {
                        public void onResponse(String response) {
                            request = response;
                            //Log.d("=-=" , request);
                            ScheduleAnalysis sa = new ScheduleAnalysis();
                            try {
                                pd = sa.getstatu(response);
                            } catch (JSONException e) {}
                            if (pd){
                               Log.d("=-=" , "pd: " + pd);
                                try {
                                    ScheduleAnalysis.AnalysisSchedule(response ,context);
                                    pr.dismiss();
                                } catch (JSONException e) {
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {}
            }) {
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", user.getId());
                    params.put("password", user.getPw());
                    params.put("action", "update");
                    return params;
                }
            };
            mQueue.add(postRequest);
            return null;
        }

        protected void onProgressUpdate(Void... progress){}
        protected void onPostExecute(Void result){}

    }


    private void genScreen() {
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screenWidth  = display.getWidth();
        screenHeight  = display.getHeight();
        screenWidth = screenWidth / 10 * 13 ;
    }


    private void oncreate() {
        bian = new TextView[12];
        bian[0] = (TextView) view.findViewById(R.id.bian_0);
        bian[1] = (TextView) view.findViewById(R.id.bian_1);
        bian[2] = (TextView) view.findViewById(R.id.bian_2);
        bian[3] = (TextView) view.findViewById(R.id.bian_3);
        bian[4] = (TextView) view.findViewById(R.id.bian_4);
        bian[5] = (TextView) view.findViewById(R.id.bian_5);
        bian[6] = (TextView) view.findViewById(R.id.bian_6);
        bian[7] = (TextView) view.findViewById(R.id.bian_7);
        bian[8] = (TextView) view.findViewById(R.id.bian_8);
        bian[9] = (TextView) view.findViewById(R.id.bian_9);
        bian[10] = (TextView) view.findViewById(R.id.bian_10);
        bian[11] = (TextView) view.findViewById(R.id.bian_11);

        for (int i = 0 ; i < 12 ; i++){
            bian[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, screenHeight / 12 ));
        }

        //对应活动中的各个组件
        choose = (Spinner) view.findViewById(R.id.choose);
        s_choose  = new String[20];

        day = new LinearLayout[7];
        day[0] = (LinearLayout) view.findViewById(R.id.day0);
        day[1] = (LinearLayout) view.findViewById(R.id.day1);
        day[2] = (LinearLayout) view.findViewById(R.id.day2);
        day[3] = (LinearLayout) view.findViewById(R.id.day3);
        day[4] = (LinearLayout) view.findViewById(R.id.day4);
        day[5] = (LinearLayout) view.findViewById(R.id.day5);
        day[6] = (LinearLayout) view.findViewById(R.id.day6);

        for (int i = 0 ; i < 7 ; i++){
            day[i].setLayoutParams(new LinearLayout.LayoutParams( screenWidth / 8 ,  LinearLayout.LayoutParams.MATCH_PARENT));
        }

        day_t = new TextView[7][6];
        day_top = new TextView[7][2];

        day_top[0][0] = (TextView) view.findViewById(R.id.day0_top1);
        day_top[0][1] = (TextView) view.findViewById(R.id.day0_top2);
        day_top[1][0] = (TextView) view.findViewById(R.id.day1_top1);
        day_top[1][1] = (TextView) view.findViewById(R.id.day1_top2);
        day_top[2][0] = (TextView) view.findViewById(R.id.day2_top1);
        day_top[2][1] = (TextView) view.findViewById(R.id.day2_top2);
        day_top[3][0] = (TextView) view.findViewById(R.id.day3_top1);
        day_top[3][1] = (TextView) view.findViewById(R.id.day3_top2);
        day_top[4][0] = (TextView) view.findViewById(R.id.day4_top1);
        day_top[4][1] = (TextView) view.findViewById(R.id.day4_top2);
        day_top[5][0] = (TextView) view.findViewById(R.id.day5_top1);
        day_top[5][1] = (TextView) view.findViewById(R.id.day5_top2);
        day_top[6][0] = (TextView) view.findViewById(R.id.day6_top1);
        day_top[6][1] = (TextView) view.findViewById(R.id.day6_top2);



        day_t[0][0] = (TextView) view.findViewById(R.id.day0_0);
        day_t[0][1] = (TextView) view.findViewById(R.id.day0_1);
        day_t[0][2] = (TextView) view.findViewById(R.id.day0_2);
        day_t[0][3] = (TextView) view.findViewById(R.id.day0_3);
        day_t[0][4] = (TextView) view.findViewById(R.id.day0_4);
        day_t[0][5] = (TextView) view.findViewById(R.id.day0_5);

        day_t[1][0] = (TextView) view.findViewById(R.id.day1_0);
        day_t[1][1] = (TextView) view.findViewById(R.id.day1_1);
        day_t[1][2] = (TextView) view.findViewById(R.id.day1_2);
        day_t[1][3] = (TextView) view.findViewById(R.id.day1_3);
        day_t[1][4] = (TextView) view.findViewById(R.id.day1_4);
        day_t[1][5] = (TextView) view.findViewById(R.id.day1_5);

        day_t[2][0] = (TextView) view.findViewById(R.id.day2_0);
        day_t[2][1] = (TextView) view.findViewById(R.id.day2_1);
        day_t[2][2] = (TextView) view.findViewById(R.id.day2_2);
        day_t[2][3] = (TextView) view.findViewById(R.id.day2_3);
        day_t[2][4] = (TextView) view.findViewById(R.id.day2_4);
        day_t[2][5] = (TextView) view.findViewById(R.id.day2_5);

        day_t[3][0] = (TextView) view.findViewById(R.id.day3_0);
        day_t[3][1] = (TextView) view.findViewById(R.id.day3_1);
        day_t[3][2] = (TextView) view.findViewById(R.id.day3_2);
        day_t[3][3] = (TextView) view.findViewById(R.id.day3_3);
        day_t[3][4] = (TextView) view.findViewById(R.id.day3_4);
        day_t[3][5] = (TextView) view.findViewById(R.id.day3_5);

        day_t[4][0] = (TextView) view.findViewById(R.id.day4_0);
        day_t[4][1] = (TextView) view.findViewById(R.id.day4_1);
        day_t[4][2] = (TextView) view.findViewById(R.id.day4_2);
        day_t[4][3] = (TextView) view.findViewById(R.id.day4_3);
        day_t[4][4] = (TextView) view.findViewById(R.id.day4_4);
        day_t[4][5] = (TextView) view.findViewById(R.id.day4_5);

        day_t[5][0] = (TextView) view.findViewById(R.id.day5_0);
        day_t[5][1] = (TextView) view.findViewById(R.id.day5_1);
        day_t[5][2] = (TextView) view.findViewById(R.id.day5_2);
        day_t[5][3] = (TextView) view.findViewById(R.id.day5_3);
        day_t[5][4] = (TextView) view.findViewById(R.id.day5_4);
        day_t[5][5] = (TextView) view.findViewById(R.id.day5_5);

        day_t[6][0] = (TextView) view.findViewById(R.id.day6_0);
        day_t[6][1] = (TextView) view.findViewById(R.id.day6_1);
        day_t[6][2] = (TextView) view.findViewById(R.id.day6_2);
        day_t[6][3] = (TextView) view.findViewById(R.id.day6_3);
        day_t[6][4] = (TextView) view.findViewById(R.id.day6_4);
        day_t[6][5] = (TextView) view.findViewById(R.id.day6_5);

        for (int i = 0 ; i < 7 ; i++){
            for (int j = 0 ; j < 6 ; j++) {
                day_t[i][j].setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 8, screenHeight / 6 ));
            }
        }

        top = (LinearLayout)view.findViewById(R.id.top);
        top.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, screenHeight / 12 ));

        int i = 0;

        for( i = 0 ; i< 7 ; i++){
            day[i].setOnClickListener((View.OnClickListener) this);
        }

        for( i = 0 ; i < 20 ; i++){
            s_choose[i] = "第" + ( i+1) + "周";
        }

        choose_adapter = new ArrayAdapter<String>(activity,R.layout.drop_down_item_top,  s_choose);
        choose_adapter.setDropDownViewResource(R.layout.drop_down_item);
        choose.setAdapter(choose_adapter);
        choose.setOnItemSelectedListener(new SpinnerSelectedListener());
        choose.setVisibility(View.VISIBLE);

        top_dq = ( TextView ) view.findViewById(R.id.top_dq);
    }

        /*
        * 下拉菜单监听
        * */
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            choose_week = (int) arg3 + 1;
            drawSchedule();
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
    private void setKeBiao(int _week) {

//        for (int i = 0; i < 7; i++) {
//            for (int j = 0; j < 6; j++) {
//                int length = kebiaoadapter[i][j].getNum();
//                lesson_is = false;
//                for (int k = 0; k < length; k++) {
//                    int[] check_week = new int[20];
//                    check_week = kebiaoadapter[i][j].getWeeks(k);
//                    if (lesson_is == false) {
//                        day_t[i][j].setText("");
//                        day_t[i][j].setBackgroundColor(Color.parseColor("#FFFFFF"));
//                    }
//                    int check_lessong_lesson = check_week.length;
//                    for (int l = 0; l < check_lessong_lesson; l++) {
//
//                        if (check_week[l] == _week) {
//                            day_t[i][j].setText("" + kebiaoadapter[i][j].getClass_name(k) + "\n@" + kebiaoadapter[i][j].getClassrom(k));
//                            day_t[i][j].setTextColor(Color.parseColor("#000000"));
//                            lesson_is = true;            //某一节课不同周数课程同 防止空的课程把已存在的覆盖
//                            changecolor(kebiaoadapter[i][j].getClass_name(k), i, j);
//                        }
//                    }
//                }
//            }
//        }
    }



    private void changeDayWidth(int _day) {
        for(int  i = 0 ; i < 7 ; i++){
            if( i !=  _day) {
                day[i].setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 8, LinearLayout.LayoutParams.MATCH_PARENT ));

                day_top[i][0].setTextColor(Color.parseColor("#000000"));
                day_top[i][1].setBackgroundColor(Color.parseColor("#EFEAF0"));
                for ( int j = 0 ; j < 6 ; j++ ){
                    day_t[i][j].setTextSize(12);
                    day_t[i][j].setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 8 , screenHeight / 6));
                }
            }else{
                day[i].setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 16 * 3, LinearLayout.LayoutParams.MATCH_PARENT ));
                day_top[i][0].setTextColor(Color.parseColor("#34CED9"));
                day_top[i][1].setBackgroundColor(Color.parseColor("#34CED9"));
                for (int j = 0 ; j < 6 ; j++ ){
                    day_t[i][j].setTextSize(14);
                    day_t[i][j].setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 16 * 3, screenHeight / 6));
                }
            }
        }
    }
}