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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public
class Home extends Fragment
{

    private
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private
    firestore_adapter fsadapter;
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<task_struc> fsdatalist = new ArrayList<>();
    RecyclerView fsrecycler;
    View v;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private
    static final String ARG_PARAM1 = "param1";
    private
    static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private
    String mParam1;
    private
    String mParam2;

    public
    Home()
    {
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
    public
    static Home newInstance(String param1, String param2)
    {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_home, container, false);
        fsrecycler = v.findViewById(R.id.firestoreRecycler);
        setUpRecyclerView();
        current();
        return v;
    }

    private
    void setUpRecyclerView()
    {
        Date date = Calendar.getInstance().getTime();
        //        small mm is minutes
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        DateFormat timeFormat = new SimpleDateFormat("kk");
        String strDate = dateFormat.format(date);
        String strTime = timeFormat.format(date);
        int intTime = Integer.parseInt(strTime);

        if (intTime == 24)
        {
            int prevdate = (Integer.parseInt(strDate.substring(0, 2)) - 1);
            if (prevdate < 10)
            {
                strDate = "0" + prevdate + strDate.substring(2);
            }
            else
            {
                strDate = prevdate + strDate.substring(2);
            }
        }

        fsrecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        db.collection("users").document(user.getUid()).collection("data").document(strDate).get().addOnCompleteListener(task->{
            if (task.isSuccessful())
            {
                DocumentSnapshot document = task.getResult();
                if (document.exists())
                {
                    fsdatalist.addAll(document.toObject(retrieve_POJO.class).task);
                    Collections.sort(fsdatalist, new Comparator<task_struc>() {
                        @Override
                        public int compare(task_struc t1, task_struc t2) {
                            if(t1.hour==t2.hour) {
                                return 0;
                            }
                            return Integer.parseInt(t1.hour) < Integer.parseInt(t2.hour) ? -1 : 1;
                        }
                    });
                    for(int i=fsdatalist.size()-1;i>=0;i--){
                        if(Integer.parseInt(fsdatalist.get(i).getHour())>(intTime+1)){
                            Log.i("TAG", fsdatalist.get(i).getHour()+" "+fsdatalist.get(i).getTask()+" "+fsdatalist.get(i).getTag());
                            fsdatalist.remove(i);
                        }
                        else{
                            break;
                        }
                    }
                    fsadapter = new firestore_adapter(fsdatalist);
                    fsrecycler.setAdapter(fsadapter);

//                    Original database design
//                    New design currently in testing phase so kept as backup
//***********************************************************************************************************************************************************
//                    Log.i("tag", fsdatalist.get(0).getHour() );
//                    users = new TreeMap<String, Object>(users);
//                    //                            Collections.sort(users);
//                    for (String d : users.keySet())
//                    {
//                        Log.i("TAG", d + "");
//                    }
//                    for (Object d : users.values())
//                    {
//                        ArrayList<Map<String, String>> a = new ArrayList<>();
//                        a = (ArrayList<Map<String, String>>)d;
//                        for (int i = 0; i < a.size(); i++)
//                        {
//                            //                                    Map<String,String>=a.get(i);
//                            String p, q, r, s;
//                            p = a.get(i).get("task");
//                            q = a.get(i).get("time");
//                            r = a.get(i).get("tag");
//                            s = a.get(i).get("hour");
////                            if (Integer.parseInt(s) <= intTime + 1)
////                            {
//                                fsdataliist.add(new task_struc(p, q, r, s));
////                            }
////                            else
////                            {
////                                break;
////                            }
//                            //                                    HashMap<String,String>
//                            //                                    a.get(i).get("tag");
//                        }
//                        Log.i("tag", a + "");
//                    }
                    //                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //                                users.forEach((k, v) ->
                    //                                        firestorestruc obj=v.toObject(firestorestruc.class);
                    //                                            fsdataliist.add(obj)
                    //                                );
                    //                            }
                    //                                Log.i("TAG", users.get(0).get(users.get(0).keySet().toArray()[0]));
//***********************************************************************************************************************************************************
                }
            }
        });
    }

    public
    void current()
    {
        Spinner tagcur = v.findViewById(R.id.currentspinner);
        TextView timecur = v.findViewById(R.id.currenttime);
        TextView taskcur = v.findViewById(R.id.currenttask);
        ImageButton submitcur = v.findViewById(R.id.currentsubmit);
        ArrayList<String> courses = new ArrayList<String>();
        courses.add("");
        courses.add("DSA");
        courses.add("Class");
        courses.add("Development");
        courses.add("Necessity");
        courses.add("Morning");
        courses.add("Wasted");
        courses.add("Binging");
        courses.add("Enjoyment");
        courses.add("Food");
        courses.add("Studying");
        courses.add("Creative proc");
        courses.add("Family");
        courses.add("Applying/searching");
        courses.add("Friends/room");
        courses.add("Extra Sleep");
        courses.add("Exercise");
        ArrayAdapter ad = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, courses);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagcur.setAdapter(ad);

        submitcur.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view)
            {
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                DateFormat timeFormat = new SimpleDateFormat("kk");
                String strDate = dateFormat.format(date);
                String strTime = timeFormat.format(date);
                int intTime=Integer.parseInt(strTime);

                int proceed = 1;

                String getTask, getTag, getTime;

                getTask = taskcur.getText().toString();
                getTime = timecur.getText().toString();
                getTag = tagcur.getSelectedItem().toString();

                if (getTask == "" || getTime == "" || getTag == "")
                {
                    Toast.makeText(getContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
                    proceed = 0;
                }

                if (proceed == 1)
                {
                    DocumentReference id = db.collection("users").document(user.getUid()).collection("data").document(strDate);
                    String finalStrTime = strTime;
                    String finalStrTime1 = strTime;
                    id.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override public void onComplete(@NonNull Task<DocumentSnapshot> task)
                        {
                            if (task.isSuccessful())
                            {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists())
                                {
                                    Log.i("TAG", "Document exists!");
                                    id.update("task", FieldValue.arrayUnion(new task_struc(getTask,getTime,getTag, finalStrTime1)));
                                    fsdatalist.add(new task_struc(getTask,getTime,getTag, finalStrTime1));
                                    Collections.sort(fsdatalist, new Comparator<task_struc>() {
                                        @Override
                                        public int compare(task_struc t1, task_struc t2) {
                                            if(t1.hour==t2.hour) {
//                                            Log.i("TAG", t1.hour+" "+t2.hour);
                                                return 0;
                                            }
                                            return Integer.parseInt(t1.hour) < Integer.parseInt(t2.hour) ? -1 : 1;
                                        }
                                    });
                                    for(int i=fsdatalist.size()-1;i>=0;i--){
                                        if(Integer.parseInt(fsdatalist.get(i).getHour())>(intTime+1)){
                                            Log.i("TAG", fsdatalist.get(i).getHour()+" "+fsdatalist.get(i).getTask()+" "+fsdatalist.get(i).getTag());
                                            fsdatalist.remove(i);
                                        }
                                        else{
                                            break;
                                        }
                                    }
                                    fsadapter = new firestore_adapter(fsdatalist);
                                    fsrecycler.setAdapter(fsadapter);
//                                    fsadapter.notifyDataSetChanged();
                                    taskcur.setText("");
                                    timecur.setText("");
                                    tagcur.setSelection(0);
                                }
                                else
                                {
                                    Log.i("TAG", "Document does not exist!");
                                    ArrayList<task_struc> empty=new ArrayList<>();
                                    Map<String, ArrayList<task_struc>> initialise = new HashMap<>();

                                    for(int i=8;i<=24;i++){
                                        empty.add(new task_struc("", "", "0",Integer.toString(i)));
                                    }
                                    fsdatalist.addAll(empty);
                                    initialise.put("task",empty);
                                    id.set(initialise, SetOptions.merge());

                                    id.update("task", FieldValue.arrayUnion(new task_struc(getTask,getTime,getTag, finalStrTime1)));
                                    fsdatalist.add(new task_struc(getTask,getTime,getTag, finalStrTime1));
                                    Collections.sort(fsdatalist, new Comparator<task_struc>() {
                                        @Override
                                        public int compare(task_struc t1, task_struc t2) {
                                            Log.i("TAG", t1.hour);
                                            if(t1.hour==t2.hour) {
                                                return 0;
                                            }
                                            return Integer.parseInt(t1.getHour()) < Integer.parseInt(t2.getHour()) ? -1 : 1;
                                        }
                                    });
                                    for(int i=fsdatalist.size()-1;i>=0;i--){
                                        if(Integer.parseInt(fsdatalist.get(i).getHour())>(intTime+1)){
                                            Log.i("TAG", fsdatalist.get(i).getHour()+" "+fsdatalist.get(i).getTask()+" "+fsdatalist.get(i).getTag());
                                            fsdatalist.remove(i);
                                        }
                                        else{
                                            break;
                                        }
                                    }
                                    fsadapter = new firestore_adapter(fsdatalist);
                                    fsrecycler.setAdapter(fsadapter);
//                                    fsadapter.notifyDataSetChanged();
                                    taskcur.setText("");
                                    timecur.setText("");
                                    tagcur.setSelection(0);
                                }
                            }
                            else
                            {
                                Log.i("TAG", "Failed with: ", task.getException());
                            }
                        }
                    });
                }
            }
        });
    }
}
