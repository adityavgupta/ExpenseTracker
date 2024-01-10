# ExpenseTracker
Expense Tracker made by Aditya and Brandon

## Features to Implement
- Direction for Summary view
    - part of the plot view and see the summary for the selected filter with the plot itself.
- Direction for Plot View
    - Plot the net for each day within the filtered range

## QOL
- Implement converter for date input field
- Edit expenses from within the table
- Deselect amount and date box with escape
- Customizable date format
- Applying a filter that doesn't match anything should at least give a pop up.

## Bugs
- Can't use esc to remove focus from input boxes with filters

## FAQ
- How do you clean cache in VSCode?
	- Make sure settings.json has the following setting set to false "java.debug.settings.onBuildFailureProceed": false
	- When build fails, click fix, then clear cache
		- or click search bar at the top of VS Code. Select run task then Java build

## Next Time
- Clear/Apply filters toggle button
- Restructure scenebuilder stuff to one tab pane.
- Tab1 add expense and table
- Tab2 plots and summary
- On both we want filters. Either part of each tab or existing as its own anchor pane. TBD

## Maybe feature
- Add Category to expenses
