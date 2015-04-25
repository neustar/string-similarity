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

// A structure consisting of variables used to obtain the string
// similarity scores
public class StrMetricVariables{
    // Counts the total number of characters in one string that
    // have matching characters in the other string
    public int totalMatchCount;
    // Counts the number of matching characters common to more
    // than one subsequence group
    public int numCommonChars;
    // Accumulates the sum of the squares of the subsequence group
    // sizes for the subsequence based string similarity metric compuations
    public double sSeqSquaresSumNew;
    // Accumulates the sum of the squares of the subsequence
    // group sizes for the substring based string similarity metric compuations
    public double sStrSquaresSumNew;
    // Maintains the maximum subsequence group size used
    // for the compute distance based string similarity metrics
    public int highestSseqGrpSizeNew;
    public StrMetricVariables(){
		totalMatchCount = 0;
		numCommonChars = 0;
		sSeqSquaresSumNew = 0.0;
		sStrSquaresSumNew = 0.0;
		highestSseqGrpSizeNew = 0;	
   }
}
