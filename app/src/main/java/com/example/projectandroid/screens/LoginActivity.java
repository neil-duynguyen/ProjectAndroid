package com.example.projectandroid.screens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.bumptech.glide.Glide;
import com.example.projectandroid.R;
import com.example.projectandroid.model.User;
import com.example.projectandroid.providerScreens.HomeProviderActivity;
import com.example.projectandroid.providerScreens.PickerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private TextView createAccount, tvErr;

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
        tvErr = findViewById(R.id.sign_in_error);
        tvErr.setVisibility(View.GONE);

        email.setText("test1@gmail.com");
        //password.setText(000000);

        mAuth = FirebaseAuth.getInstance();

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                startActivity(new Intent(LoginActivity.this, PickerActivity.class));
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
                    redirectByRole();
                } else {
                    Exception exception = task.getException();
                    if (exception != null) {
                        String errorMessage = exception.getMessage();
                        tvErr.setVisibility(View.VISIBLE);
                        tvErr.setText(errorMessage);
                        //Toast.makeText(LoginActivity.this, "Fail: " + errorMessage, Toast.LENGTH_SHORT).show();
                    } else {
                        tvErr.setVisibility(View.VISIBLE);
                        tvErr.setText("Unknown error occurred.");
                        //Toast.makeText(LoginActivity.this, "Fail: Unknown error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                tvErr.setVisibility(View.VISIBLE);
                tvErr.setText(e.getMessage());
                //Toast.makeText(LoginActivity.this, "Fail...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void redirectByRole() {

        FirebaseUser temp = FirebaseAuth.getInstance().getCurrentUser();

        if(temp!=null){
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Lấy dữ liệu từ danh sách và xử lý
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User object = snapshot.getValue(User.class);
                        // Xử lý object ở đây
                            if(snapshot.getKey().equals(temp.getUid())){
                            if(object.getRole().equals("renter")){
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finishAffinity();
                            }else if(object.getRole().equals("provider")){
                                startActivity(new Intent(LoginActivity.this, HomeProviderActivity.class));
                                finishAffinity();
                            }
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý lỗi nếu có
                }
            });
        }
    }

}