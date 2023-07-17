package com.example.projectandroid.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectandroid.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextView createAccount;
    /*private Button login;*/
    private EditText email, password;
    private AppCompatButton loginButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        createAccount = findViewById(R.id.dont_have_account);
        email = findViewById(R.id.user_email);
        password = findViewById(R.id.user_password);
        loginButton = findViewById(R.id.login_button);

        mAuth = FirebaseAuth.getInstance();

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        /*login = findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                Toast.makeText(LoginActivity.this, "Login successfuly", Toast.LENGTH_SHORT).show();
            }
        });*/

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!email.getText().toString().trim().isEmpty() && emailChecker(email.getText().toString().trim())){
                    if (!password.getText().toString().trim().isEmpty()){
                        loginUser(email.getText().toString().trim(), password.getText().toString().trim());
                    } else {
                        Toast.makeText(LoginActivity.this, "Enter valid password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    boolean emailChecker(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    void loginUser(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    Toast.makeText(LoginActivity.this, "Succesfully login", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Fail..", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Fail...", Toast.LENGTH_SHORT).show();
            }
        });
    }

}