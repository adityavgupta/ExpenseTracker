# ExpenseTracker
Expense Tracker made by Aditya and Brandon

## Features to Implement
- Filters - DONE

- Summary
Implement this as a tab group with the table
    - Net expenditure (total credit) over the range of the data
    - Avg expenditure in the time frame if the time frame permits that calculation
        > - year
        > - month
        > - week
        > - day
    - Net earning
    - Total profit

- Plotting
Implement this as a tab group with the table.
	>details tbd

## QOL
- Implement converter for date input field
- Edit expenses from within the table
- Deselect amount and date box with escape
- Customizable date format
- In loadBinary() perhaps try not to load the data 2 times for expense and filter. More efficient method??

## Bugs
- Can't use esc to remove focus from input boxes with filters

## FAQ
- How do you clean cache in VSCode?
	- Make sure settings.json has the following setting set to false "java.debug.settings.onBuildFailureProceed": false
	- When build fails, click fix, then clear cache
		- or click search bar at the top of VS Code. Select run task then Java build

## Next Time
- Clear/Apply filters toggle button
- Summary view
- Right now, filtering is broken due to changes with summary controller and how filtered Map is being initialized.   
- Add check on filter to see if summary tab is selected and recalculate summary values
