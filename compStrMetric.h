/*
 Copyright 2014 Neustar Inc.
 
 Licensed under the Apache License, Version 2.0(the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

#ifndef COMPSTRMETRIC_H_
#define COMPSTRMETRIC_H_

#include <list>

#define MAXSTRLEN 100
#define MAXNUMSSTR 300

using namespace std;

// A stucture for a single character of a string
class charInfo{
 public:
        // Number of occurances of character in a string
        int cntr;
        // Position of the character cntr in the string
        int chPos[MAXSTRLEN];
        // Initialize the character counter and posistions in the string
        charInfo(){
            cntr = 0;
            for (int i = 0; i < MAXSTRLEN; i++){
                chPos[i] = -1;
            }
        }
};

// A structure for the different string similarity scores returned by
// the computeStrMetric function
typedef struct strMetricStruct{
    // Compute-distance metric based on longest matching
    // subsequence group and length of longest string
    double compDistPercent;
    // Weighted compute-distance metric based on longest matching subsequence
    // group and a weighted average of longest and shortest string
    double wcompDistPercent;
    // Wieghted metric based on sizes of all subsequences and
    // lengths of the two strings
    double sseqWsPercent;
    // Wieghted metric based on sizes of all substrings and
    // lengths of the two strings
    double sstrWsPercent;
    // Square root metric based on sizes of all subsequences
    // and weighted lengths of the two strings
    double sqrtsseqWsPercent;
    // Wieghted metric based on size of longest subsequence, sizes of all
    // other subsequences and weighted average of lengths of the two strings
    double wsumsseqWsPercent;
    // This is a commonly known metric which obtains the ratio of matching
    // characters to the weighted average of the lengths of the two strings.
    double matchRatio;
};

// A structure consisting of variables used to obtain the string
// similarity scores
typedef struct strMetricVariables{
    // Counts the total number of characters in one string that
    // have matching characters in the other string
    int totalMatchCount;
    // Counts the number of matching characters common to more
    // than one subsequence group
    int numCommonChars;
    // Accumulates the sum of the squares of the subsequence group
    // sizes for the subsequence based string similarity metric compuations
    double sSeqSquaresSumNew;
    // Accumulates the sum of the squares of the subsequence
    // group sizes for the substring based string similarity metric compuations
    double sStrSquaresSumNew;
    // Maintains the maximum subsequence group size used
    // for the compute distance based string similarity metrics
    int highestSseqGrpSizeNew;
};

// A structure for each substring group in the string similarity
// score calculations
class subStringStruct{
 public:
        // Number of characters which fall into the same substring
        // group(same incline)
        int cntr;
        // List of characters in the substring
        // Initialize the substring variables
        list <int> subStrList;
        subStringStruct(){
            cntr = 0;
            subStrList.empty();
        }
};

// A structure for each subsequence group in the string similarity
// score calculations
class subSequenceStruct{
 public:
        // Counts the size of the subsequence group which is
        // number of matching characters with the same incline value
        int cntr;
        // Number of substrings in a subsequence
        int subStrCntr;
        // List of characters in a subsequence
        list <int> subSeqList;
        // The array of substrings in a subsequence
        subStringStruct subStrArray[MAXNUMSSTR];
        // To mark beginning of substring in a subsequence
        int prevCharPos;
        // Initialize the subsequence parameters
        subSequenceStruct(){
            cntr = 0;
            subStrCntr = 0;
            subSeqList.empty();
            prevCharPos = 0;
        }
};

// The main function which takes two strings as input and returns
// the string similarity metrics as output
strMetricStruct computeStrMetric(char* argv[]);

#endif  // COMPSTRMETRIC_H_
