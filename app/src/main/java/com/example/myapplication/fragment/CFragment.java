package com.example.myapplication.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.datamodel;
import com.example.myapplication.myadapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class CFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    DatabaseReference db;
    FirebaseAuth auth;
    FirebaseUser user;
    com.example.myapplication.myadapter myadapter;
    ArrayList<datamodel> dataholder;

    public CFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_c, container, false);
        //-----------------------Recyclerview fetching data---------------------------------------------------------------------------------------------------------------------------------------
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String userId = user.getUid();
        db = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Tasks");
        recyclerView = view.findViewById(R.id.recViewC);

        dataholder = new ArrayList<>();
        myadapter = new myadapter(dataholder,this);
        recyclerView.setAdapter(myadapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        // Retrieve tasks due today
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
        Date today = new Date();
//        Query query = db.orderByChild("dueDate");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataholder.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren() ){
                    datamodel datamodel = dataSnapshot.getValue(com.example.myapplication.datamodel.class);

                    boolean status = datamodel.isCompleted();
                    if (status == false) {
                        //Parse the due date string to a Date object for comparison
                        try {
                            Date dueDate = sdf.parse(datamodel.getDueDate());

                            // Compare the due date with the current date
                            if (dueDate != null && dueDate.compareTo(today) > 0) {
                                // Due date is equal to or earlier than the current date
                                // Add the task to the dataholder list
                                dataholder.add(datamodel);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                }
                Log.d("DataHolder CFragment", "Size: " + dataholder.size());
                myadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }


}