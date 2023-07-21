package com.example.projectandroid.providerFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.projectandroid.R;
import com.example.projectandroid.adaptersProvider.HomeAdapterProvider;
import com.example.projectandroid.adaptersProvider.TransactionAdapter;
import com.example.projectandroid.model.Item;
import com.example.projectandroid.model.Transaction;
import com.example.projectandroid.model.User;
import com.example.projectandroid.providerScreens.HomeProviderActivity;
import com.example.projectandroid.providerScreens.PaymentActivity;
import com.example.projectandroid.screens.HomeActivity;
import com.example.projectandroid.screens.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionFragmentProvider#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionFragmentProvider extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView transRV;
    TextView tv;
    Button btn;
    TransactionAdapter adapter;
    List<Transaction> list = new ArrayList<>();

    public TransactionFragmentProvider() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionFragmentProvider.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionFragmentProvider newInstance(String param1, String param2) {
        TransactionFragmentProvider fragment = new TransactionFragmentProvider();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction_provider, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        transRV = view.findViewById(R.id.trans_rv);
        adapter = new TransactionAdapter(getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        transRV.setLayoutManager(linearLayoutManager);
        tv= view.findViewById(R.id.trans_wallet);
        btn = view.findViewById(R.id.trans_add);

        loadList();
        redirectByRole();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PaymentActivity.class));
            }
        });
    }
    private void loadList() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference objectCRef = database.getReference("transactions");
        objectCRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot bSnapshot : dataSnapshot.getChildren()) {
                    Transaction tempTransaction = bSnapshot.getValue(Transaction.class);
                    list.add(tempTransaction);
                }
                adapter.setData(list);
                transRV.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
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
                        if(snapshot.getKey().equals(temp.getUid())){
                            tv.setText("Wallet: "+ object.getWallet() + " VNĐ");
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