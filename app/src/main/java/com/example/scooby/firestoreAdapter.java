package com.example.scooby;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class firestoreAdapter extends FirestoreRecyclerAdapter<firestorestruc,firestoreAdapter.taskHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public firestoreAdapter(@NonNull FirestoreRecyclerOptions<firestorestruc> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull taskHolder holder, int position, @NonNull firestorestruc
            model) {
        holder.fstime.setText(model.getTime());
        holder.fstag.setText(model.getTag());
        holder.fstask.setText(model.getTask());
    }

    @NonNull
    @Override
    public taskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.firestore_card,
                parent,false);
        return new taskHolder(v);
    }

    class taskHolder extends RecyclerView.ViewHolder {
        TextView fstime,fstag,fstask;
        public taskHolder(@NonNull View itemView) {

            super(itemView);
            fstime=itemView.findViewById(R.id.firestoretime);
            fstag=itemView.findViewById(R.id.firestoretag);
            fstask=itemView.findViewById(R.id.firestoretask);
        }
    }
}
