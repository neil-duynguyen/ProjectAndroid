package com.example.projectandroid.providerScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.projectandroid.R;
import com.example.projectandroid.model.Transaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FailActivity extends AppCompatActivity {

    TextView tvBefor, tvAmount, tvAfter, tvid, tvtoken, tvdate, tvstatus, tvresult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fail);
        Intent intent = getIntent();
        String id = intent.getStringExtra("transId");
        tvBefor = findViewById(R.id.success_befor);
        tvAmount = findViewById(R.id.success_amount);
        tvAfter = findViewById(R.id.success_after);
        tvid = findViewById(R.id.success_transId);
        tvtoken = findViewById(R.id.success_transtoken);
        tvdate = findViewById(R.id.success_date);
        tvstatus = findViewById(R.id.success_status);
        tvresult = findViewById(R.id.success_mes);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference objectCRef = database.getReference("transactions");
        objectCRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot bSnapshot : dataSnapshot.getChildren()) {

                    if(bSnapshot.getKey().equals(id)){

                        Transaction trans = bSnapshot.getValue(Transaction.class);
                        if(String.valueOf(trans.walletBefor) ==null){
                            tvBefor.setText("0");
                        }else{
                            tvBefor.setText(String.valueOf(trans.walletBefor));
                        }
                        tvAmount.setText("+ " + String.valueOf(trans.Amount));
                        tvAfter.setText("= "+ String.valueOf(trans.walletAfter) );
                        tvid.setText("TransactionId: "+trans.TransactionId);
                        tvtoken.setText("TransactionToken: "+trans.TranToken);
                        tvdate.setText("Date: "+trans.TransDate);
                        if(trans.Status == 200){
                            tvstatus.setText("Status: "+"Success");
                        }else{
                            tvstatus.setText("Status: "+"Fail");

                        }
                        tvresult.setText("Message: "+ trans.Result);
                        return;
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