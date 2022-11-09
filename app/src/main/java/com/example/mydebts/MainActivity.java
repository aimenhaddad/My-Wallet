package com.example.mydebts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mydebts.transaction.AdapterTransaction;
import com.example.mydebts.transaction.db_transaction.DebtsMethod;
import com.example.mydebts.transaction.DebtsModel;
import com.example.mydebts.TOOLS.Tools;
import com.example.mydebts.account.AccountMethod;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    /*---------------------Data base ------------------------*/
    Activity  activity= MainActivity.this;;
    Context context=MainActivity.this;
    DataBase db =new DataBase(context);
    //*************************

    ArrayList<DebtsModel> Items =new ArrayList<DebtsModel>();
    AdapterTransaction adapterTransaction;


    RecyclerView recyclerview;
    /** declartion */

    double balance=0;
    TextView tv_balance;
    Switch  sw_balance;
    boolean state_balance=false;


    FloatingActionButton btn_add_new_item;
    /** part  add item  */
    AlertDialog alertDialog;
    Button save;
    Button cancel;

    Switch  type;
    Switch  etat;


    EditText nom;
    EditText montant;
    EditText note;

    // vriable
    String Nom;
    String Note="" ;
    String  Date;
    String  Time ;

    double Montant;
    int Type,Etat;

    /** part  update item  */

    /** part  delete item  */

    /**Date Picker & Time Picker  */
    private DatePickerDialog datePickerDialog;
    Button dateButton,timeButton;
    int hour, minute;
    /** Managment ACCOUNT  */
    Spinner sp_account;
    ArrayList<String> account_list_name=new ArrayList<String>() ;
    int id_account=1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*---------------------Hooks------------------------*/
        recyclerview =(RecyclerView) findViewById(R.id.Debts_liste);

        btn_add_new_item=(FloatingActionButton) findViewById(R.id.btn_add_new_item);

        tv_balance=findViewById(R.id.tv_balance);
        sw_balance=findViewById(R.id.sw_balance);

        sp_account=(Spinner) findViewById(R.id.sp_account);

        // Solde
        balance= Tools.balance(context,id_account,state_balance);
        tv_balance.setText(Tools.MaskMoney(balance));

        sw_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state_balance=sw_balance.isChecked();
                balance= Tools.balance(context,id_account,state_balance);
                tv_balance.setText(Tools.MaskMoney(balance));
                updateTransaction(DebtsMethod.getItemListe(context,id_account,state_balance));
            }
        });

        // switch Account
        AccountMethod.initAccount(context);
        account_list_name.addAll(AccountMethod.getAccountNames(context));
        ArrayAdapter sp_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, account_list_name);
        sp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_account.setAdapter(sp_adapter);
        sp_account.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // swap account
                String name=sp_account.getSelectedItem().toString();
                id_account= AccountMethod.getIdAccountByName(context,name);
                // update recycler view
                updateTransaction(DebtsMethod.getItemListe(context,id_account,state_balance));
                // update balance
                balance= Tools.balance(context,id_account,state_balance);
                tv_balance.setText(Tools.MaskMoney(balance));
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Display DATA
        initTransaction(context,id_account);
        // add new
        btn_add_new_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               muneAddNewTransaction(context);
            }
        });

        // update total
        adapterTransaction.OnRecyclerViewClick(new AdapterTransaction.OnRecyclerViewClick() {
            @Override
            public void OnItemClick(int position) {
                MuneUpdateTransaction(context,position);

            }
        });
    }//end

    //mune option Transaction
    private void MuneUpdateTransaction(Context context,int position) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.buttom_menu_option);

        /*----------------------Hooks -----------------------*/
        LinearLayout updateLayout = dialog.findViewById(R.id.update_transaction);
        Switch swap_state = dialog.findViewById(R.id.swap_state);
        LinearLayout deletLayout = dialog.findViewById(R.id.delete);
        if (Items.get(position).ETAT==1){
            swap_state.setChecked(true);
        }
        /*----------------------update transaction-----------------------*/
        updateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });
        /*-----------------------swap state----------------------*/
        swap_state.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                boolean state=swap_state.isChecked();
                ContentValues values=new ContentValues();
                String whereClause="";
                if (state){

                    values.put(DataBase.DEBTS_COL_ETAT,1);
                     whereClause=DataBase.DEBTS_COL_ID+"="+Items.get(position).ID;
                }else{
                    values.put(DataBase.DEBTS_COL_ETAT,0);
                     whereClause=DataBase.DEBTS_COL_ID+"="+Items.get(position).ID;
                }
                try {
                    db.UPDATE(values,DataBase.TABLE_DEBTS,whereClause);
                    if (!state_balance && !state ) {
                        Items.remove(position);
                        adapterTransaction.notifyDataSetChanged();
                        dialog.dismiss();
                    }

                }catch (Exception e){
                    Log.d("Error Sql", "update state : "+e.getMessage());
                }

            }
        });
        /*--------------------Delette-------------------------*/
        deletLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                int id= Items.get(position).ID;
                try {
                    alaretDelete(MainActivity.this,id,position);
                }catch (Exception e){
                    Log.d("Exception", "onClick: "+e.getMessage());
                    dialog.dismiss();
                }
            }
        });

        /*---------------------show dialog------------------------*/
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }//end

    public void updateTransaction(ArrayList<DebtsModel> viewModels) {
        Items.clear();
        Items.addAll(viewModels);
        adapterTransaction.notifyDataSetChanged();
    }

    public void  initTransaction(Context context,int id_account) {
        adapterTransaction = new AdapterTransaction(activity, context,Items);
        recyclerview.setAdapter(adapterTransaction);
        recyclerview.setLayoutManager(new LinearLayoutManager(context));
    }//end

    private void muneAddNewTransaction(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.buttom_menu_add_transaction);
        /*----------------------Hooks -----------------------*/
        LinearLayout ll_depenses = dialog.findViewById(R.id.ll_depenses);
        LinearLayout ll_revenus = dialog.findViewById(R.id.ll_revenus);

        /*-----------------------revenus----------------------*/
        ll_revenus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                newTransaction(0);
                dialog.dismiss();
            }
        });
        /*--------------------depenses-------------------------*/
        ll_depenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                newTransaction(1);
                dialog.dismiss();
            }
        });

        /*---------------------show dialog------------------------*/
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }//end

    @SuppressLint("MissingInflatedId")
    public void newTransaction(int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.model_dialog_add_item, null);

        // date & time
        initDatePicker();
        dateButton = view1.findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());

        timeButton = view1.findViewById(R.id.timeButton);
        timeButton.setText(Tools.CurrentTime());
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popTimePicker(view);
            }
        });

        // confirmation
        save = (Button) view1.findViewById(R.id.validate);
        cancel = (Button) view1.findViewById(R.id.cancel);

        etat= view1.findViewById(R.id.etat);
        // input
        nom=view1.findViewById(R.id.nom);
        montant=view1.findViewById(R.id.montant);
        note=view1.findViewById(R.id.note);
        TextWatcher textWatchernom,textWatchermontant;

        textWatchernom=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                Nom= String.valueOf(nom.getText());
                testNom(Nom);
            }
        };
        nom.addTextChangedListener(textWatchernom);

        textWatchermontant=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String payment = String.valueOf(montant.getText());
                testpayment(payment);

            }
        };
        montant.addTextChangedListener(textWatchermontant);


        //set view
        builder.setView(view1);
        //creat dialog
        alertDialog = builder.create();
        alertDialog.show();



        /*_____________________SAVE_____________________*/
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values=new ContentValues();
                try {
                    Nom=String.valueOf(nom.getText());
                    Montant =Double.parseDouble(String.valueOf(montant.getText()));
                    Note=String.valueOf(note.getText());
                    Date=String.valueOf(dateButton.getText());
                    Time=String.valueOf(timeButton.getText());
                    if (etat.isChecked()){Etat=1;}else {Etat=0; }
                  // add value in Content Value
                    values.put(DataBase.DEBTS_COL_NAME, Nom);
                    values.put(DataBase.DEBTS_COL_MONTANT, Montant);
                    values.put(DataBase.DEBTS_COL_NOTE, Note);
                    values.put(DataBase.DEBTS_COL_TYPE, type);
                    values.put(DataBase.DEBTS_COL_ETAT, Etat);
                    values.put(DataBase.DEBTS_COL_DATE, Date);
                    values.put(DataBase.DEBTS_COL_TIME, Time);
                    values.put(DataBase.DEBTS_COL_ACCOUNT,id_account);
                }catch (Exception exception){
                    Log.d("Error", "Exception get information : "+exception.getMessage());
                }
                boolean add=false;
                try {
                     add= db.INSERT(values,DataBase.TABLE_DEBTS);
                }catch (Exception ex){
                    Log.d("Error", "insert in data base : "+ex.getMessage());
                }
                if (add){
                    DebtsModel item=new DebtsModel(db.GetLastID(),Nom,Montant,Note,Type,Etat,Date,Time,id_account);
                    Items.add(0,item);
                    adapterTransaction.notifyDataSetChanged();

                    balance= Tools.balance(context,id_account,state_balance);
                    tv_balance.setText(Tools.MaskMoney(balance));

                    Toast.makeText(activity, "ADD SUCCESSFULLY ", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }else{
                    Toast.makeText(activity, "NOT ADDED", Toast.LENGTH_SHORT).show();
                }

            }
        });//end

        /*_____________________CANCEL_____________________*/
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                alertDialog.dismiss();
            }
        });// end


    }//end

    // verification champ
    private  Boolean testNom(String name){
        try {
            if (name.isEmpty()) {
                nom.setError("It can't be empty");
                save.setEnabled(false);
                return false;
            }else{
                save.setEnabled(true);
                return true;
            }
        }catch (Exception ex){
            Log.d("Error", "test Name: "+ex.getMessage());
            nom.setError("This invalid name");
            save.setEnabled(false);
            return false;
        }
    }
    private  Boolean testpayment(String payment){
        try {
            if (payment.isEmpty()) {
                montant.setError("It can't be empty");
                save.setEnabled(false);
                return false;
            }else {
                if (Double.parseDouble(payment) == 0.0){
                    montant.setError("He can't be '0.00'");
                    save.setEnabled(false);
                    return false;
                }else{
                    save.setEnabled(true);
                    return  true;
                }
            }
        } catch (NumberFormatException ex) {
            Log.d("NumberFormatException", "testpayment: "+ex.getMessage());
            save.setEnabled(false);
            montant.setError("This invalid number");
            return false;
        }
    }

    public void alaretDelete(Context context , int ID, int position){
        MaterialAlertDialogBuilder alarte = new MaterialAlertDialogBuilder(context);
        alarte.setTitle("CONFIRMATION");
        alarte.setMessage("Are you sure you want to delete this?");
        alarte.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean delete=db.DELETE(ID);
                if (delete) {
                    Items.remove(position);
                    adapterTransaction.notifyDataSetChanged();

                    balance= Tools.balance(context,id_account,state_balance);
                    tv_balance.setText(Tools.MaskMoney(balance));

                    Toast.makeText(context, "Delete Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alarte.setNegativeButton("Nope", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });
        alarte.show();

    }


    // Date Picker & Time Picker
    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        // datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }
    private String makeDateString(int day, int month, int year) {
        return   year+ "-" + month + "-" +day;
    }
    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }
    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

}