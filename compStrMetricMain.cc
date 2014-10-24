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
#include <list>
#include "compStrMetric.h"

// Length of the inpute line (the two strings to be compared)
#define INPUTLINELEN 200
//
using namespace std;
//
// The main function starts the string similarity program.
// It takes two files as inputs.
// The first file consists of the pairs of strings whose
// string similarity values are to be obtained by the computeMetrics
// string similarity program.
// The output file is filled with program output after the program finishes running.
// The output file consists of the pairs of strings to be compared and
// their corresponding string similarity score values
//
// This main function calls the computeStrMetric function by passing
// a pair of strings.
// The computeStrMetric function returns the strMetricStructReturnLocal
// structure which consists of the string similarity scores of the two strings.
//
// The main function then puts the output into the output file and also
// displays it on the standard output (screen).

int main(int argc, char** argv) {
    // Program usage
    if (argc < 1) {
        printf("Program usage: ./computeMetrics inputFilePath/inputFile outputFilePath/outputFile  \n");
        return -1;
    }
    if (strcmp(argv[1], argv[2]) == 0) {
        printf("Input and output file names should not be the same.\n");
        return -1;
    }
    // String input file
    FILE* stringsFile = fopen(argv[1], "r");
    //
    // Output file
    FILE* outputFile = fopen(argv[2], "w");
    //
    // Return structure of the string metric computation functions
    strMetricStruct strMetricStructReturnLocal;
    //
    // Two strings passed to the string metric function
    char* compDistArg[2];
    //
    // The whole input line. A full name is not longer than 100 characters (assumption)
    char inputeLine[INPUTLINELEN];
    //
    // For the input tokenizer
    int lindex = 0;
    //
    printf("string1                                    string2                                compDistPerc wcompDistPerc sseqWsPerc  sstrWsPerc sqrtsseqWsPerc wsumsseqWsPerc   matchRatio  \n");
    printf("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
    // 
    // And in output file
    fprintf(outputFile, "string1                                    string2                                compDistPerc wcompDistPerc sseqWsPerc  sstrWsPerc sqrtsseqWsPerc wsumsseqWsPerc     matchRatio  \n");
    fprintf(outputFile, "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
    //
    // Read the two strings from every inpute line
    // Get the string metric values and
    // Write results to standard output and to output file
    //
    while (fgets(inputeLine, INPUTLINELEN, stringsFile) != NULL) {
        if (inputeLine[strlen(inputeLine)-1] == '\n') {
            inputeLine[strlen(inputeLine)-1] = '\0';
        }
        //
        lindex = 0;
        compDistArg[lindex] = strtok(inputeLine, "|");
        while (compDistArg[lindex] != NULL) {
            lindex++;
            compDistArg[lindex] = strtok(NULL, "|");
        }
        //
        strMetricStructReturnLocal = computeStrMetric(compDistArg);
        //
        // Write results to standard output (screen)
        printf("%-40s %-40s %-12f %-12f %-12f %-12f %-12f  %-12f  %-12f \n", compDistArg[0], compDistArg[1], strMetricStructReturnLocal.compDistPercent, strMetricStructReturnLocal.wcompDistPercent, strMetricStructReturnLocal.sseqWsPercent, strMetricStructReturnLocal.sstrWsPercent, strMetricStructReturnLocal.sqrtsseqWsPercent, strMetricStructReturnLocal.wsumsseqWsPercent, strMetricStructReturnLocal.matchRatio);
        //
        // Write results to output file
        fprintf(outputFile, "%-40s %-40s %-12f %-12f %-12f %-12f %-12f  %-12f  %-12f \n", compDistArg[0], compDistArg[1], strMetricStructReturnLocal.compDistPercent, strMetricStructReturnLocal.wcompDistPercent, strMetricStructReturnLocal.sseqWsPercent, strMetricStructReturnLocal.sstrWsPercent, strMetricStructReturnLocal.sqrtsseqWsPercent, strMetricStructReturnLocal.wsumsseqWsPercent, strMetricStructReturnLocal.matchRatio);
    }
    //
    // CLose input and output files
    fclose(stringsFile);
    fclose(outputFile);
    //
    return 0;
}
