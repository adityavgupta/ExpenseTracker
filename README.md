# ExpenseTracker
Expense Tracker made by Aditya and Brandon

## Features to Implement
- Filters
This would be a tab group with the amount, date, credit etc. 
	- Date Filter
   		>- Case1: Both dates entered, get data within that range, check start is before end.
   		>- Case2: Only start date. Start till the last entry possible.
     	>- Case3: Only end date. Earliest till End date.
    	>- Nothing: All values
    
	- Amount Filter
 		> similar cases as date filter
     
	- String Matching in comments
  
	- Payment Method string matching
   
    - Credit Debit check boxes
      
    - Apply button
    	> can be triggered by enter when that tab has focus
    
    - Remove filter button
    	> keybind tbd

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
- Add Comments in code for ourselves

## Necessary Code Changes
- Make all of ExpenseMap static and adjust MainViewController for this change