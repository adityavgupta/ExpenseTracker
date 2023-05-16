package com.expensetracker;

import java.util.Date;

public class Expense {

    // Amount you spent
    private double amount;

    // Expense Type, Credit is negative, Debit is positive
    enum expenseType {
        CREDIT,
        DEBIT
    }

    private expenseType expType;

    // Date
    private Date date;

    // Payment Method
    private String paymentMethod;

    // Comments
    private String comment;


    // Constructors
    public Expense(double amount, expenseType creditOrDebit, Date date)
    {
        this(amount, creditOrDebit, date, "NA", "NA");

    }

    public Expense(double amount, expenseType creditOrDebit, Date date, int flag, String paymentMethodOrComment) throws Exception
    {
        this.amount = amount;
        this.date = date;
        this.expType = creditOrDebit;
        this.date = date;
        switch(flag) {
            case 0:
                this.paymentMethod = paymentMethodOrComment;
                this.comment = "NA";
                break;
            case 1:
                this.paymentMethod = "NA";
                this.comment = paymentMethodOrComment;
                break;
            default:
                System.out.println("here");
                throw new Exception("Expense constructor for payment/comment flag invalid");
        }
    }

    public Expense(double amount, expenseType creditOrDebit, Date date, String paymentMethod, String comment)
    {
        this.amount = amount;
        this.date = date;
        this.expType = creditOrDebit;
        this.date = date;
        this.paymentMethod = paymentMethod;
        this.comment = comment;

    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public expenseType getExpType() {
        return expType;
    }

    public void setExpType(expenseType expType) {
        this.expType = expType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

