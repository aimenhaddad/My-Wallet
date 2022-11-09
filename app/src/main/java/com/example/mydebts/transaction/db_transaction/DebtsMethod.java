package com.example.mydebts.transaction.db_transaction;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.mydebts.transaction.DebtsModel;
import com.example.mydebts.DataBase;

import java.util.ArrayList;

public class DebtsMethod {
  // MYSQl
    public static ArrayList<DebtsModel> getItemListe(Context context, int id,boolean state ) {

        String col = DataBase.DEBTS_COL_ID+","+DataBase.DEBTS_COL_NAME+","+DataBase.DEBTS_COL_MONTANT
                +","+DataBase.DEBTS_COL_NOTE+","+DataBase.DEBTS_COL_TYPE+","+DataBase.DEBTS_COL_ETAT
                +","+DataBase.DEBTS_COL_TIME+","+DataBase.DEBTS_COL_DATE +","+DataBase.DEBTS_COL_ACCOUNT;

        String table = DataBase.TABLE_DEBTS;
        String where= "";

        if (!state){
            where=" AND "+DataBase.DEBTS_COL_ETAT+"=1";
        }

        String where_clos="WHERE "+DataBase.DEBTS_COL_ACCOUNT+" ="+id + where+" ORDER BY "+DataBase.DEBTS_COL_ID+" DESC";
        String sql ="SELECT "+col+" FROM "+table+" "+where_clos+" ;";
        Log.d("SQL", "getItemListe: "+sql);

     return selectItemDebts(context,col,table,where_clos);
    }
    public static ArrayList<DebtsModel> selectItemDebts(Context context,String col ,String table ,String whereclos){
        DataBase db=new DataBase(context);
        ArrayList<DebtsModel> Items=new ArrayList<DebtsModel>();
        try {
            Cursor cursor =db.getData(col,table,whereclos);
            while (cursor.moveToNext()){
                DebtsModel item=new DebtsModel(cursor.getInt(0),cursor.getString(1),cursor.getDouble(2),
                        cursor.getString(3),cursor.getInt(4),cursor.getInt(5),
                        cursor.getString(6),cursor.getString(7),cursor.getInt(8));
                Items.add(item);
            }
            return Items;
        } catch (Exception ex) {
            Log.d("Error", "select Item Debts : "+ex.getMessage());
            return Items;
        }
    }
}
