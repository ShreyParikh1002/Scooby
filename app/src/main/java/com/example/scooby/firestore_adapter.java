package com.example.scooby;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class firestore_adapter extends RecyclerView.Adapter<firestore_adapter.fsviewholder> {


    ArrayList<task_struc> fsdatalist;

    public firestore_adapter(ArrayList<task_struc> fsdatalist) {
        this.fsdatalist = fsdatalist;
    }

    @NonNull
    @Override
    public fsviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.firestore_card,
                parent,false);
        return new fsviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull fsviewholder holder, int position) {
        if(fsdatalist.get(position).getTask()==""){
            holder.fshour.setText(fsdatalist.get(position).getHour());
            holder.fstime.setText(fsdatalist.get(position).getTime());
            holder.fstask.setText(fsdatalist.get(position).getTask());
            holder.fstime.setBackgroundColor(Color.parseColor("#00000000"));
            holder.fstask.setBackgroundColor(Color.parseColor("#00000000"));
            holder.fstag.setBackgroundColor(Color.parseColor("#00000000"));
            holder.fscard.setBackgroundColor(Color.parseColor("#00000000"));
        }
        if(fsdatalist.get(position).getTask()!=""){
            holder.fstime.setText(fsdatalist.get(position).getTime());
            holder.fstag.setText(fsdatalist.get(position).getTag());
            holder.fstask.setText(fsdatalist.get(position).getTask());
            holder.fshour.setBackgroundColor(Color.parseColor("#00000000"));
            holder.fscard.setBackgroundColor(Color.parseColor("#00000000"));
            holder.bglinear.setBackgroundResource(R.drawable.card_corners);
        }
    }

    @Override
    public int getItemCount() {
        return fsdatalist.size();
    }

    class fsviewholder extends RecyclerView.ViewHolder{
        TextView fstime,fstag,fstask,fshour;
        CardView fscard;
        LinearLayout bglinear;
        public fsviewholder(@NonNull View itemView) {
            super(itemView);
            fscard=itemView.findViewById(R.id.card);
            fstime=itemView.findViewById(R.id.firestoretime);
            fstag=itemView.findViewById(R.id.firestoretag);
            fstask=itemView.findViewById(R.id.firestoretask);
            fshour=itemView.findViewById(R.id.firestorehour);
            bglinear=itemView.findViewById(R.id.bglinear);
        }
    }
}
