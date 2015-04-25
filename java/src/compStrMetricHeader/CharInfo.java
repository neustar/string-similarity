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

// A stucture for a single character of a string
public class CharInfo{
    static final int MAXSTRLEN = 100;
    // Number of occurances of character in a string
    public int cntr;
    // Position of the character cntr in the string
    public int[] chPos = new int[MAXSTRLEN];
    // Initialize the character counter and posistions in the string
    public CharInfo(){
            cntr = 0;
            for (int i = 0; i < chPos.length; i++){
                chPos[i] = -1;
            }
        }
}
