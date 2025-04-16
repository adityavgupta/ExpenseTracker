# ExpenseTracker
Expense Tracker made by Aditya and Brandon

## QOL
- Customizable date format


## Bugs
- On startup, if there is no data.bin file, we are currently throwing the exception instead of creating the file or doing proper error handling.

## FAQ
- How do you clean cache in VSCode?
	- Make sure settings.json has the following setting set to false "java.debug.settings.onBuildFailureProceed": false
	- When build fails, click fix, then clear cache
		- or click search bar at the top of VS Code. Select run task then Java build

## Features in Order of Priority
- import/export csv
	- link save as to writeCsv in ExpenseMap
	- Update save as to do file selection, not directory
- Edit expenses from within the table
- Add investing to expense type
	- integrate with plot

## Maybe Features
- Summary tab
	- Calculator
		- time to accumulate certain amount (with or without intrest)
		- Want to hit amount by date, what do earnings need to look like (can use spending history)
		- interest rate on input amount
			- if you allocate part of savings, intrest rate projections
	- wishlist
		- Estimated time to achieve
		- time to accumulate certain amount
