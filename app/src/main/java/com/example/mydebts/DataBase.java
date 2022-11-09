package com.example.mydebts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DataBase extends SQLiteOpenHelper {

   public static final String DB_NAME="data.DEBTS";
   public static final String TABLE_DEBTS="DEBTS";


    public static final String DEBTS_COL_ID="ID";
    public static final String DEBTS_COL_NAME="NOM";
    public static final String DEBTS_COL_MONTANT="MONTANT";
    public static final String DEBTS_COL_NOTE="NOTE";
    public static final String DEBTS_COL_TYPE="TYPE";
    public static final String DEBTS_COL_ETAT="ETAT";
    public static final String DEBTS_COL_DATE="DATE";
    public static final String DEBTS_COL_TIME="TIME";
    public static final String DEBTS_COL_ACCOUNT="ID_ACCOUNT";


    public static final String TABLE_ACCOUNT="ACCOUNT";
    public static final String ACCOUNT_COL_ID="ID";
    public static final String ACCOUNT_COL_NAME="NAME";
    public static final String ACCOUNT_COL_DESCRIPTION="DESCRIPTION";








    /*-----------------------------------TABLE_DEBTS-------------------------------------*/
    // ID,NOM,MONTANT,NOTE,TYPE,ETAT,DATE,TIME
    public static final String CREATE_DEBTS ="CREATE TABLE "+TABLE_DEBTS+" ("
            + DEBTS_COL_ID+"                INTEGER        PRIMARY KEY AUTOINCREMENT,"
            + DEBTS_COL_NAME+"               VARCHAR (50) ,"
            + DEBTS_COL_MONTANT+"           DECIMAL (24, 6),"
            + DEBTS_COL_NOTE+"              VARCHAR (255),"
            + DEBTS_COL_TYPE+"              INTEGER DEFAULT 0,"
            + DEBTS_COL_ETAT+"              INTEGER DEFAULT 0,"
            + DEBTS_COL_DATE +"             DATE,"
            + DEBTS_COL_TIME +"             TIME,"
            + DEBTS_COL_ACCOUNT+"           INTEGER ) ;";
    /*-----------------------------------TABLE_ACCOUNT-------------------------------------*/

    public static final String CREATE_ACCOUNT ="CREATE TABLE "+TABLE_ACCOUNT+" ("
            + ACCOUNT_COL_ID +"                INTEGER       PRIMARY KEY AUTOINCREMENT,"
            + ACCOUNT_COL_NAME + "             VARCHAR (50)  UNIQUE,"
            + ACCOUNT_COL_DESCRIPTION +"       VARCHAR (255) ) ;";

    /*-----------------------------------CREATE DATABASE -------------------------------------*/

    public DataBase(Context context ) {
        super(context, DB_NAME,null,1);
    }

    /*-----------------------------------onCreate-------------------------------------*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DEBTS);
        db.execSQL(CREATE_ACCOUNT);
    }

    /*-----------------------------------onUpgrade-------------------------------------*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="DROP TABLE IF  EXISTS "+TABLE_DEBTS;
        db.execSQL(sql);
        sql="DROP TABLE IF EXISTS "+TABLE_ACCOUNT;
        db.execSQL(sql);
    }

    /*---------------------------------------------------------METHODE---------------------------------------------------------*/

    /*----------------------isExists--------------------------*/
    public boolean isExists(String sql){
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor res = db.rawQuery(sql,null);
        if(res.getCount()==0) {
            return false;
        }else {
            return true;
        }
    }//end

    /*-----------------------Insert---------------------------*/

    public boolean INSERT(ContentValues values  ,String TABLE_NAME){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TABLE_NAME, null, values)>0;
    }


    /*-----------------------Update---------------------------*/
    public boolean UPDATE (ContentValues values  ,String TABLE_NAME,String whereClause){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TABLE_NAME, values,whereClause, null)>0;
    }
    /*----------------------------DELETE----------------------*/
    public boolean DELETE(int ID){
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause="ID="+ID;
        return db.delete(TABLE_DEBTS, whereClause, null)== 1;
    }

    /*----------------getDATA-------------------*/
    public Cursor GetData(String sql){

        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=null;
        if (db!=null){
            cursor=db.rawQuery(sql,null);
        }
        return cursor;
    }//end
    public Cursor getData(String colmn,String TABLE_NAME,String whereclose){
        String sql ="SELECT "+colmn+" FROM "+TABLE_NAME+" "+whereclose+" ;";
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=null;
        if (db!=null){
            Log.d("TAG", "getData: "+sql);
            cursor=db.rawQuery(sql,null);
        }
        return cursor;
    }//end
    /*----------------last ID-------------------*/
    public int GetLastID(){
        int ID=0;
        String sql="SELECT MAX(ID)  FROM DEBTS ;";
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=null;
        if (db!=null){
            cursor=db.rawQuery(sql,null);
            if (cursor.moveToNext()){
                ID=cursor.getInt(0);
            }

        }
        return ID;
    }//end

}