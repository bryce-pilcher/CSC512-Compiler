package org.bcpilche.scanner;

import java.io.PrintWriter;

public class MetaToken implements Token{

	TokenType type = TokenType.META;
	String token = "";
	int state = 0;
	
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
		switch (state) {
		case 0:
			if(token.indexOf('#') == 0){
				state = 2;
			}else if(token.indexOf('/') == 0){
				state = 1;
			}else{
				state = -1;
			}
			break;
			
		case 1:
			if(token.lastIndexOf('/') == 1){
				state = 2;
			}else{
				state = -1;
			}
			break;
		
		case 2:
			state = 2;
			break;
		}
		
		if(state > 0){
			return true;
		}
		else{
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
