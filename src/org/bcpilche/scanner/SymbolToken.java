package org.bcpilche.scanner;

import java.io.PrintWriter;

public class SymbolToken implements Token{
	
	TokenType type = TokenType.SYMBOL;
	String token = "";
	int state = 0;
	
	@Override
	public TokenType getTokenType() {
		return type;
	}
	
	@Override
	public void Print(PrintWriter outputFile) {
		outputFile.print(token);
	}
	
	@Override
	public String getToken() {
		return token;
	}
	
	public boolean match(char c){
		String check = token + c;
		switch (state) {
		case 0:
			if(("(").equals(check) || (")").equals(check) || check.equals("{") || check.equals("}") || check.equals("[") || check.equals("]") || check.equals(",") || check.equals(";") || check.equals("+") || check.equals("-") || check.equals("*") || check.equals("/")){
				state = 1;
			}else if(check.equals("=") || check.equals(">") ||check.equals("<") || check.equals("!")){
				state = 2;
			}else if(check.equals("&") || check.equals("|")){
				state = 3;
			}else{
				state = -1;
			}
			break;
		case 1:
				state = -1;
				break;
		case 2:
			if(check.equals("==") ||   check.equals(">=") ||  check.equals("<=")){
				state = 1;
			}else{
				state = -1;
			}
			
		case 3:
			if(check.equals("&&") || check.equals("||")){
				state = 1;
			}else{
				state = -1;
			}
		default:
			break;
		}		
		if(state >= 0){
			token += c;
			return true;
		}
		else{
			return false;
		}
	}
	
	@Override
	public boolean match(String token) {
		if(token.equals("(") || token.equals(")") || token.equals("{") || token.equals("}") || token.equals("[") || token.equals("]") || token.equals(",") || token.equals(";") || token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")
				|| token.equals("=") || token.equals("==") || token.equals("!") || token.equals(">") || token.equals(">=") || token.equals("<") || token.equals("<=") || token.equals("&") || token.equals("&&") || token.equals("|") || token.equals("||")){			
			return true;
		}else{
			return false;
		}
	}
}
