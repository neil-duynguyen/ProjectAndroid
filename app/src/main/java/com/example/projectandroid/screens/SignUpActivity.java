package com.example.projectandroid.screens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.projectandroid.R;
import com.example.projectandroid.model.User;
import com.example.projectandroid.providerScreens.HomeProviderActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;


public class SignUpActivity extends AppCompatActivity {
    User user;
    private TextView haveAccount;
    private FirebaseAuth mAuth;
    private EditText userName, userEmail, userPassword, confirmPassword , tvPhone;
    private AppCompatButton signUpButton;
    private DatabaseReference mRef;
    ProgressDialog dialog;
    Spinner spinner;
    TextView err ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        haveAccount = findViewById(R.id.have_account);
        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        userPassword = findViewById(R.id.user_password);
        confirmPassword = findViewById(R.id.user_confirm_password);
        signUpButton = findViewById(R.id.sign_up_button);
        spinner = findViewById(R.id.user_role);
        tvPhone = findViewById(R.id.user_phone);

        err = findViewById(R.id.sign_up_error);
        err.setVisibility(View.GONE);
        String[] items = {"renter", "provider"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        mRef =  FirebaseDatabase.getInstance().getReference();

        dialog = new ProgressDialog(this);
        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userName.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
                } else if (userEmail.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                } else if (userPassword.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                } else if (!userPassword.getText().toString().trim().equals(confirmPassword.getText().toString().trim())) {
                    Toast.makeText(SignUpActivity.this, "Enter valid password", Toast.LENGTH_SHORT).show();
                }
                else {

                    if (emailChecker(userEmail.getText().toString().trim())) {
                        boolean isValid = isValidPhoneNumber(tvPhone.getText().toString());
                        if (isValid) {
                            createUser(userEmail.getText().toString().trim(),
                                    userPassword.getText().toString().trim(),
                                    userName.getText().toString().trim(), tvPhone.getText().toString().trim());
                        } else {
                            Toast.makeText(SignUpActivity.this, "Enter valid phone", Toast.LENGTH_SHORT).show();

                        }

                    } else {
                        Toast.makeText(SignUpActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });


    }

    boolean isValidPhoneNumber(String phoneNumber) {
        // Định dạng cho số điện thoại: chính xác 10 chữ số
        String phonePattern = "^[0-9]{10}$";
        return Pattern.matches(phonePattern, phoneNumber);
    }

    boolean emailChecker(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    void createUser(String email, String password, String name, String Phone) {
        dialog.setTitle("Sign Up");
        dialog.setMessage("");
        dialog.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String role = spinner.getSelectedItem().toString();
                String avartar = "https://firebasestorage.googleapis.com/v0/b/rentalhome-df2d1.appspot.com/o/60111.jpg?alt=media&token=23b1b40d-379c-49cf-b05c-10c2f3b34e96";
                user = new User(name, email, 0, avartar,role,Phone );

                if (task.isSuccessful()) {

                    //save data in Firebase database with automatic generated key
                    //push function for use => automatic key generation
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    saveUserToDatabase(currentUser.getUid());
                    dialog.dismiss();
                    if(role.equals("renter")){
                        startActivity(new Intent(SignUpActivity.this, HomeActivity.class));

                    }else if(role.equals("provider")){
                        startActivity(new Intent(SignUpActivity.this, HomeProviderActivity.class));

                    }
                    finishAffinity();

                    Toast.makeText(SignUpActivity.this, "User create successfully ...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpActivity.this, "Fail to create user ..", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                err.setVisibility(View.VISIBLE);
                err.setText(e.getMessage());
                dialog.dismiss();
                Toast.makeText(SignUpActivity.this, "Fail to create user", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void saveUserToDatabase(String uid) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = databaseRef.child("users");
        DatabaseReference userRef = usersRef.child(uid);

        userRef.child("name").setValue(user.getName());
        userRef.child("email").setValue(user.getEmail());
        userRef.child("image").setValue(user.getImage());
        userRef.child("wallet").setValue(user.getWallet());
        userRef.child("role").setValue(user.getRole());
        userRef.child("phone").setValue(user.getPhone());

    }
}