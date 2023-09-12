package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Activity.MainActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddTask extends AppCompatActivity {

    EditText TaskName, TaskDesc;
    TextView selectDate, DatePick;
    int year,month,day;
    Button imgBtn, doneBtn;

    CircleImageView circleImageView;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseAuth  auth;
    FirebaseUser user;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);

        //---------------------Toolbar Navigation-------------------------------------------------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolbarTask);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //------------SETTING DATE PICKER-------------------------------------------------------------------------------------------------
        selectDate = findViewById(R.id.DatePick);
        Calendar calendar = Calendar.getInstance();
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTask.this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String dateString = String.format(Locale.US, "%02d.%02d.%04d", dayOfMonth, month + 1, year);
                        selectDate.setText(dateString);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        //---------------------------Storing data to firebase on btn click--------------------------------------------------------------------
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        doneBtn = findViewById(R.id.doneBtn);
        TaskName = findViewById(R.id.TaskName);
        TaskDesc = findViewById(R.id.TaskDesc);
        DatePick = findViewById(R.id.DatePick);
        String userId = user.getUid();

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // Get the values from the UI elements
                    String taskName = TaskName.getText().toString().trim();
                    String taskDesc = TaskDesc.getText().toString().trim();
                    String dueDate = DatePick.getText().toString().trim();
                    boolean completed = false; //set initial status to uncompleted

                    // Check if any of the fields is empty
                    if (taskName.isEmpty() || taskDesc.isEmpty() || dueDate.isEmpty()) {
                        // Show a toast message indicating that all fields must be filled
                        Toast.makeText(getApplicationContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                    } else {

                        rootNode = FirebaseDatabase.getInstance();
                        reference = rootNode.getReference("Users");

                        // Create a new task object
                        AddTaskHelperClass helperClass = new AddTaskHelperClass(taskName, taskDesc, dueDate, completed);
                        // Generate a new unique key for the task node in the Firebase Realtime Database
                        String taskId = reference.child(userId).child("Tasks").push().getKey();
                        // Store the new task in the Firebase Realtime Database
                        reference.child(userId).child("Tasks").child(taskId).setValue(helperClass);

                        // Start the MainActivity
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
            }
        });


        //------------------------IMAGE PICKER---------------------------------------------------------------------------------------------------------------
       circleImageView = findViewById(R.id.taskImg);
       imgBtn = findViewById(R.id.imgBtn);

       imgBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ImagePicker.with(AddTask.this)
                       .crop()	  //Crop image(Optional), Check Customization for more option
                       .start();

           }
       });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        circleImageView.setImageURI(uri);
    }


}