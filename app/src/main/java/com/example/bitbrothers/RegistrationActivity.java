package com.example.bitbrothers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.bitbrothers.databinding.ActivityRegistrationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {
    ActivityRegistrationBinding binding;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String userFullName,email;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressBar = new ProgressDialog(RegistrationActivity.this);
        progressBar.setMessage("Sign Up...");
        progressBar.setCancelable(false);
        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        binding.loginIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signUp() {
        userFullName = binding.userFullName.getText().toString();
        email  = binding.userEmailAddress.getText().toString();
        String phoneNumber = binding.userPhoneNumber.getText().toString();
        String password = binding.userPassword.getText().toString();
        String confirmPassword = binding.confirmPassword.getText().toString();
        if (TextUtils.isEmpty(userFullName)) {
            binding.userFullName.setError("Full Name is Required");
        }
        else if(TextUtils.isEmpty(phoneNumber)){
            binding.userPhoneNumber.setError("Phone Number is Required");
        }
        else if(TextUtils.isEmpty(email)){
            binding.userEmailAddress.setError("Email is required");
        }
        else if(!email.trim().matches(emailPattern)){
            binding.userEmailAddress.setError("Email is Not Valid");
        }
        else if(TextUtils.isEmpty(password)){
            binding.userPassword.setError("Password is required");
        }
        else if(TextUtils.isEmpty(confirmPassword)){
            binding.confirmPassword.setError("Confirm Password is Required ");
        }
        else if(password.length() < 6){
            binding.userPassword.setError("Password Must be equal to 6 character or greater than 6");
        }
        else if(!confirmPassword.equals(password)){
            binding.confirmPassword.setError("Password and Confirm Password must be same");
        }
        else{
            progressBar.show();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            }
                            else{
                                Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                }
            });
        }


    }
    private void updateUI(FirebaseUser user) {
        if(user != null){
            Intent intent = new Intent(RegistrationActivity.this,MainActivity.class);
            intent.putExtra("Name",userFullName);
            intent.putExtra("Email",email);
            startActivity(intent);

        }
        else{
            Toast.makeText(RegistrationActivity.this, "Try Again Some Time!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(RegistrationActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }

}