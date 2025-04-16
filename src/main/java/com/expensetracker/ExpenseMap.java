package com.expensetracker;

import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;

import com.opencsv.CSVWriter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Date;

public class ExpenseMap {
    
    //Fields
    public static TreeMap<Long, Expense> expenseMap;
    public static Map<Long, Expense> filteredMap = new HashMap<>();
    private static final String filePath = System.getProperty("user.dir") + File.separator + "data.bin";

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

            // TreeMap<Long, Expense> tempMap = new TreeMap<>();
            // for(Map.Entry<Long, Expense> entry : expenseMap.entrySet())
            // {
            //     tempMap.put(entry.getKey()+1,entry.getValue());
            // }

            myObjectOutStream.writeObject(expenseMap);
            myObjectOutStream.close();
            myFileOutStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Add Expense
    public static void addExpense(Expense expense)
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
            // long UID = ExpenseMap.generateUID(expense.getDate());
            // expense.setUID(UID);
            filteredMap.put(expense.getUID(),expense);
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
    public static void removeExpense(Long UID)
    {
        expenseMap.remove(UID);
    }

    //Request Range of Dates
    public static Map<Long,Expense> getDateRange(Date start, Date end) //Need error if no expenseMap
    {
        TreeMap<Long,Expense> temp = new TreeMap<Long,Expense>(expenseMap);
        return temp.subMap(start.getTime(),end.getTime());
    }

    public static void writeCSV(String filePath) 
    { 
        // first create file object for file placed at location 
        // specified by filepath 
        File file = new File(filePath); 
        try { 
            // create FileWriter object with file as parameter 
            FileWriter outputfile = new FileWriter(file); 
    
            // create CSVWriter object filewriter object as parameter 
            CSVWriter writer = new CSVWriter(outputfile); 
    
            // adding header to csv 
            String[] header = { "date", "amount", "paymentMethod", "comment" }; 
            writer.writeNext(header); 
    

            if(!(ExpenseMap.expenseMap == null || ExpenseMap.expenseMap.isEmpty())){
                for(Map.Entry<Long,Expense> entry : ExpenseMap.expenseMap.entrySet()) {
                    Expense e = entry.getValue();

                    String amount = Double.toString(e.getAmount() * (e.getExpType() == Expense.expenseType.Debit ? 1 : -1));
                    String date = e.getDate().toString();
                    String paymentMethod = e.getPaymentMethod();
                    String comment = e.getComment();

                    String[] data = {date, amount, paymentMethod, comment};
                    writer.writeNext(data);
                }
            }
    
            // closing writer connection 
            writer.close(); 
        } 
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
    } 

}
