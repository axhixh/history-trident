# History Trident

Sample code using Trident to generate heatmap from browsing history.

## TridentStreamHistory
Generates data file for heatmap based on all visitis. Very basic example.

## TridentDRPCHistory
Generate tab separated file with data for heatmap from visits. Uses DRPC.

## TridentStreamFiltered
Generates data file for heatmap. Filters to include only URL that has 
'clojure' in it. Example of merging two sources.



In each of the case, the generated file is used to create heatmap with the
help of gnuplot. The script for this is included as heatmap.gnu

## To Do
1. Fix the NPE. 
2. Need to change the code to do batch processing. 
3. Figure out the reason for IOException at of the application.


