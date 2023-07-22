package com.example.projectandroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectandroid.R;
import com.example.projectandroid.model.Transaction;
import com.example.projectandroid.model.User;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context context;
    private List<User> traineeList;

    public UserAdapter(Context context) {
        this.context = context;
    }

    public void  setData(List<User> list){
        this.traineeList = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent,false);
        return new UserAdapter.UserViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if(traineeList!=null){
            return traineeList.size();
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        User trainee = traineeList.get(position);
        if(trainee== null){
            return;
        }

        holder.tvId.setText("Name: "+trainee.getName());
        holder.tvToken.setText("Email: "+trainee.getEmail());
        holder.TvMess.setText("Phone: "+trainee.getPhone());
        holder.tvDate.setText("Role: " + trainee.getRole());

        holder.tvAmount.setText("Wallet: "+convertToFormattedStringLONG(trainee.getWallet()) +"VNĐ");
    }

    public String convertToFormattedStringLONG(long number) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat("#,###,###", symbols);
        String formattedString = decimalFormat.format(number);

        return formattedString;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        TextView tvId, tvToken, TvMess, TvStatus, tvDate, tvAmount;
        CardView cardView;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.item_trans_id);
            tvToken = itemView.findViewById(R.id.item_trans_token);
            TvMess = itemView.findViewById(R.id.item_trans_mes);
            TvStatus = itemView.findViewById(R.id.item_contract_btnStatus);
            tvDate = itemView.findViewById(R.id.item_trans_tvDate);
            tvAmount = itemView.findViewById(R.id.item_transAmount);
        }
    }
}

