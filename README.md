# LocationTrackerApp
The LocationTracker is an android app that tracks the user's device, using google maps.

After the GPSService outputs a new location, a content provider inserts a new entry in a local andorid database, with it's longitude, latitude and timestamp provided from the GPSService. On each new entry the new location will be annouced to the user through a toast. 


### Information
- In order for the application to run, the user must give fine location access to the application and maintain a WiFi connection.
- The application updates the user's location every 5 seconds or every 10 meters from his last tracked position.

Source code is located at: _\src\main\java\com\example\locationapp_
