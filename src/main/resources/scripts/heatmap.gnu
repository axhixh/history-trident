# gnuplot script for plotting browsing history heatmaps
# gnuplot -e "filename='heatmap.txt'" heatmap.gnu

reset

set title "Browsing History (number of sites visited)"
set term png size 800, 600
#set output "heatmap_log.png"
set output filename.".png" 
set size ratio 0.5
set xlabel "Hour of Day"
set xrange[0:24]
set xtics 0,1,24
set ylabel "Day of Week"
set yrange[0:7]
set ytics ("Sun" 0, "Mon" 1, "Tue" 2, "Wed" 3, "Thu" 4, "Fri" 5, "Sat" 6)

set logscale zcb

set palette rgbformulae 22,13,10

#plot "heatmap.txt" matrix with image
plot filename matrix with image

