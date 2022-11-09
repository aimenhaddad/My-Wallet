package com.example.mydebts.TOOLS;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.mydebts.DataBase;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

public class Tools {
    public static String CurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static String CurrentTime() {
        return new SimpleDateFormat("HH:mm").format(new Date());
    }

    public static String MaskMoney(double prix) {
        Locale locale;
        locale = new Locale("fr", "DZ");
        Currency currency = Currency.getInstance(locale);
        NumberFormat forma = NumberFormat.getCurrencyInstance(locale);
        forma.setCurrency(currency);
        return forma.format(prix);
    }
    public static String DecimalFormat(Double price){
        String result="",prix;
        DecimalFormat decim = new DecimalFormat("#.00");
        prix=decim.format(price);
        int length=prix.length();
        for (int i = 0; i < length; i++) {
            if (i==length-3){
                result+= ".";
            }else{
                result+=prix.charAt(i);
            }

        }

        return  result;
    }


    public static double balance(Context context,int id_account ,boolean state) {

        String where_close_revenus  = " WHERE " + DataBase.DEBTS_COL_TYPE + "=0 AND "+DataBase.DEBTS_COL_ACCOUNT + "=" +id_account;
        String where_close_depenses = " WHERE " + DataBase.DEBTS_COL_TYPE + "=1 AND "+DataBase.DEBTS_COL_ACCOUNT + "=" +id_account;
        String where_close="";
        String col="";

        String table=DataBase.TABLE_DEBTS;
        col=" SUM("+DataBase.DEBTS_COL_MONTANT+") AS solde ";

        if (!state) {
            where_close = " AND " +DataBase.DEBTS_COL_ETAT + "=1 " ;
        }

        where_close_revenus += where_close;
        where_close_depenses += where_close;

        double balance_revenus  = 0;
        double balance_depenses = 0;

        balance_revenus  = calculateBalance(context,col,table,where_close_revenus);
        balance_depenses = calculateBalance(context,col,table,where_close_depenses);


        return balance_revenus- balance_depenses;
    }
    public  static double calculateBalance(Context context,String col ,String table ,String whereclos){
        DataBase db=new DataBase(context);
        double balance=0;
        try {
            Cursor res = db.getData(col,table,whereclos);
            while (res.moveToNext()) {
                balance = res.getDouble(0);
            }
            return balance;
        } catch (Exception e) {
            Log.d("Error", "calculate Balance : " + e.getMessage());
            return balance;
        }

    }


}
