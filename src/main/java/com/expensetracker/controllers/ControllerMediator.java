package com.expensetracker.controllers;

public class ControllerMediator implements ControllerInterface {
    
    private MainViewController mainViewController;
    private FilterController filterController;
    
    public void registerMainViewController(MainViewController mainViewController)
    {
        this.mainViewController = mainViewController;
    }
    public void registerFilterController(FilterController filterController)
    {
        this.filterController = filterController;
    }

    public void filterTable()
    {
        mainViewController.filterTable();
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