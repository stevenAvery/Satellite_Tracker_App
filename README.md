# Overview
Select a satellite for more details, and to view its current location on a map

# Detail
## Activities
- Select Satellite Activity
	- mainActivity
	- List of available satellites are stored on a local database
	- Allows the user to select a satellite (by name or NORAD id)
- Satellite Map
	- Shows the location of the given satellite

## Steps
- Present user with Select Satellite activity
- When the user selects a satelite, look up the TLE for given satellite from https://www.space-track.org/#/tle
	- https://www.space-track.org/basicspacedata/query/class/tle_latest/ORDINAL/1/NORAD_CAT_ID/19822/orderby/TLE_LINE1%20ASC/format/tles
- Parse TLE string into TLE object
- Compute the the current location from TLE (http://www.stltracker.com/resources/equations)
- Intent to start Map activity at satellite location