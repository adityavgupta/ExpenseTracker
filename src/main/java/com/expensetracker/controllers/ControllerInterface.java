package com.expensetracker.controllers;

public interface ControllerInterface {
    public void registerMainViewController(MainViewController mainViewController);
    public void registerFilterController(FilterController filterController);
    public void filterTable();

}