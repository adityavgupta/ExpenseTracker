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
- Filter tab invalid inputs highlight red
- Change the CSS for the min and max boxes to grayed out on changing tab to Yahoo View


## Bugs
- Can't use esc to remove focus from input boxes with filters
- In retro view add a filter to payment or comment, press filter. Press the refresh button and then go to yahoo view.
  The graph still shows the filtered data. Need to clear the filter after refresh.\
- If there is no match in the yahoo view, then it should not get rid of the points. It should just not do anything.

## FAQ
- How do you clean cache in VSCode?
	- Make sure settings.json has the following setting set to false "java.debug.settings.onBuildFailureProceed": false
	- When build fails, click fix, then clear cache
		- or click search bar at the top of VS Code. Select run task then Java build

## Next Time
- More summary calculations
- Add summary to the right of the plot that shows current savings etc etc.

## Maybe feature
- Add Category to expenses
- Filter with no matches does nothing. Change this?
