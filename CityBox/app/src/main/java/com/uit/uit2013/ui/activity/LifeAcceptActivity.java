package com.uit.uit2013.ui.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.exceptions.EaseMobException;
import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.GrowEffect;
import com.twotoasters.jazzylistview.effects.HelixEffect;
import com.uit.uit2013.R;
import com.uit.uit2013.model.AllOrder;
import com.uit.uit2013.utils.analysis.OrderAnalysis;
import com.uit.uit2013.utils.db.ResDateCtrl;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by yszsyf on 16/2/16.
 */
public class LifeAcceptActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView accept_title , accept_back , accept_updata , accept_history;
    private AllOrder allorder;
    private JazzyListView accept_listview;
    private  SimpleAdapter adapter;
    private  List<Map<String, Object>> data;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.life_accept);

        imcreate();
        createtitle();

        createlistview();
    }

    private void createlistview() {
        accept_listview = (JazzyListView)findViewById(R.id.life_accept_listview);
        accept_listview.setTransitionEffect( new GrowEffect());



//        try {
//            res = ResDateCtrl.QueryRes(this);
//        }catch (Exception exceptione){}
//
        data = getData();
        adapter = new SimpleAdapter(this,data,R.layout.item_accept,
                new String[]{"statu","loaction","allprice" , "address" , "rewark"},
                new int[]{R.id.item_accept_statu , R.id.item_accept_location,R.id.item_accept_allprice , R.id.item_accept_address , R.id.item_accept_rewark} );


        accept_listview.setAdapter(adapter);
        accept_listview.setOnItemClickListener(this);
    }

    private List<Map<String,Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i=0 ; i<1 ; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("statu", "等你\n抢单");
            map.put("loaction", "位置:" + i);
            map.put("allprice" , "总价:" + i);
            map.put("address" , "地址:" + i);
            map.put("rewark" , "打赏" + i);
            list.add(map);
        }
        return list;
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                            long arg3) {


    }

    private void createtitle() {
        accept_title = (TextView) findViewById(R.id.life_title);
        accept_title.setText("送餐");
        accept_back = (TextView) findViewById(R.id.life_back);
        accept_back.setOnClickListener(this);
        accept_updata = (TextView) findViewById(R.id.life_updata);
        accept_updata.setOnClickListener(this);
        accept_history = (TextView) findViewById(R.id.life_history);
        accept_history.setOnClickListener(this);

    }

    private void imcreate() {
        Activity appContext = this;
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        if (processAppName == null ||!processAppName.equalsIgnoreCase("com.uit.uit2013.ui.activity.LifeAcceptActivity")) {
            Log.e("accept", "enter the service process!");
        }
        EMChat.getInstance().init(this);
        EMChat.getInstance().setDebugMode(true);//在做打包混淆时，要关闭debug模式，避免消耗不必要的资源
        IntentFilter cmdIntentFilter = new IntentFilter(EMChatManager.getInstance().getCmdMessageBroadcastAction());
        appContext.registerReceiver(cmdMessageReceiver, cmdIntentFilter);
        EMChat.getInstance().setAppInited();
        loginim();

    }

    private BroadcastReceiver cmdMessageReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String msgId = intent.getStringExtra("msgid");
            EMMessage message = intent.getParcelableExtra("message");
            CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
            String aciton = cmdMsgBody.action;//获取自定义action
            //获取扩展属性

            String attr= null;
            try {
                attr = message.getStringAttribute("status");
                Log.d("accept" , attr);
                if (attr.equals("new")){
                    Log.d("accept" , aciton);
                    //adddongcanxinxi("" + aciton);
                    getneworder(aciton);

                }else if (attr.equals("taked")){
                    Log.d("accept" , aciton);
                    // removedongcanxinxi("" + aciton);
                    //removelist("" +aciton);
                }
            } catch (EaseMobException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    };

    private void getneworder(String aciton) throws JSONException {
        allorder = OrderAnalysis.orderanalysis(aciton);
        Log.d("accept" ,"allorder.getPrice(): "+ allorder.getId() );

        insertneworder(allorder);

    }

    private void insertneworder(AllOrder allorder) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("statu", "等你\n抢单");
        map.put("loaction", "位置:" + allorder.getOrdermeal().get(0).getSt());
        map.put("allprice" , "总价:" + allorder.getPrice());
        map.put("address" , "地址:" + allorder.getWhere());
        map.put("rewark" , "打赏" + allorder.getRemark());

        data.add(map);

        adapter.notifyDataSetChanged();

    }

    private void loginim() {
        EMChatManager.getInstance().login("201312026","201312026",new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        EMGroupManager.getInstance().loadAllGroups();
                        EMChatManager.getInstance().loadAllConversations();
                        Log.e("accept", "登陆聊天服务器成功！");
                    }
                });
            }
            public void onProgress(int progress, String status) {}
            public void onError(int code, String message) {
                Log.e("accept", "登陆聊天服务器失败！");
            }
        });

    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {}
        }
        return processName;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.life_back:
                this.finish();
                break;
            case R.id.life_history:
                break;
            case R.id.life_updata:
                break;
        }
    }
}
