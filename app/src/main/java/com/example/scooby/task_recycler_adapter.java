package com.example.scooby;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class task_recycler_adapter extends RecyclerView.Adapter<task_recycler_adapter.ViewHolder> {
    Context context;
    ArrayList<task_struc> task_collection;
    ArrayList<String> courses;


//    ArrayAdapter ad
//            = new ArrayAdapter(
//            this,
//            android.R.layout.simple_spinner_item,
//            courses);

    // set simple layout resource file
    // for each item of spinner
//        ad.setDropDownViewResource(
//    android.R.layout
//            .simple_spinner_dropdown_item);


    task_recycler_adapter(Context context,ArrayList<task_struc> task_collection, ArrayList<String> courses){
        this.context=context;
        this.task_collection=task_collection;
        this.courses=courses;

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

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, courses);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.tag.setAdapter(arrayAdapter);
        holder.task.setText(task_collection.get(position).task);
        holder.tag.setSelection(arrayAdapter.getPosition((task_collection.get(position).tag)));
        holder.time.setText(task_collection.get(position).time);
        holder.time.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                task_collection.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                return true;
            }
        });
        holder.ll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
//                AlertDialog.Builder confirmDelete=new AlertDialog.Builder(context)
//                        .setTitle("Delete card")
//                        .setMessage("Are you sure you want to delete this entry?")
//                        .setIcon(R.drawable.ic_baseline_delete_24)
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
                                task_collection.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
//                            }
//                        })
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        });
//                confirmDelete.show();
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return task_collection.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText task,time;
        Spinner tag;
        LinearLayout ll;
//        Spinner tag;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            task=itemView.findViewById(R.id.task1);
            time=itemView.findViewById(R.id.time1);
            tag=itemView.findViewById(R.id.spinner1);
            ll =itemView.findViewById(R.id.window_content1);
        }
    }
}
