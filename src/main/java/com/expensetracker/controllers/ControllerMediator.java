package com.expensetracker.controllers;

import com.expensetracker.Expense;

public class ControllerMediator implements ControllerInterface {
    
    private FilterController filterController;
    private AddExpenseController addExpenseController;
    private TableController tableController;
    
    // Register Controllers
    public void registerFilterController(FilterController filterController)
    {
        this.filterController = filterController;
    }
    public void registerAddExpenseController(AddExpenseController addExpenseController)
    {
        this.addExpenseController = addExpenseController;
    }
    public void registerTableController(TableController tableController)
    {
        this.tableController = tableController;
    }

    // Table Controller Functions
    public void filterTable()
    {
        tableController.filterTable();
    }
    public void addTableExpense(Expense e) {
        tableController.addTableExpense(e);
    }

    /*
     * Everything below here is in support of Singleton pattern
     */
    private ControllerMediator() {}

    public static ControllerMediator getInstance() {
        return ControllerMediatorHolder.INSTANCE;
    }

    private static class ControllerMediatorHolder {
        private static final ControllerMediator INSTANCE = new ControllerMediator();
    }
}