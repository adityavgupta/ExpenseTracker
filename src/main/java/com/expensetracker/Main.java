package com.expensetracker;

import java.util.Date;

public class Main {
    public static void main(String[] args) throws Exception {
        int f = 0;
        Expense item = new Expense(123.00, Expense.expenseType.CREDIT, new Date(), f, "bitcoin");
        System.out.println(item.getPaymentMethod());
    }
}
