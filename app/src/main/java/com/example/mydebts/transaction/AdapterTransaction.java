package com.example.mydebts.transaction;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydebts.TOOLS.Tools;
import com.example.mydebts.R;

import java.util.ArrayList;

public class AdapterTransaction extends RecyclerView.Adapter<AdapterTransaction.MyViewHolder>   {

 Activity activity;
 Context context;
 ArrayList<DebtsModel> Items;

    /*_________________Clicke of DebtsList________________*/
    public OnRecyclerViewClick listener;
    public interface OnRecyclerViewClick{
        void OnItemClick(int position );
    }
    public void OnRecyclerViewClick(OnRecyclerViewClick listener) {
        this.listener = listener;
    }

    public AdapterTransaction(Activity activity, Context context, ArrayList<DebtsModel> Items ){
        this.activity=activity;
        this.context=context;
        this.Items=Items;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item, parent, false);
        return new MyViewHolder(view,listener);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DebtsModel items= Items.get(position);
        holder.NOM.setText(items.NOM);
        holder.MONTANT.setText( Tools.MaskMoney(items.MONTANT));
        holder.NOTE.setText(items.NOTE);
        holder.DATE.setText(items.DATE);
        if (items.TYPE==1){
            holder.TYPE.setImageResource(R.drawable.ic_call_made);
            holder.MONTANT.setTextColor(Color.parseColor("#F83F3D"));
        }else{
            holder.TYPE.setImageResource(R.drawable.ic_call_received);
            holder.MONTANT.setTextColor(Color.parseColor("#4CAF50"));
        }


    }

    @Override
    public int getItemCount() {
        return Items.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView NOM,MONTANT,NOTE,DATE;
        ImageView TYPE;
        LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView,OnRecyclerViewClick listener) {
            super(itemView);
            NOM = itemView.findViewById(R.id.NOM);
            MONTANT = itemView.findViewById(R.id.MONTANT);
            NOTE = itemView.findViewById(R.id.NOTE);
            TYPE = itemView.findViewById(R.id.TYPE);
            DATE = itemView.findViewById(R.id.DATE);
            linearLayout = itemView.findViewById(R.id.item);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener!= null && getAdapterPosition()!= RecyclerView.NO_POSITION ){
                        listener.OnItemClick(getAdapterPosition());
                    }
                }
            });


        }
    }






}
