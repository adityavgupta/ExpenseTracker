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

## Bugs
- Can't use esc to remove focus from input boxes with filters

## FAQ
- How do you clean cache in VSCode?
	- Make sure settings.json has the following setting set to false "java.debug.settings.onBuildFailureProceed": false
	- When build fails, click fix, then clear cache
		- or click search bar at the top of VS Code. Select run task then Java build

## Next Time
- Plot on hover display values on axis (ideally use closest datapoint and indicate this datapoint)
- Make plot look better
- More summary calculations
- Deleting from table does not update plot
- Right now have to click the filter and refresh twice to get the correct rendering on the date axis. Will Potentiallly
need to fix the sort and stuff to resolve this.
- Look into localdate from datepicker and timezone issue for expense dates

## Maybe feature
- Add Category to expenses
- Filter with no matches does nothing. Change this?
