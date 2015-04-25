Program Usage
=============
In this repository (sub-fodler) there are three program source packages namely, `compStrMetricMain`, `compStrMetricHeader` and `compStrMetric`. 
Sample input and output files are also included in the `testInputData` and `testOutputData` folders. Once the program is complied using the make file provided and the instructions below, the sample output should look like what is in one of the corresponding sample output files. The same result is also displayed as a standard output (on the screen) when the program runs.


Dependency
==========
The makefile uses `javac`. Hence `javac` needs to be installed to use the current `Makefile` as it is.


Building and Running
=====================
To run the program in a Linux environment, do the following.

1. Download the archive file and unzip it.
2. In the unzipped folder, type `make` to generate the CompStrMetricMain.class class file in the bin folder.
3. Prepare an input file where the two input strings to be compared are separated with the "|" character.  
4. Run the program by typing  `java -cp bin CompStrMetricMain inputFile outputFile` where `inputFile` and `outputFile` are input and output files respectively. The program can also be run by launching the `runJavMain.sh` shell script as `sh runJavMain.sh`. The input and output files can be specified in this shell script.
5. Get the output from the standard output and from the output file in the output folder.
6. A `compStrMetricMain.jar` jar file can also be extracted for instance using Eclipse as `File->Export->Java->Ruunable JAR file`. The jar file can then be run from the local folder for example as `java -jar compStrMetricMain.jar testInputData/simpleTest.txt testOutputData/simpleTestOut.txt`. This command line can also be run by launching the `runJar.sh` shell script as `sh runJar.sh`. The input and output files can be changed in this shell script.


Testing
==========
Some sample test input files are given in the `testInputData` folder. The names dataset in this folder is extracted from [2]. The corresponding output files are given in `testOutputData` folder. After compiling the program successfully, running the `CompStrMetricMain` class file with the input files in the testInputData folder should generate the strings with the corresponding metric values in a format which is the same as those in the `testOutputData` floder.
The program can be run with the test files as `java -cp bin testInputData/testInputFile testOuputData/testOutputFile` where `testInputFile` can be any of the files in the `testInputData` folder. 
This can then generate the `testOputputFile` in the `testOutputData` folder.


Contributors
============
If you find a bug or would like to suggest improvements, please use the issue tracker available on GitHub.


License
=======
This tool is made available under the Apache License, Version 2.0 (see [1] for details).


[1] http://www.apache.org/licenses/LICENSE-2.0.html <br />
[2] http://www.ssa.gov/cgi-bin/popularnames.cgi

