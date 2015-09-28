package org.bcpilche.parser;

import org.bcpilche.scanner.Scanner;
import org.bcpilche.token.Token;

import java.io.File;
import java.io.PrintWriter;

/**
 * This class is the parser that contains the grammar for
 * parsing programs and reporting the number of variables,
 * functions, and statements.  
 * 
 * 
 * @author Bryce Pilcher
 * @course CSC 512
 * @assignment Project 1
 */
public class Parser {

	Scanner scanner;
	PrintWriter outputFile;
	Token token = null;
	//Hold count of variables
	int variables = 0;
	//Hold count of functions
	int functions = 0;
	//Hold count of statements
	int stmts = 0;

	//Constructor for parser
	public Parser(File program, PrintWriter outputfile){
		scanner = new Scanner(program);
		outputFile = outputfile;
	}

	//This method kicks off the program by calling
	//the root of the grammar.
	public int[] parse(){
		boolean pass = program();
		outputFile.close();
		int[] counts = {variables,functions,stmts};
		if(pass){
			return counts;
		}
		else{
			return null;
		}
	}

	//This method handles getting the next token in dealing 
	//with spaces and meta tokens.  It also handles writing 
	//the tokens out to file.  
	private Token nextToken(){
		if(token != null){
			outputFile.print(token.getToken());
			//System.out.print(token.getToken());
		}
		token = scanner.getNextToken();
		while((token == null || isMeta(token))){
			if(token == null){
				outputFile.print(" ");
			}else{
				outputFile.println(token.getToken());
			}

			token = scanner.getNextToken();
		}
		return token;
	}

	//<program> --> <data decls> <func list>
	private boolean program(){
		token = nextToken();
		return (prog_start() && token.getTokenType() == Token.TokenType.EOF);
	}

	//<func list> --> empty | <func> <func list>
	private boolean func_list(){
		if(func()){
			return func_list_prime();
		}
		return true;
	}

	//<func list prime> empty | <func decl prime> <after func decl> 
	private boolean func_list_prime(){
		if(func_decl_prime()){
			if(after_func_decl()){
				return func_list_prime();
			}
			return false;
		}
		return true;
	}

	//<func> --> <func decl> <after func decl>
	private boolean func(){
		if(func_decl()){
			return after_func_decl();
		}
		return false;
	}

	//<after func decl> --> semicolon | left_brace <data decls prime> <statements> right_brace
	private boolean after_func_decl(){
		if(isSymbol(token)) {
			if(token.getToken().equals(";")){
				token = nextToken();
				return true;
			}else if(token.getToken().equals("{")){
				token = nextToken();
				if(data_decls_prime()) {
					if(statements()) {
						if(isSymbol(token) && token.getToken().equals("}")){
							functions++;
							token = nextToken();
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	//<func decl> --> left_parenthesis <parameter list> right_parenthesis
	private boolean func_decl(){
		if(isSymbol(token) && token.getToken().equals("(")){
			token = nextToken();
			if(parameter_list()){
				if(isSymbol(token) && token.getToken().equals(")")){
					token = nextToken();
					return true;
				}
			}
		}
		return false;
	}

	//<func decl prime> --> <type name> ID left_parenthesis <parameter list> right_parenthesis
	private boolean func_decl_prime(){
		if(type_name()){
			if(isID(token)){
				token = scanner.getNextToken();
				if(isSymbol(token) && token.getToken().equals("(")){
					token = nextToken();
					if(parameter_list()){
						if(isSymbol(token) && token.getToken().equals(")")){
							token = nextToken();
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	//<prog start> --> <type name> ID <data or func>
	private boolean prog_start(){
		if(type_name()){
			if(isID(token)){
				token = nextToken();
				return data_or_func();
			}
		}
		return false;
	}

	//<data or func> --> <data decls> | <func list>
	private boolean data_or_func(){
		return data_decls() && func_list();
	}

	//<type name> --> int | void | binary | decimal
	private boolean type_name(){
		if(isReserved(token)){
			String type = token.getToken();
			if(type.equals("int") || type.equals("void") || type.equals("binary") || type.equals("decimal")){
				token = nextToken();
				return true;
			}
		}
		return false;
	}

	//<parameter list> --> empty | void | <non-empty list>
	private boolean parameter_list(){
		if(isReserved(token) && token.getToken().equals("void")){
			token = nextToken();
		}
		non_empty_list();
		return true;
	}

	//<parameter type> --> int | binary | decimal
	private boolean parameter_type(){
		String type = token.getToken();
		if(isReserved(token) && (type.equals("int") || type.equals("binary") || type.equals("decimal"))){
			token = nextToken();
			return true;
		}
		return false;
	}

	//<non-empty list> --> <type name> ID <non-empty list prime>
	private boolean non_empty_list(){
		if(parameter_type()){
			if(isID(token)){
				token = nextToken();
				return non_empty_list_prime();
			}
		}
		return false;
	}

	//<non-empty list prime> --> comma <type name> ID <non-empty list prime>  | empty
	private boolean non_empty_list_prime(){
		if(isSymbol(token) && token.getToken().equals(",")){
			token = nextToken();
			if(type_name()){
				if(isID(token)){
					token = nextToken();
					return non_empty_list_prime();
				}
				else{
					return false;
				}
			}
			else{
				return false;
			}
		}
		return true;
	}

	//<data decls> --> empty | <id after ID> <id list prime> semicolon <data decls prime>
	private boolean data_decls() {
		if (id_after_id()){
			if(id_list_prime()){
				if(isSymbol(token) && token.getToken().equals(";")) {
					token = nextToken();
					variables++;
					return data_decls_prime();
				}
			}
			else {
				return false;
			}
		}
		return true;
	}

	//<data decls prime> --> empty | <type name> <id list> semicolon <data decls>
	private boolean data_decls_prime() {
		if (type_name()){
			if(id_list()){
				if(isSymbol(token) && token.getToken().equals(";")) {
					token = nextToken();
					variables++;
					return data_decls_prime();
				}
			}
			else {
				return false;
			}
		}
		return true;
	}

	//<id list> --> <id> <id list prime>
	private boolean id_list(){
		if(id()){
			return id_list_prime();
		}
		return false;
	}

	//<id list prime> --> comma <id> <id list prime> | empty
	private boolean id_list_prime(){
		if(isSymbol(token) && token.getToken().equals(",")){
			token = nextToken();
			if(id()){
				variables++;
				return id_list_prime();
			}else{
				return false;
			}
		}
		return true;
	}

	//<id> --> ID <after ID>
	private boolean id(){
		if(isID(token)){
			token = nextToken();
			return id_after_id();
		}
		return false;
	}

	//<id after ID> --> left_bracket <expression> right_bracket | empty
	private boolean id_after_id(){
		if(isSymbol(token) && token.getToken().equals("[")){
			token = nextToken();
			if(expression()){
				if(isSymbol(token) && token.getToken().equals("]")) {
					token = nextToken();
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}
		return true;
	}

	//<block statements> --> left_brace <statements> right_brace
	private boolean block_statements(){
		if(isSymbol(token) && token.getToken().equals("{")){
			token = nextToken();
			if(statements()){
				if(isSymbol(token) && token.getToken().equals("}")) {
					token = nextToken();
					return true;
				}
			}
		}
		return false;
	}

	//<statements> --> empty | <statement> <statements>
	private boolean statements(){
		if(statement()){
			stmts++;
			return statements();
		}
		return true;
	}

	//<statement> --> ID <statement after id> | <if statement> | <while statement> | <return statement> | <break statement> | <continue statement> |
	// read left_parenthesis  ID right_parenthesis semicolon | write left_parenthesis<expression> right_parenthesis semicolon |
	// print left_parenthesis  STRING right_parenthesis semicolon
	private boolean statement(){
		if(isID(token)){
			token = nextToken();
			return statement_after_id();
		}else if(if_statement()){
			return true;
		}else if(while_statement()){
			return true;
		}else if(return_statement()) {
			return true;
		}else if(break_statement()){
			return true;
		}else if(continue_statement()){
			return true;
		}else if(isReserved(token)){
			if(token.getToken().equals("read")) {
				token = nextToken();
				if (isSymbol(token) && token.getToken().equals("(")) {
					token = nextToken();
					if (isID(token)) {
						token = nextToken();
						if (isSymbol(token) && token.getToken().equals(")")) {
							token = nextToken();
							if (isSymbol(token) && token.getToken().equals(";")) {
								token = nextToken();
								return true;
							}
						}
					}
				}
			}else if(token.getToken().equals("write")){
				token = nextToken();
				if (isSymbol(token) && token.getToken().equals("(")) {
					token = nextToken();
					if (expression()) {
						if (isSymbol(token) && token.getToken().equals(")")) {
							token = nextToken();
							if (isSymbol(token) && token.getToken().equals(";")) {
								token = nextToken();
								return true;
							}
						}
					}
				}
			}else if(token.getToken().equals("print")){
				token = nextToken();
				if (isSymbol(token) && token.getToken().equals("(")) {
					token = nextToken();
					if (isString(token)) {
						token = nextToken();
						if (isSymbol(token) && token.getToken().equals(")")) {
							token = nextToken();
							if (isSymbol(token) && token.getToken().equals(";")) {
								token = nextToken();
								return true;
							}
						}
					}
				}
			}
			return false;
		}
		return false;
	}

	//<statement after id> --> <id after ID> <assignment> | <func call>
	private boolean statement_after_id(){
		if(func_call()){
			return true;
		}
		id_after_id();
		return assignment();
	}

	//<assignment> --> equal_sign <expression> semicolon
	private boolean assignment(){
		if(isSymbol(token) && token.getToken().equals("=")){
			token = nextToken();
			if(expression()){
				if(isSymbol(token) && token.getToken().equals(";")){
					token = nextToken();
					return true;
				}
			}
		}
		return false;
	}
	//<func call> --> left_parenthesis <expr list> right_parenthesis semicolon
	private boolean func_call(){
		if(isSymbol(token) && token.getToken().equals("(")){
			token = nextToken();
			if(expr_list()){
				if(isSymbol(token) && token.getToken().equals(")")){
					token = nextToken();
					if(isSymbol(token) && token.getToken().equals(";")){
						token = nextToken();
						return true;
					}
				}
			}
		}
		return false;
	}

	//<expr list> --> empty | <non-empty expr list>
	private boolean expr_list(){
		non_empty_expr_list();
		return true;
	}

	//<non-empty expr list> --> <expression> <after expression>
	private boolean non_empty_expr_list(){
		if(expression()){
			return after_expression();
		}
		return false;
	}

	//<non-empty expr list prime> --> comma <expression> <non-empty expr list prime> | empty
	private boolean non_empty_expr_list_prime(){
		if(isSymbol(token) && token.getToken().equals(",")){
			token = nextToken();
			if(expression()){
				return non_empty_expr_list_prime();
			}
			return false;
		}
		return true;
	}
	//<after expression> --> <non-empty expr list prime> | <comparison op> <expression>
	private boolean after_expression(){
		if(comparison_op()){
			return expression();
		}
		return non_empty_expr_list_prime();
	}

	//<if statement> --> if left_parenthesis <condition expression> right_parenthesis <block statements>
	private boolean if_statement(){
		if(isReserved(token) && token.getToken().equals("if")){
			token = nextToken();
			if(isSymbol(token) && token.getToken().equals("(")){
				token = nextToken();
				if(condition_expression()){
					if(isSymbol(token) && token.getToken().equals(")")){
						token = nextToken();
						return block_statements();
					}
				}
			}
		}
		return false;
	}

	//<condition expression> -->  <condition> <after condition>
	private boolean condition_expression(){
		if(condition()){
			return after_condition();
		}
		return false;
	}

	//<after condition> --> <condition op> <condition> | empty
	private boolean after_condition(){
		if(condition_op()){
			return condition();
		}
		return true;
	}

	//<condition op> --> double_end_sign | double_or_sign
	private boolean condition_op(){
		if(isSymbol(token) && (token.getToken().equals("&&") || token.getToken().equals("||"))){
			token = nextToken();
			return true;
		}
		return false;
	}

	//<condition> --> <expression>  <after expression>
	private boolean condition(){
		if(expression()){
			return after_expression();
		}
		return false;
	}

	//<comparison op> --> == | != | > | >= | < | <=
	private boolean comparison_op(){
		if(isSymbol(token)){
			if(token.getToken().equals("==") || token.getToken().equals("!=") || token.getToken().equals(">") || token.getToken().equals(">=")
					|| token.getToken().equals("<") || token.getToken().equals("<=")){
				token = nextToken();
				return true;
			}
		}
		return false;
	}

	//<while statement> --> while left_parenthesis <condition expression> right_parenthesis <block statements>
	private boolean while_statement(){
		if(isReserved(token) && token.getToken().equals("while")){
			token = nextToken();
			if(isSymbol(token) && token.getToken().equals("(")){
				token = nextToken();
				if(condition_expression()){
					if(isSymbol(token) && token.getToken().equals(")")){
						token = nextToken();
						return block_statements();
					}
				}
			}
		}
		return false;
	}

	//<return statement> --> return <after return>
	private boolean return_statement(){
		if(isReserved(token) && token.getToken().equals("return")){
			token = nextToken();
			return after_return();
		}
		return false;
	}

	//<after return> --> <expression> semicolon | semicolon
	private boolean after_return(){
		if(expression()){
			if(isSymbol(token) && token.getToken().equals(";")){
				token = nextToken();
				return true;
			}
			return false;
		}else if(isSymbol(token) && token.getToken().equals(";")){
			token = nextToken();
			return true;
		}
		return false;
	}

	//<break statement> --> break semicolon
	private boolean break_statement(){
		if(isReserved(token) && token.getToken().equals("break")){
			token = nextToken();
			if(isSymbol(token) && token.getToken().equals(";")){
				token = nextToken();
				return true;
			}
		}
		return false;
	}

	//<continue statement> --> continue semicolon
	private boolean continue_statement(){
		if(isReserved(token) && token.getToken().equals("continue")){
			token = nextToken();
			if(isSymbol(token) && token.getToken().equals(";")){
				token = nextToken();
				return true;
			}
		}
		return false;
	}

	//<expression> --> <term> <expression prime>
	private boolean expression(){
		if(term()){
			return expression_prime();
		}
		return false;
	}

	//<expression prime> --> <addop> <term> <expression prime> | empty
	private boolean expression_prime(){
		if(addop()){
			if(term()){
				return expression_prime();
			}
			return false;
		}
		return true;
	}
	//<addop> --> plus_sign | minus_sign
	private boolean addop(){
		if(isSymbol(token) && (token.getToken().equals("+") || token.getToken().equals("-"))){
			token = nextToken();
			return true;
		}
		return false;
	}

	//<term> --> <factor> <term prime>
	private boolean term(){
		if(factor()){
			return term_prime();
		}
		return false;
	}

	//<term prime> --> <mulop> <factor> <term prime> | empty
	private boolean term_prime(){
		if(mulop()){
			if(factor()){
				return term_prime();
			}
			return false;
		}
		return true;
	}

	//<mulop> --> star_sign | forward_slash
	private boolean mulop(){
		if(isSymbol(token) && (token.getToken().equals("*") || token.getToken().equals("/"))){
			token = nextToken();
			return true;
		}
		return false;
	}

	//<factor> --> ID <after ID> | NUMBER | minus_sign NUMBER | left_parenthesis <expression> right_parenthesis
	private boolean factor(){
		if(isID(token)){
			token = nextToken();
			return factor_after_id();
		}else if(isNumber(token)){
			token = nextToken();
			return true;
		}else if(isSymbol(token) && token.getToken().equals("-")){
			token = nextToken();
			if(isNumber(token)){
				token = nextToken();
				return true;
			}
		}else if(isSymbol(token) && token.getToken().equals("(")){
			token = nextToken();
			if(expression()){
				if(isSymbol(token) && token.getToken().equals(")")){
					token = nextToken();
					return true;
				}
			}
		}
		return false;
	}
	//<factor after ID> --> left_parenthesis <expr list> right_parenthesis | left_bracket <expression> right_bracket | empty
	private boolean factor_after_id(){
		if(isSymbol(token) && token.getToken().equals("(")){
			token = nextToken();
			if(expr_list()){
				if(isSymbol(token) && token.getToken().equals(")")){
					token = nextToken();
					return true;
				}
			}
			return false;
		}else if(isSymbol(token) && token.getToken().equals("[")){
			token = nextToken();
			if(expression()){
				if(isSymbol(token) && token.getToken().equals("]")){
					token = nextToken();
					return true;
				}
			}
			return false;
		}
		return true;
	}

	/**
	 * Methods for determing if a token is of a certain type.
	 */
	private boolean isID(Token token){
		return (token != null && token.getTokenType() == Token.TokenType.ID);
	}

	private boolean isReserved(Token token){
		return (token != null && token.getTokenType() == Token.TokenType.RESERVED);
	}

	private boolean isSymbol(Token token){
		return (token != null && token.getTokenType() == Token.TokenType.SYMBOL);
	}

	private boolean isString(Token token){
		return (token != null && token.getTokenType() == Token.TokenType.STRING);
	}

	private boolean isNumber(Token token){
		return (token != null && token.getTokenType() == Token.TokenType.NUMBER);
	}

	private boolean isMeta(Token token){
		return (token != null && token.getTokenType() == Token.TokenType.META);
	}
}
