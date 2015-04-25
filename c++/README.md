Program Usage
=============
In this repository (sub-folder) there are three program source files namely, `compStrMetricMain.cc`, `compStrMetric.h` and `compStrMetric.cc`. 
Sample input and output files are also included in the `testInputData` and `testOutputData` folders. Once the program is complied using the make file provided and the instructions below, the sample output should look like what is in one of the corresponding sample output files. The same result is also displayed as a standard output (on the screen) when the program runs.


Dependency
==========
The makefile uses `g++`. Hence `g++` needs to be installed to use the current `Makefile` as it is.


Building and Running
=====================
To run the program in a Linux environment, do the following.

1. Download the archive file and unzip it.
2. In the unzipped folder, type `make` to generate the binary file.
3. Prepare an input file where the two input strings to be compared are separated with the "|" character.  
4. Run the program by typing  `./computeMetrics inputFile outputFile` where `inputFile` and `outputFile` are input and output files respectively.
5. Get the output from the standard output and from the output file in the output folder.
6. If you copy the `computeMetrics` binary into the `/bin` folder in a Macbook Pro for instance, it serves just as a Linux command performing what the `computeMetrics` tool is designed to do, those that are described above. It becomes similar to the well known Linux commands such as `ls`, `sync`, `chmod` ... etc. In this case, the tool is run as `computeMetrics inputFile outputFile` from any folder in the Macbook Pro.

Testing
==========
Some sample test input files are given in the `testInputData` folder. The names dataset in this folder is extracted from [2]. The corresponding output files are given in `testOutputData` folder. After compiling the program successfully, running the `computeMetrics` binary with the input files in the testInputData folder should generate the strings with the corresponding metric values in a format which is the same as those in the `testOutputData` floder.
The program can be run with the test files as `./computeMetrics testInputData/testInputFile testOuputData/testOutputFile` where `testInputFile` can be any of the files in the `testInputData` folder. 
This can then generate the `testOputputFile` in the `testOutputData` folder.


Contributors
============
If you find a bug or would like to suggest improvements, please use the issue
tracker available on GitHub.


License
=======
This tool is made available under the Apache License, Version 2.0 (see [1] for details).


[1] http://www.apache.org/licenses/LICENSE-2.0.html <br />
[2] http://www.ssa.gov/cgi-bin/popularnames.cgi

