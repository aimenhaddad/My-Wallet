package com.example.mydebts.account;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.mydebts.DataBase;

import java.util.ArrayList;

public class AccountMethod {
    public static ArrayList<String> getAccountNames(Context context){
        DataBase db=new DataBase(context);
        ArrayList<String> names=new ArrayList<String>();
        String colmn=DataBase.ACCOUNT_COL_NAME;
        try{
          Cursor cursor= db.getData(colmn,DataBase.TABLE_ACCOUNT,"");
          while (cursor.moveToNext()){
              names.add(cursor.getString(0));
          }

        }catch (Exception e){

        }
        return names;
    }
    public static void initAccount(Context context){
        DataBase db=new DataBase(context);
        Cursor cursor= db.getData("COUNT(*) AS rec ",DataBase.TABLE_ACCOUNT,"");
        int rec=0;
        if (cursor.moveToNext()) {
            rec = cursor.getInt(0);
        }
        if (rec==0) {
            String[] names = {"Personal", "Skycode", "Skycode Bank", "ADS", "Bank", "CCP", "Other"};
            for (String name : names) {
                ContentValues values = new ContentValues();
                values.put(DataBase.ACCOUNT_COL_NAME, name);
                values.put(DataBase.ACCOUNT_COL_DESCRIPTION, "this Account " + name);
                db.INSERT(values, DataBase.TABLE_ACCOUNT);
            }
        }

    }

    public static int getIdAccountByName(Context context,String name){
        DataBase db=new DataBase(context);

        String whereclose="WHERE "+DataBase.ACCOUNT_COL_NAME+"='"+name+"'";
        String col=DataBase.ACCOUNT_COL_ID;
        String table=DataBase.TABLE_ACCOUNT;
        int id=0;
        try {
            Cursor cursor= db.getData(col,table,whereclose);
            if (cursor.moveToNext()) {
                id = cursor.getInt(0);
            }
            return id;
        }catch (Exception e){
            return id;
        }


    }



}
