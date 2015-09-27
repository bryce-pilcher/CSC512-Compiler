package org.bcpilche.token;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;


public class ReservedToken implements Token{
	TokenType type = TokenType.RESERVED;
	String token = "";
	HashSet<String> reservedWords = new HashSet<String>(Arrays.asList("int","void","if ","while", "return", "read", "write", "print", "continue", "break", "binary", "decimal"));

	@Override
	public TokenType getTokenType() {
		return type;
	}

	@Override
	public void Print(PrintWriter outputFile) {
		outputFile.print(token + ' ');
	}

	@Override
	public String getToken() {
		return token;
	}

	public boolean match(char c){
		String check = token + c;
		if(Character.isLowerCase(c) || Character.isUpperCase(c)){
			token += c;
			return true;
		}else if(reservedWords.contains(check)){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean match(String token) {
		if(reservedWords.contains(token)){
			return true;
		}else{
			return false;
		}
	}
}
