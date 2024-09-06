# ExpenseTracker
Expense Tracker made by Aditya and Brandon

## Features to Implement
- Direction for Summary view
    - part of the plot view and see the summary for the selected filter with the plot itself.
- Direction for Plot View
    - Plot the net for each day within the filtered range
- Summary tab
	- avg savings
		- day avg
		- week avg
		- month avg
		- year avg
	- net earning
	- net spent
	- net savings
	- wishlist
		- Estimated time to achieve
		- time to accumulate certain amount
	- Calculator
		- time to accumulate certain amount (with or without intrest)
		- Want to hit amount by date, what do earnings need to look like (can use spending history)
		- interest rate on input amount
			- if you allocate part of savings, intrest rate projections
	- naive calculations with only slope
	- look at 551 notes for better curve fitting solutions

## QOL
- Implement converter for date input field
- Edit expenses from within the table
- Deselect amount and date box with escape
- Customizable date format


## Bugs
- Table coloring on startup


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
