package com.example.scooby;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class task_recycler_adapter extends RecyclerView.Adapter<task_recycler_adapter.ViewHolder> {
    Context context;
    ArrayList<task_struc> task_collection;
    task_recycler_adapter(Context context,ArrayList<task_struc> task_collection){
        this.context=context;
        this.task_collection=task_collection;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if attached true recycler view can't detach it to send it to bottom to reuse
        View v=LayoutInflater.from(context).inflate(R.layout.inputcard,parent,false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.task.setText(task_collection.get(position).task);
        holder.tag.setText(task_collection.get(position).tag);
        holder.time.setText(task_collection.get(position).time);
    }

    @Override
    public int getItemCount() {
        return task_collection.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView task,time,tag;
//        Spinner tag;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            task=itemView.findViewById(R.id.task1);
            time=itemView.findViewById(R.id.time1);
            tag=itemView.findViewById(R.id.spinner1);
        }
    }
}
