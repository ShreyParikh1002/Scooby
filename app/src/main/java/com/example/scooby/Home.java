package com.example.scooby;

import android.os.Build;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private firestore_adapter fsadapter;
    ArrayList<task_struc> fsdataliist=new ArrayList<>();
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
//        Query query = taskRef;
//        firestorestruc f=new firestorestruc("a","b","c");
////        taskRef.document("15-02-2023").set(f);
//        FirestoreRecyclerOptions<firestorestruc> options=new FirestoreRecyclerOptions.Builder<firestorestruc>()
//                .setQuery(query,firestorestruc.class)
//                .build();
//        Log.d("TAG", "DocumentSnapshot data: " + options);
//        fsadapter=new firestoreAdapter(options);
//        fsadapter.startListening();
//        fsrecycler.setAdapter(fsadapter);
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
//        db.collection("userid").document("date").collection("09-02-2023").document("tasks")
//                .set

        fsrecycler.setLayoutManager(new LinearLayoutManager(getContext()));
//        fsdataliist=new ArrayList<>();
        fsadapter=new firestore_adapter(fsdataliist);
        fsrecycler.setAdapter(fsadapter);

        db.collection("task").document("07-02-2023").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> users = document.getData();
                            for(Object d:users.values()){
                                ArrayList<HashMap<String,String>> a=new ArrayList<>();
                                a= (ArrayList<HashMap<String,String>>) d;
                                for(int i=0;i<a.size();i++){
//                                    Map<String,String>=a.get(i);
                                    String p,q,r;
                                    p=a.get(i).get("task");
                                    q=a.get(i).get("time");
                                    r=a.get(i).get("tag");
                                    fsdataliist.add(new task_struc(p,q,r));
//                                    HashMap<String,String>
//                                    a.get(i).get("tag");
                                Log.i("tag",a.get(i).get("tag").getClass().getSimpleName()+"");
                                }
                            }
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                users.forEach((k, v) ->
//                                        firestorestruc obj=v.toObject(firestorestruc.class);
//                                            fsdataliist.add(obj)
//                                );
//                            }
//                                Log.i("TAG", users.get(0).get(users.get(0).keySet().toArray()[0]));

                        }
                    }
                                    fsadapter.notifyDataSetChanged();
                });

            Log.i("tag",fsdataliist.size()+"");
//        for (int i = 0; i < fsdataliist.size(); i++)

            // Printing and display the elements in ArrayList
//        fsdataliist.add(new task_struc("a","b","c"));
        fsadapter.notifyDataSetChanged();
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        fsadapter.startListening();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        fsadapter.stopListening();
//    }
}