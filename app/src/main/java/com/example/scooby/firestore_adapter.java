package com.example.scooby;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
        holder.fstime.setText(fsdatalist.get(position).getTime());
        holder.fstag.setText(fsdatalist.get(position).getTag());
        holder.fstask.setText(fsdatalist.get(position).getTask());
    }

    @Override
    public int getItemCount() {
        return fsdatalist.size();
    }

    class fsviewholder extends RecyclerView.ViewHolder{
        TextView fstime,fstag,fstask;
        public fsviewholder(@NonNull View itemView) {
            super(itemView);
            fstime=itemView.findViewById(R.id.firestoretime);
            fstag=itemView.findViewById(R.id.firestoretag);
            fstask=itemView.findViewById(R.id.firestoretask);
        }
    }
}
