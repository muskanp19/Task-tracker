package com.example.myapplication.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import  android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Activity.MainActivity;
import com.example.myapplication.AddTask;
import com.example.myapplication.R;
import com.example.myapplication.TaskDetail;
import com.example.myapplication.datamodel;
import com.example.myapplication.myadapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class BFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    DatabaseReference db;
    FirebaseAuth auth;
    FirebaseUser user;
    myadapter  myadapter;
    ArrayList<datamodel>  dataholder;
    AFragment aFragment;

    public BFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_b, container, false);

        //-----------------------Recyclerview fetching data---------------------------------------------------------------------------------------------------------------------------------------
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String userId = user.getUid();
        db = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Tasks");
        recyclerView = view.findViewById(R.id.recViewB);

        dataholder = new ArrayList<>();
        myadapter = new myadapter(dataholder,this);
        recyclerView.setAdapter(myadapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        // Retrieve tasks due today
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
        Date today = new Date();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataholder.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren() ){
                    datamodel datamodel = dataSnapshot.getValue(com.example.myapplication.datamodel.class);
                    // Set the task ID using the Firebase getKey() method
                    datamodel.setId(dataSnapshot.getKey());

                    boolean status = datamodel.isCompleted();
                    if (status == false) {
                        //Parse the due date string to a Date object for comparison
                        try {
                            Date dueDate = sdf.parse(datamodel.getDueDate());
                            // Compare the due date with the current date
                            if (dueDate != null && dueDate.compareTo(today) <= 0) {
                                // Due date is equal to or earlier than the current date
                                // Add the task to the dataholder list
                                dataholder.add(datamodel);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                }
                Log.d("DataHolder BFragment", "Size: " + dataholder.size());
                myadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //-----------------------------FAB Button---------------------------------------------------------------------------------------------------------------------------------
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iAddTask = new Intent(getActivity(), AddTask.class);
                startActivity(iAddTask);
            }
        });

        aFragment = (AFragment) getActivity().getSupportFragmentManager().findFragmentByTag("a_fragment_tag");

        return view;
    }

    public void showOptionsDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Mark as completed:")
                .setItems(new CharSequence[]{"Completed", "Cancel"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
//                                Intent intent = new Intent(getActivity(), TaskDetail.class);
//                                startActivity(intent);
                                // Mark the task as completed, this method in completed fragment
                                markTaskAsCompleted(position);
                                break;
                            case 1:
                                // Cancel the dialog
                                dialog.dismiss();
                                break;
                        }
                    }
                })
                .show();
    }


    private void markTaskAsCompleted(int position) {
        // Get the selected task from the data holder
        datamodel selectedTask = dataholder.get(position);

        // Update the status of the task to completed
        selectedTask.setCompleted(true);
        // Get the task ID from the selected task
        String taskId = selectedTask.getId();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String userId = user.getUid();
       // String taskId = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Tasks").push().getKey();
        FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Tasks").child(taskId).child("completed").setValue(true);

        // Remove the task from the current data holder
        dataholder.remove(position);
        myadapter.notifyItemRemoved(position);

        // Notify the adapter that the data set has changed
        myadapter.notifyDataSetChanged();
    }


}