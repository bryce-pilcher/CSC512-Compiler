package org.bcpilche.token;

import java.io.PrintWriter;

/**
 * Created by Bryce on 9/26/2015.
 */
public class EOFToken implements Token{

    TokenType type = TokenType.EOF;
    String token = "";
    int state = 0;

    @Override
    public TokenType getTokenType() {
        return type;
    }

    @Override
    public void Print(PrintWriter outputFile) {
        outputFile.print("EOF");
    }

    @Override
    public boolean match(char c) {
        return false;
    }

    @Override
    public boolean match(String token) {
        return false;
    }

    @Override
    public String getToken() {
        return null;
    }
}
