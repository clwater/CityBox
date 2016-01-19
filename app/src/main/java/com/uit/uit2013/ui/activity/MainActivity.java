package com.uit.uit2013.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.uit.uit2013.R;
import com.uit.uit2013.utils.PreferenceTool;
import com.umeng.analytics.MobclickAgent;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     PreferenceTool pt = new PreferenceTool(this);

     boolean login_statu = pt.getloginstatu();        //判断是否登陆

     if(!login_statu){
            Log.d("=-=" , "=-=");
                startActivity(new Intent( this , LoginActivity.class));
        }
    }



    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
