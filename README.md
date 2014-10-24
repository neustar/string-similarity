Project Description
===================
The string similarity project designs and implements new string similarity metrics and efficient algorithms to obtain them. Given any two strings, the program (tool) in this open source returns various percentile metrics showing how similar the two strings are.
The current version of this project has implementations of the following six new and one existing (common) string similarity metrics. 

1. `Compute-Distance Metric`: Compute-distance metric is based on length of the longest matching subsequence group and length of longest of the two input strings.

2. `Weighted Compute-Distance Metric`:  Weighted compute-distance metric is based on the longest matching subsequence group and a weighted average of lengths of the longest and shortest strings.

3. `Weighted Similarity Metric (Subsequences based)`: This metric is based on lengths of all matching subsequence groups and lengths of the two strings.

4. `Weighted Similarity Metric (Substrings based)`: This metric is based on lengths of all matching substring groups and lengths of the two strings.

5. `Square Root Similarity Metric`: The square root similarity metric is based on sizes of all matching subsequence groups and weighted lengths of the two strings.

6. `Weighted Sum Similarity Metric`: Weighted sum similarity metric is based on length of longest subsequence group, sizes of all other subsequence groups and weighted average of lengths of the two strings.

7. The `String Match Ratio`: This is a commonly known metric, which obtains the ratio of matching characters of two strings to the weighted average of the lengths of the two strings.


Algorithms
==========
The algorithm to compute the above metrics first generates groups of parallel lines where each line is from a character in one string to the matching character in the second string. 

The algorithm then maintains the groups and their corresponding numbers of parallel lines (size) along with the group with highest number of lines.

Each of the the string metrics above is then obtained as a function of the sizes of the groups, and the length of the longer and shorter strings. 

For instance we set a metric to be directly proportional to the number of groups and the weighted average of the sizes of the groups and inversely proportional to the length of the longest string to obtain the `weighted similarity metric`. This is because, the more matching groups and the larger the average size of such groups relative to the string size, the more similar the strings are.
 
The weight of a group size in the average group size calculation is also directly proportional to the size of the group. This is because the larger the group size, the more similar the strings are. So the metrics are designed and derived using such intuition. The implementation details can be found in the commented code.

Motivation
==========
String similarity algorithms use various metrics to determine how close/far a string is from another string(s). With big dataset of strings, computing such metric requires fast and efficient algorithms. The most common existing similarity metrics are edit-distance (Levenshtein Distance), hamming distance and longest common subsequence (LCS). 

The edit-distance between two strings is the minimum number of edits (insertion, deletion and substitution) needed to change one string to the other. Here, insertions and deletions are assumed to have the same computational cost regardless of where in the string they are performed. For example edit-distance between “abcefdgh” and “abcdgh” is 2 as “abcefdgh” can be converted to “abcdgh” by removing “e” and “f”. Finding an edit distance is computationally complex. For instance finding an edit-distance between a string of length `n` and another string of length `m` using the most commonly used dynamic programming algorithm requires about `nm` comparisons making the time complexity of `O(nm)`.

Hamming distance between two strings of equal length is the number of positions at which the two strings differ. In this case,  if for instance one string is shifted to the right by adding a single character at the left, the hamming distance between the string and its shifted version can be as high as the length of the string if all characters of the string are unique.  For example, the hamming distance between “great” and “grant” is 2. Hamming distance does not efficiently consider the similarity of two strings if one string is shifted to the right or left by even a single character. For example the hamming distance between xyabcde and gabcdem is 7 even though the two strings have substring, abcde in common.

The LCS of two strings on the other hand is the longest subsequence (with characters not necessarily consecutive) common to the two strings. For instance the LCS of “ABC” and “ACB” can be “AB” or “AC”. Length of LCS here is 2. Existing algorithms to compute LCS are also slow. Many common algorithms take `O(nm)` time complexity to find LCS.

To address the shortcomings of existing string similarity algorithms we have recently proposed the above-mentioned new string similarity metrics and designed as well as implemented the efficient algorithms to compute them. By taking into account carefully designed weighted sizes of matching groups of subsequences and substrings of the strings being compared, our algorithms give very efficient string similarity scores.  

The worst and average case computational complexity of our algorithms is lower than well-known algorithms used to compute existing string similarity metrics such as edit-distance and LCS. 

The computational complexity of our algorithms to obtain the above metrics is the sum of the products of the number of repeated and matching characters in each string. If the number of repetitions of character `i` in the first string is `ni` and in the second string is `mi` , then the worst case order complexity is the sum of `ni*mi` over `N`, where `N` is the number of unique characters which appear in both the first and second string. We also have `N > 3` as there are simpler algorithms to obtain the compute distances when `N < 4`. This worst case computational complexity is less than that of the most commonly used edit-distance algorithm which is `O(nm)` where `n` and `m` are lengths of the first and second strings. For instance, if string1 and string2 both have length of 10, and if we have 2a, 3d, 5g in string1 and 3a, 3d, 4g in string2, the worst-case complexity is not 10x10 = 100. It is 2x3 + 3x3 + 5x4 = 35. As not every character in one string always appears in the second string, the average computational complexity of average strings is actually much less than this.

If string1 and string2 are non-repetitive strings with every character appearing at most once in each string, the complexity reduces to the maximum of `O(N)`, `O(n)` and  `O(m)`. This is because `ni` and `mi` each has the value of 1 in this case, as the characters do not repeat.


The Compute Distance Metric
==========================
Compute-distance algorithm takes the cost of shifting the middle characters of a string into account. The only operations in compute-distance are `replace`, `detach` and `append`.
 
`Replace` is used in the middle of a strings, `detach` and `append` are used at the beginning or end of the strings.  When `k` characters are appended at the beginning of the string, `k` characters are detached from the end of the string. Such an append operation is counted as one operation. This kind of string metric counts the actual computational cost due to shifting characters in the middle of a string, and hence called compute-distance.

Examples: The compute-distance of “abcefdgh” from “abcdgh” becomes 5 with 3 replacements of e,f,d  and 2 detachments of g,h. The edit-distance of “abcdghij”  from “abcdgh” is the same as that of “abcefdgh” which is 2. While the compute-distance of “abcefdgh” from “abcdgh” is 5 and that of “abcdghij”  from “abcdgh” is 2, as there is no cost associated with shifting characters.

Many malicious domain names are generated by inserting character(s) in or deleting characters from the middle of legit domain names. For instance a malicious domain can be facebok.com. While the edit-distance of facebok.com from facebook.com is 1, the compute-distance is 6. When classifying domain names into good and bad classes, edit-distance may put facebok.com and facebook.com into the same class as they have very small edit-distance between them. On the other hand compute-distance can easily put these two strings into two different classes as the compute-distance is larger; making web domain classification easier.

As mentioned above, the none-weighted compute distance metric is obtained by subtracting the length of the largest parallel group size from the length of the longest string. This shows how far a shorter string is from the longer one. 

The weighted compute distance on the other hand uses both lengths of the shorter and longer strings. This is particularly useful with name matching such as finding the similarity between the name DEBISH FESEHAYE and DEBISH FESEHAYE K, which are essentially the same names. In this case giving more weight to the shorter string DEBISH FESEHAYE shows perfect similarity score.

The Weighted Similarity Metrics
===============================
The subsequence and substring based weighted string similarity metrics are obtained by using the sizes of each subsequence and substring (respectively) along with the sizes of the longest and shortest strings.

Square Root Similarity Metric 
=============================
As can be seen in the code, the square root similarity metric is obtained by normalizing the square root of a function of sizes of all subsequences as well as the lengths of the longest and shortest strings.

Weighted Sum Similarity Metric
==============================
This metric is obtained by summing up the size of the longest parallel group and a function of the lengths of all other groups of parallel lines as well as the lengths of the longest and shortest strings.

The weighted similarity metrics are very strict metrics. A higher such metric value means the two strings are similar.

The square root as well as the weighted sum metrics on the other hand are less strict. A higher such metric value means that there are more matching characters than the strings are more similar.  If one wants a higher percentage representing higher similarity, these metrics are useful.


Program Usage
=============
This repository consists of string similarity tools which can tell how similar two strings are. The tools generate multiple string similarity metrics whose values range from 0 to 100. A higher metric value indicates more similarity between the two strings.

In this repository there are three program source files namely, `compStrMetricMain.cc`, `compStrMetric.h` and `compStrMetric.cc`. 
Sample input and output files are also included in the `testInputData` and `testOutputData` folders. Once the program is complied using the instructions below, the output should look like what is in one of the output files. The same result is also displayed as a standard output (on the screen) when the program runs.


Dependency
==========
The makefile uses `g++`. Hence `g++` needs to be installed to use the current `Makefile` as it is.


Building and Running
=====================
To run the program in a Linux environment, do the following.

1. Download the archive file and unzip it.
2. In the unzipped folder, type `make` to generate the binary file.
3. Prepare an input file with two input strings separated with the "|" character.  
4. Run the program by typing  `./computeMetrics inputFile outputFile` where `inputFile` and `outputFile` are input and output files respectively.
5. Get the output from the standard output and from the output files in the output folder.
6. If you copy the `computeMetrics` binary into the `/bin` folder in a Macbook Pro for instance, it serves just as a Linux command performing what the `computeMetrics` tool is designed to do, those that are described above. It becomes similar to the well known Linux commands such as `ls`, `sync`, `chmod` ... etc. In this case, the tool is run as `computeMetrics inputFile outputFile` from any folder in the Macbook Pro.

Testing
==========
Some sample test input files are given in the `testInputData` folder. The names in this folder are extracted from [2]. The corresponding output files are given in `testOutputData` folder. After compiling the program successfully, running the `computeMetrics` binary with the input files in the testInputData folder should generate the strings with the corresponding metric values in a format which is the same as those in the `testOutputData` floder.
The program can be run with the test files as `./computeMetrics testInputData/testInputFile testOuputData/testOutputFile` where `testInputFile` can be any of the files in the `testInputData` folder. 
This can then generate the `testOputputFile` in the `testOutputData` folder.


Contributors
============
If you find a bug or would like to suggest improvements, please use the issue
tracker available on GitHub.


License
=======
This tool is made available under the Apache License, Version 2.0 (see [1] for details).


[1] http://www.apache.org/licenses/LICENSE-2.0.html

[2] http://www.ssa.gov/cgi-bin/popularnames.cgi


