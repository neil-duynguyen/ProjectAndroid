package com.example.projectandroid.model;

import java.util.Date;

public class Transaction {
    public String TransactionId ;
    public long Amount ;
    public String Result;
    public int Status;
    public Date TransDate;
    public String TranToken;

    public long walletBefor ;
    public long walletAfter ;


    public Transaction(String transactionId, long amount, String result, int status, Date transDate, String tranToken, long walletBefor, long walletAfter) {
        TransactionId = transactionId;
        Amount = amount;
        Result = result;
        Status = status;
        TransDate = transDate;
        TranToken = tranToken;
        this.walletBefor = walletBefor;
        this.walletAfter = walletAfter;
    }
}
