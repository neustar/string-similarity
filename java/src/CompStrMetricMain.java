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

import java.io.*;

import compStrMetricHeader.StrMetricStruct;
import compStrMetric.CompStrMetric;

public class CompStrMetricMain{
    // Length of the input line (the two strings to be compared)
    static final int INPUTLINELEN = 200;
    
    // Input and output files
    static FileOutputStream ofstream;
    static FileInputStream ifstream;
    static BufferedReader stringsFile;
    static BufferedWriter stringsFileOutput;
    //
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

    public static void main(String[] argv) {
        // Read input and output files
        try{
            // String input file being read as an input parameter (argv[0])
            ifstream = new FileInputStream(argv[0]);
            stringsFile = new BufferedReader(new InputStreamReader(ifstream));
            //
            // Output file
          stringsFileOutput = new BufferedWriter(new FileWriter(argv[1]));
  
        } catch (IOException e) {
            System.out.print("Exception");
            System.out.println("Program usage: java -cp bin CompStrMetricMain inputFilePath/inputFile outputFilePath/outputFile");
        }
        //
        // Return structure of the string metric computation functions
        StrMetricStruct strMetricStructReturnLocal = new StrMetricStruct();
        //
        // Two strings passed to the string metric function
        String[] compDistArg = new String[2];
        //
        // The whole input line. A full name is not longer than 100 characters (assumption)
        String inputLine;
        // For standard output
        System.out.println("string1                                    string2                                compDistPerc wcompDistPerc sseqWsPerc  sstrWsPerc sqrtsseqWsPerc wsumsseqWsPerc   matchRatio  ");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        // Program output in an output file, stringsFileOutput
        try {
	        stringsFileOutput.write("string1                                    string2                                compDistPerc wcompDistPerc sseqWsPerc  sstrWsPerc sqrtsseqWsPerc wsumsseqWsPerc     matchRatio  "+"\n");
	        stringsFileOutput.write("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"+"\n");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("File writing did not work"+"\n");
			e1.printStackTrace();
		}
        
        //
        // Read the two strings from every input line
        // Get the string metric values and
        // Write results to standard output and to output file
        //
        try{
            while ((inputLine = stringsFile.readLine()) != null) {
                String[] strLine = inputLine.split("\\|");
                //
                compDistArg[0] = strLine[0];
                compDistArg[1] = strLine[1];
                //
                // Pass the two strings to the computeStrMetric function in the CompStrMetric class
                strMetricStructReturnLocal = CompStrMetric.computeStrMetric(compDistArg);
                //
                // Write results to standard output (screen)
                System.out.println(String.format("%-40s %-40s %-12f %-12f %-12f %-12f %-12f  %-12f  %-12f  ", 
                		compDistArg[0], compDistArg[1], strMetricStructReturnLocal.compDistPercent, strMetricStructReturnLocal.wcompDistPercent, strMetricStructReturnLocal.sseqWsPercent, 
                		strMetricStructReturnLocal.sstrWsPercent, strMetricStructReturnLocal.sqrtsseqWsPercent, strMetricStructReturnLocal.wsumsseqWsPercent, strMetricStructReturnLocal.matchRatio));
                //
                // Write results to output file
                stringsFileOutput.write(String.format("%-40s %-40s %-12f %-12f %-12f %-12f %-12f  %-12f  %-12f \n", 
                		compDistArg[0], compDistArg[1], strMetricStructReturnLocal.compDistPercent, strMetricStructReturnLocal.wcompDistPercent, strMetricStructReturnLocal.sseqWsPercent, 
                		strMetricStructReturnLocal.sstrWsPercent, strMetricStructReturnLocal.sqrtsseqWsPercent, strMetricStructReturnLocal.wsumsseqWsPercent, strMetricStructReturnLocal.matchRatio));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        //
        // CLose input and output files
        try{
            ifstream.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        
        try {
    		stringsFileOutput.close();
    	} catch (IOException e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    	}
    }
}

