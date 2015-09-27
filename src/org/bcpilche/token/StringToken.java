package org.bcpilche.token;

import java.io.PrintWriter;


public class StringToken implements Token{
	TokenType type = TokenType.STRING;
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
			if(c == '"'){
				state = 1;
			}else{
				state = -1;
			}
			break;
		
		case 1:
			if(c == '"'){
				state = 2;
			}else{
				state = 1;
			}
			break;

		default:
			if(c == '"'){
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
		if(state != 2){
			return false;
		}
		this.token = token;
		return true;
	}
}
