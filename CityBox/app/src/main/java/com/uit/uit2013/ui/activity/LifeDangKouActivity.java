package com.uit.uit2013.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.HelixEffect;
import com.uit.uit2013.R;
import com.uit.uit2013.model.DangKou;
import com.uit.uit2013.utils.db.DKDateCtrl;
import com.uit.uit2013.utils.network.DangKouNewWork;


import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by yszsyf on 16/2/13.
 */
public class LifeDangKouActivity  extends Activity implements View.OnClickListener {

    private  String dangkouid_s ,dangkouname;
    private int dangkouid;
    private TextView life_title , back , life_updata , life_history;
    public static ProgressDialog pr;
    private List<Map<String, Object>> data;
    Vector<DangKou> dangkou = new Vector<DangKou>();
    private JazzyListView life_dangkou_listview;

    private SimpleAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.life_dangkou);

        Intent intent = getIntent();
        dangkouid_s  = intent.getStringExtra("dangkouid");
        dangkouid = Integer.valueOf(dangkouid_s);
        dangkouid++;
        dangkouid_s = String.valueOf(dangkouid);
        dangkouname = intent.getStringExtra("dangkouname");
        createtitle();
        createlist();

    }

    private void createlist() {
       life_dangkou_listview = (JazzyListView)findViewById(R.id.life_dangkou_listview);
        life_dangkou_listview.setTransitionEffect( new HelixEffect());

        try {
            dangkou = DKDateCtrl.QueryRes(this ,dangkouid_s);
        }catch (Exception exceptione){}

        data = getData();

        OrderingAdapter myadapter = new OrderingAdapter(getBaseContext(), data);


        life_dangkou_listview.setAdapter(myadapter);


    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i=0 ; i<dangkou.size() ; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", ""+dangkou.get(i).getName().toString());
            map.put("price", "¥" + dangkou.get(i).getPrice().toString());
            map.put("sub","-");
            map.put("shuliang","0");
            map.put("add","+");
            list.add(map);
        }
        return list;
    }

    private void createtitle() {
        life_title = (TextView) findViewById(R.id.life_title);
        //String dangkouname_s = dangkouname + "   " + dangkouname +"   " + dangkouname;
        life_title.setText(dangkouname);
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

                updata();
                break;
        }
    }

    private void updata()  {
        pr = ProgressDialog.show(LifeDangKouActivity.this, null, "获取食堂数据中......");
       DKDateCtrl.delete(this , dangkouid_s);
        CountingTask task=new CountingTask();
        task.execute();
    }
    private class CountingTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {

            try {
                DangKouNewWork.getDangKou(getApplicationContext(), dangkouid_s);
            } catch (JSONException e) {}
            return null;
        }
        protected void onProgressUpdate(Void... progress){}
        protected void onPostExecute(Void result){
            getdateforwebsuccess();
        }
    }
    private  void getdateforwebsuccess(){
        pr.dismiss();
        dangkou = DKDateCtrl.QueryRes(this , dangkouid_s);

        createlist();
       // Toast.makeText(LifeDangKouActivity.this , "更新完成,请重新进入." ,Toast.LENGTH_SHORT).show();
        //this.finish();
    }


    public class OrderingAdapter extends BaseAdapter {

        private List<Map<String, Object>> data;
        private LayoutInflater layoutInflater;
        private Context context;
        private int shuliang[];
        private Zujian dk = new Zujian();


        public OrderingAdapter(Context context, List<Map<String, Object>> data) {
            this.context = context;
            this.data = data;
            this.layoutInflater = LayoutInflater.from(context);

            shuliang = new int[data.size()];
            for (int i = 0; i < data.size(); i++) {
                shuliang[i] = 0;
            }

        }

        /**
         * 组件集合，对应list.xml中的控件
         *
         * @author Administrator
         */
        public final class Zujian {
            public TextView name;
            public TextView price;
            public TextView number;
            public Button sub;
            public Button add;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        /**
         * 获得某一位置的数据
         */
        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        /**
         * 获得唯一标识
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final int selectID = position;
            if (convertView == null) {

                //获得组件，实例化组件
                convertView = layoutInflater.inflate(R.layout.item_dangkou, null);
                dk.name = (TextView) convertView.findViewById(R.id.itme_dk_name);
                dk.price = (TextView) convertView.findViewById(R.id.itme_dk_price);
                dk.sub = (Button) convertView.findViewById(R.id.item_dk_sub);
                dk.add = (Button) convertView.findViewById(R.id.item_dk_add);
                dk.number = (TextView) convertView.findViewById(R.id.item_dk_number);
                convertView.setTag(dk);
            } else {
                dk = (Zujian) convertView.getTag();
            }
            //绑定数据

            dk.name.setText((String) data.get(position).get("name"));
            dk.price.setText((String) data.get(position).get("price"));
            dk.number.setText("123");
            dk.sub.setText("-");
            dk.sub.setTag(position);
            dk.add.setTag(position);

            final Zujian finalHolder = dk;
            dk.sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("=-=", "sub: " + position + "   shuling :" + shuliang[position]);
                    shuliang[position]--;
                    
                    finalHolder.number.setText("aaaa");
                    Log.d("=-=", "number: " +  "" + finalHolder.number.getText().toString());


                }
            });
            dk.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("=-=", "add: " + position + "   shuling :" + shuliang[position]);
                    shuliang[position]++;
                    Log.d("=-=", "number: " +  "" + finalHolder.number.getText().toString());

                }
            });

            return convertView;
        }


    }


}
