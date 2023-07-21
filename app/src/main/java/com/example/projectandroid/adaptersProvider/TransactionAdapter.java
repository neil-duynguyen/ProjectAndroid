package com.example.projectandroid.adaptersProvider;

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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private Context context;
    private List<Transaction> traineeList;

    public TransactionAdapter(Context context) {
        this.context = context;
    }

    public void  setData(List<Transaction> list){
        this.traineeList = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent,false);
        return new TransactionViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if(traineeList!=null){
            return traineeList.size();
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction trainee = traineeList.get(position);
        if(trainee== null){
            return;
        }

        holder.tvId.setText("TransId: "+trainee.TransactionId);
        holder.tvToken.setText("TransToken: "+trainee.TranToken);
        holder.TvMess.setText("Result: "+trainee.Result);
        holder.tvDate.setText(trainee.TransDate);
        if(trainee.Status == 200){
            holder.TvStatus.setText("Success");
            holder.TvStatus.setTextColor(ContextCompat.getColor(context,R.color.green));
        }else{
            holder.TvStatus.setText("Fail");
            holder.TvStatus.setTextColor(ContextCompat.getColor(context,R.color.red));
        }
        holder.tvAmount.setText("Amount: "+convertToFormattedStringLONG(trainee.Amount) +"VNĐ");
    }

    public String convertToFormattedStringLONG(long number) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat("#,###,###", symbols);
        String formattedString = decimalFormat.format(number);

        return formattedString;
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder{
        TextView tvId, tvToken, TvMess, TvStatus, tvDate, tvAmount;
        CardView cardView;
        public TransactionViewHolder(@NonNull View itemView) {
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
