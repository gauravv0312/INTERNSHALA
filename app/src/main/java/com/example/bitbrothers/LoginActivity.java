package com.example.bitbrothers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.bitbrothers.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ProgressDialog progressBar;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        progressBar = new ProgressDialog(LoginActivity.this);
        progressBar.setMessage("Login...");
        progressBar.setCancelable(false);
        binding.signUpIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.userLoginEmailAddress.getText().toString().trim();
                String password = binding.userLoginPassword.getText().toString().trim();
                logIn(email,password);
            }
        });

    }

    private void logIn(String email,String password){
        if(TextUtils.isEmpty(email)){
            binding.userLoginEmailAddress.setError("Email is required");
        }
        else if(!email.trim().matches(emailPattern)){
            binding.userLoginEmailAddress.setError("Email is Not Valid");
        }
        else if(TextUtils.isEmpty(password)){
            binding.userLoginPassword.setError("Password is required");
        }
        else {
            progressBar.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUi(user);
                                progressBar.dismiss();
                            } else {
                                Toast.makeText(LoginActivity.this, "May Be Your Email or Password Wrong!", Toast.LENGTH_SHORT).show();
                                updateUi(null);
                                progressBar.dismiss();
                            }
                        }
                    });
        }
    }

    private void updateUi(FirebaseUser user) {
        if(user != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(LoginActivity.this, "Try Again!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}