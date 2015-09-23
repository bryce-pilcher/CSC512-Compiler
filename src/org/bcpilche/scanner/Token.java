package org.bcpilche.scanner;

import java.io.PrintWriter;

public interface Token {

	public enum TokenType {
		ID,
		NUMBER,
		RESERVED,
		SYMBOL,
		STRING,
		META;
	}
	
	public TokenType getTokenType();
	public void Print(PrintWriter outputFile);
	public boolean match(char c);
	public boolean match(String token);
	public String getToken();
}
