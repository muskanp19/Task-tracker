package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupActivity extends AppCompatActivity {
    AppCompatButton btnSignUp ;
    CircleImageView circleImageView;
    FloatingActionButton floatingActionButton;
    EditText email, Pass, confirmPass, Name, phone;
    FirebaseAuth mAuth;
    TextView loginText;


    @Override
    public void onStart() {
        super.onStart();
        // Check if user
        // is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        loginText = findViewById(R.id.loginText);
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iLogin = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(iLogin);
                finish();
            }
        });


        //--------------------SIGN UP BUTTON and Authentication --------------------------------------------------------------------------------

        email = findViewById(R.id.email);
        Pass = findViewById(R.id.Pass);
//        confirmPass = findViewById(R.id.confirmPass);
        Name = findViewById(R.id.Name);
        phone = findViewById(R.id.phone);
        mAuth = FirebaseAuth.getInstance();
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email, Password, ConfirmPass;
                Email= String.valueOf(email.getText());
                Password = String.valueOf(Pass.getText());

                if(TextUtils.isEmpty(Email)){
                    Toast.makeText(SignupActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(Password)){
                    Toast.makeText(SignupActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Account created ",
                                            Toast.LENGTH_SHORT).show();
                                    Intent iLogin = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(iLogin);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SignupActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        //------------------------IMAGE PICKING FROM CAMERA------------------------------------------------------------------------------
        //getting ids of views
        circleImageView = findViewById(R.id.profileImg);
        floatingActionButton = findViewById(R.id.fabImg);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(SignupActivity.this)
                        .crop()
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