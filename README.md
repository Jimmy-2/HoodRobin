# HoodRobin - StockAlertsPaperTradingApp

## Table of Contents (to be added)
1. [Overview](#Overview)
2. [Product Spec](#Product-Spec)
3. [Gifs](#Gifs)

## Overview

Group members:


[Ariq Zaman](https://github.com/ariqzaman) - Graphs and Charts

[Brian Nan](https://github.com/DogEnjoyer) - Papertrading

[Isaac Anzures](https://github.com/ianzures) - News 

[Jimmy Wu](https://github.com/Jimmy-2) - Alerts/Notifications
### Description
Stock alerts and papertrading app for Hunter College CSCI Capstone course. Features news and graphs for stocks.



## Product Spec

### 1. Screen Archetypes

* Home/1st tab: Alerts
    * Users can search for a stock and add an price movement alert for that stock to the alerts list. The app will check every minute(1 min due to api limits) and if a stock reaches the price alert, the user will be notified through a push notification. The completed alert will be automatically put into the complete alerts list with the time the alert was completed. Users can delete alerts from the alerts and completed alerts list as well as sort the lists by date added or by stock name. Clicking on an alert will lead the user to a time series candlestick chart for that alert's stock. Alerts and sort settings are persisted through SQLite database.
* 2nd tab: Papertrading
    * Users can set a balance and start buying and selling stocks in our Paper trading game. The paper trading game includes achievements which are make 2 times your initial amount of money, have five times the intial amount of your money, have a million dollars in your balance, end up with 75% of your original balance and so on. The daily balance chart takes your current balance when you start the app for the first time in a day. The stock sector pie chart refreshes and displays the percentage of your portfolio that is of a certain sector. So if you were to have only technology stocks, your pie chart would be 100% techology. If you were to have 50% technology and 50% farming, your pie chart would reflect that.
* 3rd tab: News
    * 
* Graph Screen
    * 

### 2. Navigation

**Bottom Navigation Bar** (3 Tabs - Users can navigate through the tabs by clicking on the navigation bar items) 
* Alerts Tab 
* Paper Trading Tab - Navigate to the timeseries graph screen by clicking on an alert.
* News Tab


**Flow Navigation** (Screen to Screen)

* Home/1st tab: - Navigate to the Add Alerts Screen by searching for a viable stock ticker. If the ticker does not exist, no navigation will occur. After successfully adding an alert, the user will be taken back to the Alerts home screen. The user can also hap the back button if they do not wish to add an alert. Users can also navigate to the timeseries graph screen by clicking on an alert.
