package com.example.projectandroid.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projectandroid.R;
import com.example.projectandroid.adapters.HomeAdapter;
import com.example.projectandroid.listeners.ItemListener;
import com.example.projectandroid.model.Item;
import com.example.projectandroid.model.User;
import com.example.projectandroid.providerScreens.HomeProviderActivity;
import com.example.projectandroid.providerScreens.PaymentActivity;
import com.example.projectandroid.screens.DetailsActivity;
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

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment implements ItemListener {

    private RecyclerView topDealRV;
    private HomeAdapter adapter;
    private List<Item> itemList;
    TextView tvName, tvRole;

    ImageView btnLogout;
    de.hdodenhof.circleimageview.CircleImageView userProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        topDealRV = view.findViewById(R.id.top_deal_RV);
        tvName = view.findViewById(R.id.home_frag_user_name);
        tvRole = view.findViewById(R.id.home_frag_role);
        userProfile =view.findViewById(R.id.profile_image);
        btnLogout = view.findViewById(R.id.home_frag_logout);


        itemList = new ArrayList<>();
//        itemList.add(new Item("Street # 1, Pakistan", "$ 200", "1 bedroom"));
//        itemList.add(new Item("Street # 1, Pakistan", "$ 200", "1 bedroom"));
//        itemList.add(new Item("Street # 1, Pakistan", "$ 200", "1 bedroom"));
//        itemList.add(new Item("Street # 1, Pakistan", "$ 200", "1 bedroom"));

        FirebaseDatabase.getInstance().getReference().child("image")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            itemList.add(new Item(
                                    Objects.requireNonNull(dataSnapshot.child("address").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("location").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("price").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("description").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("shortDescription").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("id").getValue()).toString()
                                    ));
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        adapter = new HomeAdapter(getContext(), itemList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        topDealRV.setLayoutManager(linearLayoutManager);
        topDealRV.setAdapter(adapter);
        setInfor();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                //startActivity(new Intent(getContext(), PaymentActivity.class));
            }
        });
    }
    @Override
    public void OnItemPosition(int position) {
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("address", itemList.get(position).getAddress());
        intent.putExtra("price", itemList.get(position).getPrice());
        intent.putExtra("location", itemList.get(position).getLocation());
        intent.putExtra("description", itemList.get(position).getDescription());
        intent.putExtra("shortDescription", itemList.get(position).getShortDescription());
        intent.putExtra("image", itemList.get(position).getImage());
        intent.putExtra("id", itemList.get(position).getId());
        startActivity(intent);
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
                        tvName.setText("Hi "+user.getName());
                        tvRole.setText("Role: "+ user.getRole());
                        Glide
                                .with(HomeFragment.this)
                                .load(user.getImage())
                                .centerCrop()
                                .placeholder(R.drawable.ic_account)
                                .into(userProfile);
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


    public void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Thực hiện logout ở đây
                performLogout();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Đóng hộp thoại xác nhận
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void performLogout() {
        FirebaseAuth.getInstance().signOut();

        startActivity(new Intent(getContext(), LoginActivity.class));

    }

}