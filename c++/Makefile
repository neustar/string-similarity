OBJS = compStrMetricMain.o compStrMetric.o
CC = g++
DEBUG = -g
CFLAGS = -Wall -c $(DEBUG)
LFLAGS = -Wall $(DEBUG)

computeMetrics : $(OBJS)
	$(CC) $(LFLAGS) $(OBJS) -o computeMetrics
compStrMetricMain.o : compStrMetric.h compStrMetricMain.cc
	$(CC) $(CFLAGS) compStrMetricMain.cc
compStrMetric.o : compStrMetric.h compStrMetric.cc 
	$(CC) $(CFLAGS) compStrMetric.cc
clean:
	rm *.o computeMetrics
