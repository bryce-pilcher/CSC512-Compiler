package org.bcpilche.scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.bcpilche.scanner.Token.TokenType;


/**
 * This is the main entry to the scanner functionality
 * of a compiler.  This class will call the scanner class
 * which will scan for and return tokens.  
 * 
 * @author Bryce Pilcher
 * @course CSC 512
 * @assignment Project 1
 */
public class Scan {

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
		
		Scanner scanner = new Scanner(program);
		while(scanner.hasToken()){
			Token t = scanner.getNextToken();
			if(t != null){
				System.out.print(t.getTokenType());
				if(t.getTokenType() == TokenType.ID && !t.getToken().equals("main")){
					outputFile.print("cs512");
					System.out.print("cs512");
				}
				t.Print(outputFile);
				System.out.print(t.getToken());
			}
		}
		outputFile.close();

	}
}
