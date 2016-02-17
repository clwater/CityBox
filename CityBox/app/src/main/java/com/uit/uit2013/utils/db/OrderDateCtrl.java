package com.uit.uit2013.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.uit.uit2013.model.AllOrder;
import com.uit.uit2013.model.DangKou;
import com.uit.uit2013.model.LocalOrder;

import java.util.Vector;

/**
 * Created by soul on 2016/1/22.
 * 课表数据库控制类
 */
public class OrderDateCtrl {
    public static OrderDateHelp dbHelper;
    public  static  SQLiteDatabase db;

    public static void createSQL(Context context){
        //创建数据库
        dbHelper = new OrderDateHelp(context, "citybox.db", null, 1);
        dbHelper.getWritableDatabase();
        db = dbHelper.getWritableDatabase();

    }



    //插入数据到数据库
    public static void UpdateRes(Context context , LocalOrder lo){
        createSQL(context);
        ContentValues values = new ContentValues();
        values.put("ordertype", lo.getOrdertype());
        values.put("ordernum", lo.getOrdernum());
        values.put("ordermealman", lo.getOrdermealman());
        values.put("sendmealman", lo.getSendmealman());
        values.put("sendmanphone", lo.getSendmanphone());
        values.put("ordermenu", lo.getOrdermenu());
        values.put("orderstart", lo.getOrderstart());
        values.put("ordersucces", lo.getOrdersucces());
        values.put("orderend", lo.getOrderend());
        values.put("orderstatu", lo.getOrderstart());
        db.insert("my_order", null, values);


    }
    

    //查询信息
    public static Vector<LocalOrder> QueryRes(Context context , String ordertype , String id ){
        createSQL(context);
        Vector res = new Vector<LocalOrder>();

        Cursor cursor = db.query("my_order", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do{

                String type = cursor.getString(cursor.getColumnIndex("ordertype"));

                if (type.equals(ordertype) ) {
                    if (ordertype.equals(LocalOrder.TYPE_ORDER)){
                        String order_t = cursor.getString(cursor.getColumnIndex("ordermealman"));
                        if (order_t.equals(id)){
                            LocalOrder lo = new LocalOrder();
                            lo.setOrdertype(cursor.getString(cursor.getColumnIndex("ordertype")));
                            lo.setOrdernum(cursor.getString(cursor.getColumnIndex("ordernum")));
                            lo.setOrdermealman(cursor.getString(cursor.getColumnIndex("ordermealman")));
                            lo.setSendmealman(cursor.getString(cursor.getColumnIndex("sendmealman")));
                            lo.setSendmanphone(cursor.getString(cursor.getColumnIndex("sendmanphone")));
                            lo.setOrderstart(cursor.getString(cursor.getColumnIndex("orderstart")));
                            lo.setOrdersucces(cursor.getString(cursor.getColumnIndex("ordersucces")));
                            lo.setOrderend(cursor.getString(cursor.getColumnIndex("orderend")));
                            lo.setOrderstatu(cursor.getString(cursor.getColumnIndex("orderstatu")));
                            String menu = cursor.getString(cursor.getColumnIndex("ordermenu")).replace('@' , '\"');
                            lo.setOrdermenu(menu);
                            res.add(lo);
                        }
                    }else if(ordertype.equals(LocalOrder.TYPE_SENG)){
                        String order_t = cursor.getString(cursor.getColumnIndex("sendmealman"));
                        if (order_t.equals(id)){
                            LocalOrder lo = new LocalOrder();
                            lo.setOrdertype(cursor.getString(cursor.getColumnIndex("ordertype")));
                            lo.setOrdernum(cursor.getString(cursor.getColumnIndex("ordernum")));
                            lo.setOrdermealman(cursor.getString(cursor.getColumnIndex("ordermealman")));
                            lo.setSendmealman(cursor.getString(cursor.getColumnIndex("sendmealman")));
                            lo.setSendmanphone(cursor.getString(cursor.getColumnIndex("sendmanphone")));
                            lo.setOrderstart(cursor.getString(cursor.getColumnIndex("orderstart")));
                            lo.setOrdersucces(cursor.getString(cursor.getColumnIndex("ordersucces")));
                            lo.setOrderend(cursor.getString(cursor.getColumnIndex("orderend")));
                            lo.setOrderstatu(cursor.getString(cursor.getColumnIndex("orderstatu")));
                            res.add(lo);
                        }
                    }
                }


            } while (cursor.moveToNext());
        }
        cursor.close();
        return res;
    }


    public  static  void delete(Context context , String id){
        createSQL(context);
       // String sql="DELETE FROM dangkou where dangkouid = " + id ;
       // db.execSQL(sql);
    }
}
