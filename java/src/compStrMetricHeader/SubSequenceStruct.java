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

//import java.util.List; // For the list collection
import java.util.*; // For the list collection

// A structure for each subsequence group in the string similarity
// score calculations
public class SubSequenceStruct{
    static final int MAXNUMSSTR = 300;
    // Counts the size of the subsequence group which is
    // number of matching characters with the same incline value
    public int cntr;
    // Number of substrings in a subsequence
    public int subStrCntr;
    // List of characters in a subsequence
    public LinkedList <Integer> subSeqList = new LinkedList<Integer>();
    // The array of substrings in a subsequence
    public SubStringStruct[] subStrArray  = new SubStringStruct[MAXNUMSSTR];
    // To mark beginning of substring in a subsequence
    public int prevCharPos;
    // Initialize the subsequence parameters
    public SubSequenceStruct(){
        cntr = 0;
        subStrCntr = 0;
        subSeqList.clear();
        prevCharPos = 0;
        for(int i = 0; i < MAXNUMSSTR; i++){
        	subStrArray[i] = new SubStringStruct();
        }
    }
}

