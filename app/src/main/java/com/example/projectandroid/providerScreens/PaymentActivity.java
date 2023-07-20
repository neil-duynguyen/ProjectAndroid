package com.example.projectandroid.providerScreens;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projectandroid.R;
import com.example.projectandroid.fragments.HomeFragment;
import com.example.projectandroid.model.Transaction;
import com.example.projectandroid.model.User;
import com.example.projectandroid.zaloService.CreateOrder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class PaymentActivity extends AppCompatActivity {
    EditText etAmount;
    androidx.appcompat.widget.AppCompatButton btnCheck;
    String tempCode = "";
    long amount = 0;
    User tempUser ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Toolbar toolbar = findViewById(R.id.payment_toolbar);
        setSupportActionBar(toolbar);
        setInfor();
        btnCheck = findViewById(R.id.payment_checkout);
        etAmount = findViewById(R.id.payment_amount);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        // Hiển thị nút quay lại (back button) trên Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(etAmount.getText().toString().equals("")){
                    etAmount.setError("Require");
                    return;
                }
                try {
                    amount = Long.parseLong(etAmount.getText().toString());
                }catch (Exception ex){
                    etAmount.setError("Number only");
                    return;
                }
                CreateOrder orderApi = new CreateOrder();

                try {
                    JSONObject data = orderApi.createOrder(etAmount.getText().toString());
                    String code = data.getString("return_code");
                    String token =data.getString("zp_trans_token");
                    if (code.equals("1")) {
                        ZaloPaySDK.getInstance().payOrder(PaymentActivity.this, token, "demozpdk://app", new PayOrderListener() {
                            @Override
                            public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        DatabaseReference transactionsRef = FirebaseDatabase.getInstance().getReference().child("transactions");
                                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                        DatabaseReference abcRef = transactionsRef.child(currentUser.getUid());
                                        Date date= new Date();

                                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                        String formattedDate = sdf.format(date);

                                        Transaction transaction = new Transaction(transactionId,amount,"Payment Success", 200,formattedDate, transToken,tempUser.getWallet(), tempUser.getWallet()+ amount );

                                        abcRef.child(transactionId).setValue(transaction);
                                        Intent intent = new Intent(PaymentActivity.this, SuccessActivity.class);
                                        intent.putExtra("transId", transactionId);
                                        startActivity(intent);
                                    }

                                });
                            }

                            @Override
                            public void onPaymentCanceled(String zpTransToken, String appTransID) {
                                DatabaseReference transactionsRef = FirebaseDatabase.getInstance().getReference().child("transactions");
                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference abcRef = transactionsRef.child(currentUser.getUid());
                                Date date= new Date();

                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                String formattedDate = sdf.format(date);
                                Transaction transaction = new Transaction(appTransID,amount,"User Cancel Payment", 400,formattedDate, zpTransToken,tempUser.getWallet(), tempUser.getWallet() );

                                abcRef.child(appTransID).setValue(transaction);
                                Intent intent = new Intent(PaymentActivity.this, FailActivity.class);
                                intent.putExtra("transId", appTransID);
                                startActivity(intent);
                            }

                            @Override
                            public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                                DatabaseReference transactionsRef = FirebaseDatabase.getInstance().getReference().child("transactions");
                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference abcRef = transactionsRef.child(currentUser.getUid());
                                Date date= new Date();

                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                String formattedDate = sdf.format(date);
                                Transaction transaction = new Transaction(appTransID,amount,"Payment Fail", 400,formattedDate, zpTransToken,tempUser.getWallet(), tempUser.getWallet() );

                                abcRef.child(appTransID).setValue(transaction);
                                Intent intent = new Intent(PaymentActivity.this, FailActivity.class);
                                intent.putExtra("transId", appTransID);
                                startActivity(intent);
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

    public void setInfor(){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Lấy dữ liệu từ danh sách và xử lý
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    // Xử lý object ở đây
                    if(snapshot.getKey().equals(currentUser.getUid())){
                        tempUser = user;
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