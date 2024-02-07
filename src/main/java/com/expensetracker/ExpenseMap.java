package com.expensetracker;

import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Date;

public class ExpenseMap {
    
    //Fields
    public static TreeMap<Long, Expense> expenseMap;
    public static Map<Long, Expense> filteredMap = new HashMap<>();
    private static final String filePath = System.getProperty("user.dir") + "\\data.bin";
    //Constructor
    public ExpenseMap()
    {  
    }

    //Load Binary
    public static void loadBinary()
    {
        try {
            FileInputStream fileInput = new FileInputStream(filePath);
            ObjectInputStream objectInput = new ObjectInputStream(fileInput);
            expenseMap = (TreeMap<Long, Expense>)objectInput.readObject();
            Date dateMin = new Date(Long.MIN_VALUE);
            Date dateMax = new Date(Long.MAX_VALUE);
            filteredMap = getDateRange(dateMin,dateMax);
            objectInput.close();
            fileInput.close();
        }
  
        catch (IOException e1) {
            e1.printStackTrace();
            return;
        }
  
        catch (ClassNotFoundException e2) {
            System.out.println("Class not found");
            e2.printStackTrace();
            return;
        }
    }

    //Save Binary
    public static void saveBinary()
    {
        try {
            FileOutputStream myFileOutStream = new FileOutputStream(filePath);
            ObjectOutputStream myObjectOutStream = new ObjectOutputStream(myFileOutStream);
            myObjectOutStream.writeObject(expenseMap);
            myObjectOutStream.close();
            myFileOutStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Add Expense
    public void addExpense(Expense expense)
    {
        if(expenseMap == null || expenseMap.isEmpty())
        {
            expenseMap = new TreeMap<Long, Expense>();
            long UID = expense.getDate().getTime();
            expense.setUID(UID);
            expenseMap.put(UID,expense);
        }
        else
        {
            long UID = ExpenseMap.generateUID(expense.getDate());
            expense.setUID(UID);
            expenseMap.put(UID,expense);
        }
        if(filteredMap == null || filteredMap.isEmpty())
        {
            filteredMap = new TreeMap<Long, Expense>();
            long UID = expense.getDate().getTime();
            expense.setUID(UID);
            filteredMap.put(UID,expense);
        }
        else
        {
            long UID = ExpenseMap.generateUID(expense.getDate());
            expense.setUID(UID);
            filteredMap.put(UID,expense);
        }
    }

    //Generate UID
    private static Long generateUID(Date d)
    {
        Long UID = d.getTime();
        while(expenseMap.containsKey(UID))
        {
            UID++;
        }
        return UID;
    }

    //Remove Expense
    public void removeExpense(Long UID)
    {
        expenseMap.remove(UID);
    }

    //Request Range of Dates
    public static Map<Long,Expense> getDateRange(Date start, Date end) //Need error if no expenseMap
    {
        TreeMap<Long,Expense> temp = new TreeMap<Long,Expense>(expenseMap);
        return temp.subMap(start.getTime(),end.getTime());
    }

}
