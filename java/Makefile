#
# A makefile for compiling string similarity java classes
#

# Define a makefile variable for the java compiler
#
JCC = javac

# Define a makefile variable for compilation flags
# the -g flag compiles with debugging information
# the -d flag specifies destination directory for class files
#
JFLAGS = -g -d

# Remove command to clean the .class files
RM = rm

# Typing 'make' will invoke the first target entry in the makefile 
# (the default one in this case)
#
default: bin/compStrMetricHeader/CharInfo.class bin/compStrMetricHeader/StrMetricStruct.class \
bin/compStrMetricHeader/StrMetricVariables.class bin/compStrMetricHeader/SubStringStruct.class \
bin/compStrMetricHeader/SubSequenceStruct.class bin/compStrMetric/CompStrMetric.class bin/CompStrMetricMain.class

# this target entry builds the Average class
# the Average.class file is dependent on the Average.java file
# and the rule associated with this entry gives the command to create it
#
bin/compStrMetricHeader/%.class: src/compStrMetricHeader/%.java
	$(JCC) $(JFLAGS) bin/ -cp bin/ $<
bin/compStrMetric/%.class: src/compStrMetric/%.java
	$(JCC) $(JFLAGS) bin/ -cp bin/ $<
bin/%.class: src/%.java
	$(JCC) $(JFLAGS) bin/ -cp bin/ $<
# To start over from scratch, type 'make clean'.  
# Removes all .class files, so that the next make rebuilds them
#
clean: 
	$(RM) bin/*.class
	$(RM) bin/compStrMetric/*.class
	$(RM) bin/compStrMetricHeader/*.class
