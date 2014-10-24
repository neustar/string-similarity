/*
 Copyright 2014 Neustar Inc.
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */


#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <map>
#include "compStrMetric.h"

// The following are used for string metric percentile computations
//
// Fraction of the larger of the lengths of two strings for
// subsequenec metric computation
#define SSEQ_LONGSTR_FRAC 0.2
//
// Fraction of the larger of the lengths of two strings for
// the match ratio metric computation
#define MRATIO_LONGSTR_FRAC 1.0
//
#define ONE_HUNDRED 100.0


using namespace std;

// This countGroupCommonChars function updates the number of
// characters common to multiple sub sequence groups
// by increamenting the numCommonChars variable in
// the strMetVariable structure.
//
// If it is the first call of the computeStrMetricHelper function
// and hence this countGroupCommonChars function (!secondCall),
// which is from a single character in string 1 to every matching
// characters in string 2 visited so far (see description above
// computeStrMetric below), the function marks the character as
// visited in string 2 (argv[1]) described by the isVisitedChar[1][i]
// for character i.
//
// Otherwise the function marks the character as visited in
// string 1 (argv[0]) described by the isVisitedChar[0][i].

void countGroupCommonChars(int i, bool (& isVisitedChar)[2][MAXSTRLEN], bool secondCall, strMetricVariables & strMetVariable, int incValNew) {
    if (!secondCall) {
        if (!isVisitedChar[1][i]) {
            // If destination pair (negative incline: from str2 to str1)
            // is marked then the other character is double-counted
            if (isVisitedChar[0][i-incValNew]) {
                strMetVariable.numCommonChars++;
            }
            isVisitedChar[1][i]  = true;
        } else {
            strMetVariable.numCommonChars++;
        }
    } else {
        if (!isVisitedChar[0][i]) {
            // If destination pair (positive incline: from str1 to str2)
            // is marked then the other character is double-counted
            if (isVisitedChar[1][i-incValNew]) {
                strMetVariable.numCommonChars++;
            }
            isVisitedChar[0][i]  = true;
        } else {
            strMetVariable.numCommonChars++;
        }
    }
}

// Helper function of the computeStrMetric function
//
// This helper function of the computeStrMetric function
// takes the variables described on top of the computeStrMetric
// function (below) and modifies the subSeqNew, isVisitedChar,
// strMetVariable varaibles passed to it by reference. See descriptions
// of these variables above the computeStrMetric function and in the
// compStrMetric.h. Such modified variables are then used by the
// computeStrMetric function to obtain the string similarity metrics.
//
// Using the position of the matching character found in the
// chInfo structure, the computeStrMetric function finds an incline value.
// And then updates the counts of the subsequence groups (subSeqNew structure)
// and substring groups found in each subsequence group
// determined by the incline value.
//
// These counts are used to accumulate the sSeqSquaresSumNew and
// sStrSquaresSumNew values in the strMetVariable structure used to
// to calculate the subsequence and substring based string similarity metrics.
//
// This computeStrMetricHelper also maintains the highestSseqGrpSizeNew
// variable of the strMetVariablem structure which is used
// to calculate the compute distance based string similarity metrics.
//
// To count the number of characters which occur in multiple groups, this
// computeStrMetricHelper function also calls
// the countGroupCommonChars function.


void computeStrMetricHelper(int i, char* inputStr, charInfo (& chInfo)[256], subSequenceStruct (& subSeqNew)[MAXSTRLEN], bool (& isVisitedChar)[2][MAXSTRLEN], strMetricVariables & strMetVariable, bool secondCall) {
    //
    int incValNew;
    //
    if (chInfo[(int)inputStr[i]].cntr > 0) {
        for (int m = 0; m < chInfo[(int)inputStr[i]].cntr; m++) {
            if (chInfo[(int)inputStr[i]].chPos[m] > -1) {
                incValNew = i - chInfo[(int)inputStr[i]].chPos[m];
                //
                if (!secondCall || incValNew > 0) {
                    // Fill in the subsequence struct
                    subSeqNew[incValNew].cntr++;
                    subSeqNew[incValNew].subSeqList.push_back((int)inputStr[i]);
                    //
                    // Increament the total match count
                    strMetVariable.totalMatchCount++;
                    //
                    // Increase the count if character is in another incline group
                    countGroupCommonChars(i, isVisitedChar, secondCall, strMetVariable, incValNew);
                    //
                    // If previous character of the string is in this incline group
                    if ((i > 0) && (inputStr[i-1] == subSeqNew[incValNew].subStrArray[subSeqNew[incValNew].subStrCntr].subStrList.back())) {
                        // Increament the incline group
                        subSeqNew[incValNew].subStrArray[subSeqNew[incValNew].subStrCntr].cntr++;
                        // Add character to the previous substring group
                        subSeqNew[incValNew].subStrArray[subSeqNew[incValNew].subStrCntr].subStrList.push_back(inputStr[i]);
                    } else {
                        // Start a new substring group
                        subSeqNew[incValNew].subStrCntr++;
                        subSeqNew[incValNew].subStrArray[subSeqNew[incValNew].subStrCntr].cntr++;
                        subSeqNew[incValNew].subStrArray[subSeqNew[incValNew].subStrCntr].subStrList.push_back(inputStr[i]);
                    }
                    // For the weighted metric computations
                        strMetVariable.sSeqSquaresSumNew += (2*subSeqNew[incValNew].cntr - 1);
                        strMetVariable.sStrSquaresSumNew += (2*subSeqNew[incValNew].subStrArray[subSeqNew[incValNew].subStrCntr].cntr - 1);
                    //
                    // For compute distance metric computation
                    if (strMetVariable.highestSseqGrpSizeNew < subSeqNew[incValNew].cntr) {
                        strMetVariable.highestSseqGrpSizeNew = subSeqNew[incValNew].cntr;
                    }
                }
            }
        }
    }
}

// This function takes the strMetVariable variables (totalMatchCount,
// numCommonChars, sSeqSquaresSumNew,sStrSquaresSumNew, highestSseqGrpSizeNew)
// and the lengths strLength and minLength of the longer and shorter strings as
// inputs. See compStrMetric.h for description of these variables.
// This computeMetricPercents then obtains the string similarity values
// in percentages and returns them to the calling function
// in the strMetricStructReturn structure which consists of all
// the string similiarity metric values.

void computeMetricPercents(strMetricStruct & strMetricStructReturn, strMetricVariables & strMetVariable, int strLength, int minLength) {
    // The compute distance percentile
    strMetricStructReturn.compDistPercent = ONE_HUNDRED*strMetVariable.highestSseqGrpSizeNew/(1.0*strLength);
    //
    // The weighted compute distance variant
    strMetricStructReturn.wcompDistPercent = ONE_HUNDRED*strMetVariable.highestSseqGrpSizeNew/(SSEQ_LONGSTR_FRAC*strLength + (1.0-SSEQ_LONGSTR_FRAC)*minLength);
    //
    // Subsequence based weighted similarity metric
    strMetricStructReturn.sseqWsPercent = 1.0*strMetVariable.sSeqSquaresSumNew/(1.0*strLength);
    strMetricStructReturn.sseqWsPercent = ONE_HUNDRED*strMetricStructReturn.sseqWsPercent/(1.0*minLength);
    if (strMetricStructReturn.sseqWsPercent > ONE_HUNDRED) {
        strMetricStructReturn.sseqWsPercent = ONE_HUNDRED;
    }
    //
    // Substring based weighted similarity metric
    strMetricStructReturn.sstrWsPercent = 1.0*strMetVariable.sStrSquaresSumNew/(1.0*strLength);
    strMetricStructReturn.sstrWsPercent = ONE_HUNDRED*strMetricStructReturn.sstrWsPercent/(1.0*minLength);
    if (strMetricStructReturn.sstrWsPercent > ONE_HUNDRED) {
        strMetricStructReturn.sstrWsPercent = ONE_HUNDRED;
    }
    //
    // Subsequence based squareroot similarity metric
    strMetricStructReturn.sqrtsseqWsPercent = ONE_HUNDRED*sqrt(strMetVariable.sSeqSquaresSumNew) /(SSEQ_LONGSTR_FRAC*strLength + (1.0-SSEQ_LONGSTR_FRAC)*minLength);
    if (strMetricStructReturn.sqrtsseqWsPercent > ONE_HUNDRED) {
        strMetricStructReturn.sqrtsseqWsPercent = ONE_HUNDRED;
    }
    //
    // Subsequence based weighted sum similarity metric
    double temp1;
    if (strMetVariable.totalMatchCount > 0) {
        temp1 = 1.0*(strMetVariable.sSeqSquaresSumNew - pow(strMetVariable.highestSseqGrpSizeNew, 2))/(1.0*strMetVariable.totalMatchCount);
    } else {
        temp1 = 0;
    }
    strMetricStructReturn.wsumsseqWsPercent = ONE_HUNDRED*(1.0*strMetVariable.highestSseqGrpSizeNew + temp1)/(SSEQ_LONGSTR_FRAC*strLength + (1.0-SSEQ_LONGSTR_FRAC)*minLength);
    if (strMetricStructReturn.wsumsseqWsPercent > ONE_HUNDRED) {
        strMetricStructReturn.wsumsseqWsPercent = ONE_HUNDRED;
    }
    //
    // Ratio of matching characters to weighted length of the two strings
    strMetricStructReturn.matchRatio = ONE_HUNDRED*(strMetVariable.totalMatchCount - strMetVariable.numCommonChars) /(MRATIO_LONGSTR_FRAC*strLength + (1.0 -MRATIO_LONGSTR_FRAC)*minLength);
    if (strMetricStructReturn.matchRatio > ONE_HUNDRED) {
        strMetricStructReturn.matchRatio = ONE_HUNDRED;
    }
    //
}

// A function which takes a pair of strings as input and returns
// their string similarity vaues (metrics) as output.
// The metric value is displayed in the form of percentages.
//
// This function calls the computeStrMetricHelper function twice.
//
// In the first call, the function updates the subsquence and
// substring group counts as well as the strMetVariable
// variables (See compStrMetric.h for detailed description).
// To do this the function for this call, loops for a single
// character in string 1 to every matching characters
// in string 2 visited so far.
//
// In the first call the function takes the following variables as inputs.
// Position of charater i in string 2,
// Pointer to the second string, argv[1],
// Reference to the array, chInfo1, of information of characters in string 1,
// The flag, visitedChar, showing whether or not the character i
// is previously matched,
// The strMetVariable structure consisting of variables used to
// calculate the string metric values,
// The secondCall flag showing whether or not it is the first
// call of the function, computeStrMetricHelper.
//
// The second call of the computeStrMetricHelper function,
// which loops for a single character in string 2 to
// every matching characters in string 1 visted so far, also
// takes the same variables with the exception
// of the first string, argv[0], instead of the second
// string, argv[1] and chInfo2 instead of chInfo1.
//
// The computeStrMetricHelper function then modifies the variabes
// subSeqNew, isVisitedChar and strMetVariable which are passed to it
// by reference to be used by the computeStrMetric function.
//
// After the computeStrMetric function gets the values of the
// strMetVariable variables (See compStrMetric.h)
// using the computeStrMetricHelper function after the for loop on
// the two strings, it calls the computeMetricPercents function.
// The computeMetricPercents returns the strMetricStruct vairables
// which are the string similarity metrics in percentages using the
// strMetricStructReturn function passed to it by reference.

strMetricStruct computeStrMetric(char* argv[]) {
    // Longest string length for the metrics computation
    int strLength = strlen(argv[0]);
    if (strLength < (int)strlen(argv[1])) {
        strLength = strlen(argv[1]);
    }
    // Shortest string
    int minLength = strlen(argv[0]);
    if (minLength > (int)strlen(argv[1])) {
        minLength = (int)strlen(argv[1]);
    }
    //
    // A structure for each ASCII values of strings 1 and 2
    charInfo chInfo1[256]; charInfo chInfo2[256];
    //
    // The following two structures are to test the new scheme
    subSequenceStruct posSubSeqNew[MAXSTRLEN], negSubSeqNew[MAXSTRLEN];
    //
    // To remove double-counting for some of the metrics
    bool visitedChar[2][MAXSTRLEN];
    //
    // A collection of variables used in the metric computations
    strMetricVariables strMetVariable;
    // Initialize variables
    strMetVariable.sSeqSquaresSumNew = 0.0;
    strMetVariable.sStrSquaresSumNew = 0.0;
    strMetVariable.highestSseqGrpSizeNew = 0;
    strMetVariable.numCommonChars = 0;
    strMetVariable.totalMatchCount = 0;
    //
    // To avoid double-counting groups with incline of 0
    bool secondCall = false;
    //
    for (int i = 0; i < strLength; i++) {
        // Character is not in any incline group yet
        visitedChar[0][i] = false;
        visitedChar[1][i] = false;
        //
        if (i < (int)strlen(argv[0])) {
            chInfo1[(int)argv[0][i]].chPos[chInfo1[(int)argv[0][i]].cntr] = i;
            chInfo1[(int)argv[0][i]].cntr++;
        }
        //
        if (i < (int)strlen(argv[1])) {
            chInfo2[(int)argv[1][i]].chPos[chInfo2[(int)argv[1][i]].cntr] = i;
            chInfo2[(int)argv[1][i]].cntr++;
        }
        // If the character in str2 (argv[1]) also exists in
        // str1 (argv[0]) and if not matched (marked) yet
        if (i < (int)strlen(argv[1])) {
            secondCall = false;
            computeStrMetricHelper(i, argv[1], chInfo1, negSubSeqNew, visitedChar, strMetVariable, secondCall);
        }
        // If the character in str1 (argv[0]) also exists in
        // str2 (argv[1]) and if not matched (marked) yet
        if (i < (int)strlen(argv[0])) {
            secondCall = true;
            computeStrMetricHelper(i, argv[0], chInfo2, posSubSeqNew, visitedChar, strMetVariable, secondCall);
        }
    }
    // Compute the percentages
    strMetricStruct strMetricStructReturn;
    computeMetricPercents(strMetricStructReturn, strMetVariable, strLength, minLength);
    return strMetricStructReturn;
}

