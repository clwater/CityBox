package com.uit.uit2013.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.uit.uit2013.R;
import com.uit.uit2013.model.Restaurant;
import com.uit.uit2013.model.User;
import com.uit.uit2013.utils.analysis.RestaurantAnalysis;
import com.uit.uit2013.utils.analysis.ScheduleAnalysis;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by yszsyf on 16/1/30.
 */
public class LifeRestauranActivity extends Activity implements View.OnClickListener {
    private TextView life_title , back , life_updata , life_history;
    public static ProgressDialog pr;
    public RequestQueue mQueue;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.life_restaurant);

        createtitle();



    }

    private void createtitle() {
        life_title = (TextView) findViewById(R.id.life_title);
        life_title.setText("选择档口");
        back = (TextView) findViewById(R.id.life_back);
        back.setOnClickListener(this);
        life_updata = (TextView) findViewById(R.id.life_updata);
        life_updata.setOnClickListener(this);
        life_history = (TextView) findViewById(R.id.life_history);
        life_history.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.life_back:
                this.finish();
                break;
            case R.id.life_history:
                break;
            case R.id.life_updata:
               // Log.d("aaaaa" , "=-=222" );
                updata();
                break;
        }
    }

    private void updata() {
        //pr = ProgressDialog.show(this, null, "更新数据中......");
        Toast.makeText(this , "huoqu" , Toast.LENGTH_SHORT).show();
        mQueue = Volley.newRequestQueue(this);
        //res = new Vector<Restaurant>();

        CountingTask task=new CountingTask();
        task.execute();
    }

    private class CountingTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... params) {
            StringRequest stringRequest = new StringRequest("http://csxyxzs.sinaapp.com/stall_all.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                int a = RestaurantAnalysis.AnalysisRes(response);
                                Log.d("=-=" , "=-=-=" + a);
                            } catch (JSONException e) {}
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("aaaaa", "error" + error.getMessage(), error);
                }
            });
            mQueue.add(stringRequest);

            return null;
        }

        protected void onProgressUpdate(Void... progress){}
        protected void onPostExecute(Void result){}

    }

}
