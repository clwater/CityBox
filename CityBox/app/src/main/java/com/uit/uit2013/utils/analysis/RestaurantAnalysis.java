package com.uit.uit2013.utils.analysis;

import android.util.Log;

import com.uit.uit2013.model.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Vector;

/**
 * Created by yszsyf on 16/1/31.
 */
public class RestaurantAnalysis {
    public static int AnalysisRes(String result) throws JSONException {
        Vector<Restaurant> res = new Vector<Restaurant>();

        Log.d("=-=" , "result" + result);

        JSONTokener jsonParser = new JSONTokener(result);
        JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
        JSONArray yy = jsonObject.getJSONArray("yy");
        Log.d("bn" ,  "yy"+yy);
        JSONTokener data2 = new JSONTokener(String.valueOf(yy.get(0)));
        JSONObject child2 = (JSONObject) data2.nextValue();
        JSONArray st2 = child2.getJSONArray("data");
        Log.d("bn" ,  "st2"+st2);
        JSONTokener data3 = new JSONTokener(String.valueOf(yy.get(1)));
        JSONObject child3 = (JSONObject) data3.nextValue();
        JSONArray st3 = child3.getJSONArray("data");
        Log.d("bn" ,  "st3"+st3);

        int dangkou_num = st2.length() + st3.length() ;


        for (int i = 0 ; i < st2.length() ; i++){
            Restaurant r = new Restaurant();
            JSONObject temp = st2.getJSONObject(i);
            Log.e("bn", "temp1" + temp);
            r.setId(temp.getString("id"));
            r.setName(temp.getString("name"));
            r.setLocation(temp.getString("location"));
            r.setFloor(temp.getString("floor"));
            r.setTelephone(temp.getString("phone"));
            res.add(r);
        }
        for (int i = st2.length() ; i < dangkou_num ; i ++){
            Restaurant r = new Restaurant();
            JSONObject temp = st2.getJSONObject(i);
            Log.e("bn", "temp1" + temp);
            r.setId(temp.getString("id"));
            r.setName(temp.getString("name"));
            r.setLocation(temp.getString("location"));
            r.setFloor(temp.getString("floor"));
            r.setTelephone(temp.getString("phone"));
            res.add(r);
        }

        return  1;
    }
}
