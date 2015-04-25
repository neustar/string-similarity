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


// Package header classes into one
package compStrMetricHeader;

// A structure for the different string similarity scores returned by
// the computeStrMetric function
public class StrMetricStruct{
    // Compute-distance metric based on longest matching
    // subsequence group and length of longest string
    public double compDistPercent;
    // Weighted compute-distance metric based on longest matching subsequence
    // group and a weighted average of longest and shortest string
    public double wcompDistPercent;
    // Wieghted metric based on sizes of all subsequences and
    // lengths of the two strings
    public double sseqWsPercent;
    // Wieghted metric based on sizes of all substrings and
    // lengths of the two strings
    public double sstrWsPercent;
    // Square root metric based on sizes of all subsequences
    // and weighted lengths of the two strings
    public double sqrtsseqWsPercent;
    // Wieghted metric based on size of longest subsequence, sizes of all
    // other subsequences and weighted average of lengths of the two strings
    public double wsumsseqWsPercent;
    // This is a commonly known metric which obtains the ratio of matching
    // characters to the weighted average of the lengths of the two strings.
    public double matchRatio;
    public StrMetricStruct(){
		compDistPercent = 0.0;
		wcompDistPercent = 0.0;
		sseqWsPercent = 0.0;
		sstrWsPercent = 0.0;
		sqrtsseqWsPercent = 0.0;
		wsumsseqWsPercent = 0.0;
    }  	
}
