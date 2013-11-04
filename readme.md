History Trident

Sample code using Trident to generate heatmap from browsing history.

# TridentStreamHistory
Generates data file for heatmap based on all visitis. Very basic example.

# TridentDRPCHistory
Generate tab separated file with data for heatmap from visits. Uses DRPC.

# TridentStreamFiltered
Generates data file for heatmap. Filters to include only URL that has 
'clojure' in it. Example of merging two sources.

# TridentStreamFiltered0
For some reason TridentStreamFiltered has NullPointerException sometimes.
This one works all the time.


In each of the case, the generated file is used to create heatmap with the
help of gnuplot. The script for this is included as heatmap.gnu

# To Do
Fix the NPE. 
Need to change the code to do batch processing. 
Figure out the reason for IOException at of the application.


