package com.example.socialmediaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.socialmediaapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {


    ActivitySignUpBinding binding;

    FirebaseAuth auth;

    FirebaseDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();



        binding.btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = binding.NameEt.getText().toString();
                String professional = binding.ProfessionalEt.getText().toString();
                String email = binding.emialEt.getText().toString();
                String password = binding.passwordET.getText().toString();


                if(name.isEmpty()){
                    binding.NameEt.setError("Please Enter Your Name");
                    return;
                }else if(professional.isEmpty()){
                    binding.ProfessionalEt.setError("Please Enter Your Professional");
                    return;
                }else if(email.isEmpty()){
                    binding.emialEt.setError("Enter Your Email");
                    return;
                }else if(password.isEmpty()){
                    binding.passwordET.setError("Enter Password");
                    return;
                }else{

                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {



                            if(task.isSuccessful()){

                                Users user = new Users(name,professional,email,password);

                                String id = task.getResult().getUser().getUid();
                                
                                
                                database.getReference().child("Users").child(id).setValue(user);
                                Toast.makeText(SignUpActivity.this, "User data SAVED", Toast.LENGTH_SHORT).show();


                                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                                startActivity(intent);



                            }else{
                                Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });




                }
            }
        });


        binding.gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });



    }
}