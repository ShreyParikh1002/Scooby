package com.example.scooby;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference taskRef=db.collection("task2");
    private firestoreAdapter fsadapter;
    RecyclerView fsrecycler;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v= inflater.inflate(R.layout.fragment_home, container, false);
        fsrecycler=v.findViewById(R.id.firestoreRecycler);
        setUpRecyclerView();
        return v;
    }


    private void setUpRecyclerView(){
        Query query = taskRef;
        firestorestruc f=new firestorestruc("a","b","c");
//        taskRef.document("15-02-2023").set(f);
        FirestoreRecyclerOptions<firestorestruc> options=new FirestoreRecyclerOptions.Builder<firestorestruc>()
                .setQuery(query,firestorestruc.class)
                .build();
        Log.d("TAG", "DocumentSnapshot data: " + options);
        fsadapter=new firestoreAdapter(options);


//        fsrecycler.setHasFixedSize(true);
        fsadapter.startListening();
        fsrecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        fsrecycler.setAdapter(fsadapter);
//        taskRef.document("07-02-2023").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                    } else {
//                        Log.d("TAG", "No such document");
//                    }
//                } else {
//                    Log.d("TAG", "get failed with ", task.getException());
//                }
//            }
//        });
    }
//
    @Override
    public void onStart() {
        super.onStart();
        fsadapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        fsadapter.stopListening();
    }
}