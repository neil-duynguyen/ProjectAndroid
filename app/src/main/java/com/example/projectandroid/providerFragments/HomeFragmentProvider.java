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

import com.bumptech.glide.Glide;
import com.example.projectandroid.R;
import com.example.projectandroid.adaptersProvider.HomeAdapterProvider;
import com.example.projectandroid.fragments.HomeFragment;
import com.example.projectandroid.listeners.ItemListener;
import com.example.projectandroid.model.Item;
import com.example.projectandroid.model.Post;
import com.example.projectandroid.model.Transaction;
import com.example.projectandroid.model.User;
import com.example.projectandroid.providerScreens.CreateRoomProvider;
import com.example.projectandroid.providerScreens.DetailsProviderActivity;
import com.example.projectandroid.providerScreens.PaymentActivity;
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

public class HomeFragmentProvider extends Fragment implements ItemListener {
    private RecyclerView listRoom;
    long price;
    private HomeAdapterProvider adapter;
    private List<Item> itemList;
    private List<Item> listID;
    Button btnAddRoom;
    TextView tv_price_proviver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_provider, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listRoom = view.findViewById(R.id.list_room_provider);
        itemList = new ArrayList<>();
        listID = new ArrayList<>();

        //code lụi
        FirebaseDatabase.getInstance().getReference().child("image")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Item item = new Item(

                                    Objects.requireNonNull(dataSnapshot.child("location").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("price").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("description").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("shortDescription").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("id").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("userID").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("address").getValue()).toString()
                            );
                            listID.add(item);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        String temp = FirebaseAuth.getInstance().getCurrentUser().zzb().getUid(); //lấy user hiện tại
        FirebaseDatabase.getInstance().getReference().child("image")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        itemList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Post post = dataSnapshot.getValue(Post.class);


                            if (temp.equals(Objects.requireNonNull(dataSnapshot.child("userID").getValue()).toString())) {
                                Item item =new Item();
                                item.setId(post.id);
                                item.setUserID(post.userID);
                                item.setAddress(post.address);
                                item.setImage(post.image);
                                item.setLocation(post.location);
                                item.setShortDescription(post.shortDescription);
                                item.setDescription(post.description);
                                item.setPrice(String.valueOf(post.price));

                                itemList.add(item);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        adapter = new HomeAdapterProvider(getContext(), itemList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listRoom.setLayoutManager(linearLayoutManager);
        listRoom.setAdapter(adapter);

        //xử lý số tiền của provider
        //tách chuỗi
        tv_price_proviver = view.findViewById(R.id.price_provider);
        setInfor();

        //String currency = price.substring(price.length() - 3);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("pricePost");
        final Integer[] priceCost = new Integer[1];
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer price = (Integer) snapshot.getValue(Integer.class);
                priceCost[0] = price;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //kết thúc

        btnAddRoom = view.findViewById(R.id.btn_addRoom);
        btnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (price < priceCost[0]) {
                    startActivity(new Intent(getContext(), PaymentActivity.class));
                } else {
                    Intent intent = new Intent(getContext(), CreateRoomProvider.class);
                    intent.putExtra("id", listID.size()+"");
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void OnItemPosition(int position) {
        Intent intent = new Intent(getContext(), DetailsProviderActivity.class);
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
                        price = user.getWallet();
                        tv_price_proviver.setText(user.getWallet()+ " VNĐ");
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