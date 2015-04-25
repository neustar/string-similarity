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

package compStrMetric;

import compStrMetricHeader.StrMetricVariables;
import compStrMetricHeader.CharInfo;
import compStrMetricHeader.SubSequenceStruct;
import compStrMetricHeader.StrMetricStruct;
//
//
public class CompStrMetric{
    //
    // Define some constants
    static final int MAXSTRLEN = 100;
    static final int MAXNUMSSTR = 300;
    // The following are used for string metric percentile computations
    //
    // Fraction of the larger of the lengths of two strings for
    // Subsequence metric computation
    static final double SSEQ_LONGSTR_FRAC = 0.2;
    //
    // Fraction of the larger of the lengths of two strings for
    // the match ratio metric computation
    static final double MRATIO_LONGSTR_FRAC = 1.0;
    //
    static final double ONE_HUNDRED = 100.0;
    //
    // Additional public variables used in the computeMetricPercents method
    public static StrMetricStruct strMetricStructReturn;
    public static int strLength;
    public static int minLength;
    //
    // A structure for each ASCII values of strings 1 and 2
    public static CharInfo[] chInfo1; 
    public static CharInfo[] chInfo2;
    // The following two structures are used to obtain parameters for the metrics computations
    public static SubSequenceStruct[] posSubSeqNew;
    public static SubSequenceStruct[] negSubSeqNew;
    //
    // To remove double-counting for some of the metrics
    public static boolean[][] isVisitedChar;
    //
    // A collection of variables used in the metric computations
    public static StrMetricVariables strMetVariable;
    //
    // Each group's incline value
    public static int incValNew;
    // To public static void double-counting groups with incline of 0
    public static boolean secondCall;       
    //
    //
    // This countGroupCommonChars function updates the number of
    // characters common to multiple sub sequence groups
    // by incrementing the numCommonChars variable in
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

    public static void countGroupCommonChars(int i) {
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
    // strMetVariable variables passed to it by reference. See descriptions
    // of these variables above the computeStrMetric function and in the
    // compStrMetricHeader package. Such modified variables are then used by the
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


    public static void computeStrMetricHelper(int i, String inputStr, CharInfo[] chInfo, SubSequenceStruct[] subSeqNew) {
        //
        int incValNew;
        //
        if (chInfo[(int)inputStr.charAt(i)].cntr > 0) {
            for (int m = 0; m < chInfo[(int)inputStr.charAt(i)].cntr; m++) {
                if (chInfo[(int)inputStr.charAt(i)].chPos[m] > -1) {
                    incValNew = i - chInfo[(int)inputStr.charAt(i)].chPos[m];
                    //
                    if (!secondCall || incValNew > 0) {
                        // Fill in the subsequence struct
                        subSeqNew[incValNew].cntr++;
                        subSeqNew[incValNew].subSeqList.add((int)inputStr.charAt(i));
                        //
                        // Increment the total match count
                        strMetVariable.totalMatchCount++;
                        //
                        // Increase the count if character is in another incline group
                        countGroupCommonChars(i);
                        //
                        // If previous character of the string is in this incline group
                        if ((i > 0) && (subSeqNew[incValNew].subStrArray[subSeqNew[incValNew].subStrCntr].cntr > 0) && 
                        		(inputStr.charAt(i-1) == subSeqNew[incValNew].subStrArray[subSeqNew[incValNew].subStrCntr].subStrList.getLast())) {
                            // Increment the incline group
                            subSeqNew[incValNew].subStrArray[subSeqNew[incValNew].subStrCntr].cntr++;
                            // Add character to the previous substring group
                            subSeqNew[incValNew].subStrArray[subSeqNew[incValNew].subStrCntr].subStrList.add((int)inputStr.charAt(i));
                        } else {
                            // Start a new substring group
                            subSeqNew[incValNew].subStrCntr++;	
                            subSeqNew[incValNew].subStrArray[subSeqNew[incValNew].subStrCntr].cntr++;
                            subSeqNew[incValNew].subStrArray[subSeqNew[incValNew].subStrCntr].subStrList.add((int)inputStr.charAt(i));
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
    // inputs. See compStrMetricHeader package for description of these variables.
    // This computeMetricPercents then obtains the string similarity values
    // in percentages and returns them to the calling function
    // in the strMetricStructReturn structure which consists of all
    // the string similarity metric values.

    public static void computeMetricPercents() {
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
        // Subsequence based square-root similarity metric
        strMetricStructReturn.sqrtsseqWsPercent = ONE_HUNDRED*Math.sqrt(strMetVariable.sSeqSquaresSumNew) /(SSEQ_LONGSTR_FRAC*strLength + (1.0-SSEQ_LONGSTR_FRAC)*minLength);
        if (strMetricStructReturn.sqrtsseqWsPercent > ONE_HUNDRED) {
            strMetricStructReturn.sqrtsseqWsPercent = ONE_HUNDRED;
        }
        //
        // Subsequence based weighted sum similarity metric
        double temp1;
        if (strMetVariable.totalMatchCount > 0) {
            temp1 = 1.0*(strMetVariable.sSeqSquaresSumNew - Math.pow(strMetVariable.highestSseqGrpSizeNew, 2))/(1.0*strMetVariable.totalMatchCount);
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
    // their string similarity values (metrics) as output.
    // The metric value is displayed in the form of percentages.
    //
    // This function calls the computeStrMetricHelper function twice.
    //
    // In the first call, the function updates the subsequence and
    // substring group counts as well as the strMetVariable
    // variables (See compStrMetricHeader package for detailed description).
    // To do this the function for this call, loops for a single
    // character in string 1 to every matching characters
    // in string 2 visited so far.
    //
    // In the first call the function takes the following variables as inputs.
    // Position of character i in string 2,
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
    // every matching characters in string 1 visited so far, also
    // takes the same variables with the exception
    // of the first string, argv[0], instead of the second
    // string, argv[1] and chInfo2 instead of chInfo1.
    //
    // The computeStrMetricHelper function then modifies the variables
    // subSeqNew, isVisitedChar and strMetVariable which are passed to it
    // by reference to be used by the computeStrMetric function.
    //
    // After the computeStrMetric function gets the values of the
    // strMetVariable variables (See compStrMetricHeader package )
    // using the computeStrMetricHelper function after the for loop on
    // the two strings, it calls the computeMetricPercents function.
    // The computeMetricPercents returns the strMetricStruct variables
    // which are the string similarity metrics in percentages using the
    // strMetricStructReturn function passed to it by reference.

    public static StrMetricStruct computeStrMetric(String[] argv) {
        // Longest string length for the metrics computation
        strLength = argv[0].length();
        if (strLength < (int)argv[1].length()) {
            strLength = argv[1].length();
        }
        // Shortest string
        minLength = argv[0].length();
        if (minLength > (int)argv[1].length()) {
            minLength = (int)argv[1].length();
        }
        //
        // A structure for each ASCII values of strings 1 and 2
        chInfo1 = new CharInfo[256]; 
        chInfo2 = new CharInfo[256];
        for(int m = 0; m < 256; m++){
            chInfo1[m] = new CharInfo();
            chInfo2[m] = new CharInfo();
        }
        //
        // The following two structures are to test the new scheme
        posSubSeqNew = new SubSequenceStruct[MAXSTRLEN];
        negSubSeqNew = new SubSequenceStruct[MAXSTRLEN];
        for(int m = 0; m < MAXSTRLEN; m++){
        	posSubSeqNew[m] = new SubSequenceStruct();
        	negSubSeqNew[m] = new SubSequenceStruct();
        }
        //
        // To remove double-counting for some of the metrics
        isVisitedChar = new boolean[2][MAXSTRLEN];
        //
        // A collection of variables used in the metric computations and 
        // their initialization
        strMetVariable = new StrMetricVariables();
        //
        // To a public static void double-counting groups with incline of 0
        secondCall = false;
        //        
        for (int i = 0; i < strLength; i++) {
            // Character is not in any incline group yet
            isVisitedChar[0][i] = false;
            isVisitedChar[1][i] = false;
            //
            if (i < (int)argv[0].length()) {
                chInfo1[(int)argv[0].charAt(i)].chPos[chInfo1[(int)argv[0].charAt(i)].cntr] = i;
                chInfo1[(int)argv[0].charAt(i)].cntr++;
            }
            //
            if (i < (int)argv[1].length()) {
                chInfo2[(int)argv[1].charAt(i)].chPos[chInfo2[(int)argv[1].charAt(i)].cntr] = i;
                chInfo2[(int)argv[1].charAt(i)].cntr++;
            }
            // If the character in str2 (argv[1]) also exists in
            // str1 (argv[0]) and if not matched (marked) yet
            if (i < (int)argv[1].length()) {
                secondCall = false;
                computeStrMetricHelper(i, argv[1], chInfo1, negSubSeqNew);
            }
            // If the character in str1 (argv[0]) also exists in
            // str2 (argv[1]) and if not matched (marked) yet
            if (i < (int)argv[0].length()) {
                secondCall = true;
                computeStrMetricHelper(i, argv[0], chInfo2, posSubSeqNew);
            }
        }
        // Compute the percentages and return the percentage results structure
        strMetricStructReturn = new StrMetricStruct();
        computeMetricPercents();
        return strMetricStructReturn;
    }
}
