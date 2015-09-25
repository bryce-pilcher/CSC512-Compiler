package org.bcpilche.scanner;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

/**
 * This class will handle the scanning of tokens
 * in the input program file. 
 * 
 * @author Bryce Pilcher
 * @course CSC 512
 * @assignment Project 1
 *
 */

public class Scanner {

	File prog;
	BufferedInputStream program;
	LinkedList<Token> tokenDefs = new LinkedList<Token>();
	LinkedList<Token> pMatchingTokens = new LinkedList<Token>();
	LinkedList<Token> matchingTokens = new LinkedList<Token>();

	public Scanner(File program){
		try {
			this.program = new BufferedInputStream(new FileInputStream(program));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public boolean hasToken(){
		try {
			if(program.available() > 0){
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public Token getNextToken(){
		resetTokenDefs();
		resetMatchingTokens();
		for(Token t : tokenDefs){
			pMatchingTokens.add(t);
		}
		String token = "";
		try {
			int r;
			while((r = program.read()) != -1 && r != 13 && r != 10){
				char c = (char) r;
				token += c;
				if(token.equals(" ") || token.equals("\t")){
					return null;
				}
				for(Token t : pMatchingTokens){
					if(t.match(c)){
						if(!matchingTokens.contains(t)){
							matchingTokens.add(t);
						}
					}else{
						matchingTokens.remove(t);
					}
				}

				if(matchingTokens.size() > 0){
					pMatchingTokens = new LinkedList<Token>();
					for(Token t : matchingTokens){
						pMatchingTokens.add(t);
					}
					program.mark(0);
				}else{
					if(token.length() > 1){
					    token = token.substring(0, token.length() - 1);
                    }
					for(Token t : tokenDefs){
						if(t.match(token)){
							if(!matchingTokens.contains(t)){
								matchingTokens.add(t);
							}
						}else{
							matchingTokens.remove(t);
                            pMatchingTokens.remove(t);
						}
					}
                    if(matchingTokens.size() > 0 || pMatchingTokens.size() > 0) {
                        program.reset();
                    }
					break;
				}
			}
			if(matchingTokens.size() > 0){
				return matchingTokens.getFirst();
			}else if (pMatchingTokens.size() > 0){
				if(r != 13 && r != 10){
					program.reset();
					return pMatchingTokens.getFirst();
				}
			}else{
				System.out.println("Error matching on token " + token);
				System.exit(1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private void resetTokenDefs(){
		tokenDefs = new LinkedList<Token>();
		tokenDefs.add(new ReservedToken());
		tokenDefs.add(new MetaToken());
		tokenDefs.add(new SymbolToken());
		tokenDefs.add(new IdToken());
		tokenDefs.add(new NumberToken());
		tokenDefs.add(new StringToken());
	}

	private void resetMatchingTokens(){
		matchingTokens = new LinkedList<Token>();
		pMatchingTokens = new LinkedList<Token>();
	}

}
