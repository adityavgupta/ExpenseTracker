package com.expensetracker.controllers;

import com.expensetracker.Expense;

public interface ControllerInterface {
    public void registerFilterController(FilterController filterController);
    public void registerAddExpenseController(AddExpenseController addExpenseController);
    public void registerTableController(TableController tableController);
    public void registerSummaryController(SummaryController summaryController);
    public void registerLineGraphController(LineGraphController lineGraphController);

    public void addTableExpense(Expense e);
    public void filterTable();
    public void calculateAll();
    public void updateData();
}