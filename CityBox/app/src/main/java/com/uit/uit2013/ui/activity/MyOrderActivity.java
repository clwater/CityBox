package com.uit.uit2013.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.uit.uit2013.R;
import com.uit.uit2013.model.LocalOrder;
import com.uit.uit2013.utils.PreferenceTool;
import com.uit.uit2013.utils.db.OrderDateCtrl;

import java.util.Vector;

/**
 * Created by yszsyf on 16/2/17.
 */
public class MyOrderActivity extends Activity implements View.OnClickListener {
    private String history_type;
    private TextView back , titile ;
    private Activity activity;
    private Vector<LocalOrder> lo = new Vector<LocalOrder>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.myorder);
        activity = this;

        history_type = gethistorytype();


        createtitle();



        getlocalorder();

        //oncreate();
    }

    private String gethistorytype() {
        Intent intent=getIntent();
        String get_type=intent.getStringExtra("type");
        if (get_type.equals(LocalOrder.TYPE_SENG)){
            return "send";
        }else {
            return  "order";
        }
    }

    private void getlocalorder() {
        PreferenceTool pt = new PreferenceTool(activity);

        if (history_type.equals("send")) {
            lo = OrderDateCtrl.QueryRes(activity, LocalOrder.TYPE_SENG, PreferenceTool.getid());
            Log.d("history", "lo.get(0).getOrdernum()" + lo.get(0).getOrdernum());
        }
    }

    private void createtitle() {
        back = (TextView)findViewById(R.id.life_back);
        back.setOnClickListener(this);
        titile = (TextView)findViewById(R.id.life_title);
        titile.setText("我的订单");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.life_back:
                this.finish();
                break;
        }
    }
}
