package org.bcpilche.parser;

import org.bcpilche.scanner.Scanner;
import org.bcpilche.token.Token;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by Bryce on 9/26/2015.
 */
public class Parse {

    public static void main(String[] args){
        /**
         * Make sure this program is passed the name
         * of a file in the arguments, otherwise print
         * usage
         */
        if(args.length != 1){
            System.out.println("Usage: scan <program>");
            System.exit(0);
        }

        String programName = args[0];
        File program = new File(programName);

        if(!program.exists() && !program.canRead()){
            System.out.println("The program " + programName + " does not exist or cannot be read.");
            System.exit(1);
        }

        int lastDot = programName.lastIndexOf('.');
        String outputFileName = programName.substring(0,lastDot) + "_gen" + programName.substring(lastDot);
        PrintWriter outputFile = null;

        try {
            outputFile = new PrintWriter(outputFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if(outputFile == null){
            System.out.println("Something happened in initializing the outputfile writer.");
            System.exit(1);
        }

        Parser parser = new Parser(program, outputFile);
        int[] counts = parser.parse();

        System.out.print("variable " + counts[0] + " function " + counts[1] + " statements " + counts[2]);

        /*Scanner scanner = new Scanner(program);
        while(scanner.hasToken()){
            Token t = scanner.getNextToken();
            if(t != null){
                System.out.print(t.getTokenType());
                if(t.getTokenType() == Token.TokenType.ID && !t.getToken().equals("main")){
                    outputFile.print("cs512");
                    System.out.print("cs512");
                }
                t.Print(outputFile);
                System.out.print(t.getToken());
            }
        }
        outputFile.close();*/

    }
}
