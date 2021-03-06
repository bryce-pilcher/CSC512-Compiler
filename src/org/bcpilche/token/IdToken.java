package org.bcpilche.token;

import java.io.PrintWriter;

public class IdToken implements Token{
	
	TokenType type = TokenType.ID;
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
		switch (state) {
		case 0:
			if(Character.isAlphabetic(c)){
				state = 1;
			}else{
				state = -1;
			}
			break;
		
		case 1:
			if(Character.isAlphabetic(c) || Character.isDigit(c) || Character.isJavaIdentifierPart(c)){
				state = 1;
			}else{
				state = -1;
			}
			break;

		default:
			if(Character.isAlphabetic(c)){
				state = 1;
			}else{
				state = -1;
			}
			break;
		}
		if(state >=0){
			token += c;
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public boolean match(String token) {
		for(char c : token.toCharArray()){
			if(!match(c)){
				return false;
			}
		}
		this.token = token;
		return true;
	}

}
