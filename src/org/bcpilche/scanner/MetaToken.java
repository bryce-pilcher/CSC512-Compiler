package org.bcpilche.scanner;

import java.io.PrintWriter;

public class MetaToken implements Token{

	TokenType type = TokenType.META;
	String token = "";
	
	@Override
	public TokenType getTokenType() {
		return type;
	}
	
	@Override
	public void Print(PrintWriter outputFile) {
		outputFile.println(token);
	}
	
	@Override
	public String getToken() {
		return token;
	}
	
	public boolean match(char c){
		token += c;
		if(token.indexOf('#') == 0 || token.indexOf('/') == 0){
			return true;
		}
		else{
			return false;
		}
	}
	
	@Override
	public boolean match(String token) {
		if(token.indexOf('#') == 0 || token.indexOf('/') == 0){
			return true;
		}
		return false;
	}
}
