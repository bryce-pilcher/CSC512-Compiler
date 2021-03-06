package org.bcpilche.parser;

import org.bcpilche.scanner.Scanner;
import org.bcpilche.token.Token;

import java.io.File;
import java.io.PrintWriter;
import java.util.Stack;

/**
 * This class is the parser that contains the grammar for
 * parsing programs and reporting the number of variables,
 * functions, and statements.  
 * 
 * 
 * @author Bryce Pilcher
 * @course CSC 512
 * @assignment Project 2
 */
public class Parser {

	Scanner scanner;
	PrintWriter outputFile;
	Token token = null;
	Stack<Token> tokenStack = new Stack<>();
	SymbolTable symbolTable = new SymbolTable();
	Stack<String> genStack = new Stack<>();
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
			//outputFile.print(token.getToken());
			//System.out.print(token.getToken());
		}
		token = scanner.getNextToken();
		while((token == null || isMeta(token))){
			if(token == null){
				//outputFile.print(" ");
			}else{
				//System.out.println(token.getToken());
				outputFile.println(token.getToken());
			}

			token = scanner.getNextToken();
		}
		//System.out.print(token.getToken());
		tokenStack.push(token);
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
                outputFile.print(";\n");
				token = nextToken();
				return true;
			}else if(token.getToken().equals("{")){
				outputFile.println(tokenStack.peek().getToken());
				token = nextToken();
				symbolTable = new SymbolTable(symbolTable);
				if(data_decls_prime()) {
					String param = "";
					//genStack.pop();
					String parameters = "";
					while(!genStack.empty() && (param = genStack.peek()).length() > 0){
						genStack.pop();
						parameters += getLocalVar(param) + " = " + param + ";\n";
					}
					if(statements()) {
						String st = "";
						while(!genStack.empty()){
							String pop = genStack.pop();
							if(!pop.equals("")){
								st = pop + "\n" + st;
							}
						}
						outputFile.println("int local[" + symbolTable.getLocalCount() + "];");
						outputFile.print(parameters);
						outputFile.print(st);
						if(isSymbol(token) && token.getToken().equals("}")){
							outputFile.println(tokenStack.peek().getToken());
							symbolTable = symbolTable.parent;
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
			String func = genStack.pop();
			outputFile.print(genStack.pop() + " " + func + tokenStack.peek().getToken());
			token = nextToken();
			if(parameter_list()){
				if(isSymbol(token) && token.getToken().equals(")")){
					outputFile.print(tokenStack.peek().getToken());
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
			genStack.pop();
			if(isID(token)){
				String funcName = tokenStack.pop().getToken();
				outputFile.print(tokenStack.pop().getToken() + " ");
				outputFile.print(funcName);
				token = nextToken();
				if(isSymbol(token) && token.getToken().equals("(")){
					outputFile.print(tokenStack.peek().getToken());
					token = nextToken();
					if(parameter_list()){
						if(isSymbol(token) && token.getToken().equals(")")){
							outputFile.print(tokenStack.peek().getToken());
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
				String funcName = tokenStack.pop().getToken();
				genStack.push(funcName);
				token = nextToken();
				return data_or_func();
			}
		}
		return false;
	}

	//<data or func> --> <data decls> | <func list>
	private boolean data_or_func(){
		if(data_decls()){
            if(symbolTable.getGlobalCount() > 0){
                outputFile.print("int global[" + symbolTable.getGlobalCount() + "];\n");
            }
            if(func_list()){
                return true;
            }
        }
        return false;
	}

	//<type name> --> int | void | binary | decimal
	private boolean type_name(){
		if(isReserved(token)){
			String type = token.getToken();
			if(type.equals("int") || type.equals("void") || type.equals("binary") || type.equals("decimal")){
				genStack.push(tokenStack.peek().getToken());
				token = nextToken();
				return true;
			}
		}
		return false;
	}

	//<parameter list> --> empty | void | <non-empty list>
	private boolean parameter_list(){
		String gen = "";
		if(isReserved(token) && token.getToken().equals("void")){
			gen += tokenStack.peek().getToken();
			token = nextToken();
		}
		if(non_empty_list()){
			gen += genStack.pop();
		}
		outputFile.print(gen);
		return true;
	}

	//<parameter type> --> int | binary | decimal
	private boolean parameter_type(){
		String gen = "";
		String type = token.getToken();
		if(isReserved(token) && (type.equals("int") || type.equals("binary") || type.equals("decimal"))){
			gen += tokenStack.peek().getToken() + " ";
			token = nextToken();
			genStack.push(gen);
			return true;
		}
		return false;
	}

	//<non-empty list> --> <type name> ID <non-empty list prime>
	private boolean non_empty_list(){
		String gen = "";
		if(parameter_type()){
			gen += genStack.pop();
			if(isID(token)){
				gen += tokenStack.peek().getToken();
				genStack.push(tokenStack.peek().getToken());
				token = nextToken();
				if(non_empty_list_prime()){
					gen += genStack.pop();
					genStack.push(gen);
					return true;
				}
			}
		}
		return false;
	}

	//<non-empty list prime> --> comma <parameter type> ID <non-empty list prime>  | empty
	private boolean non_empty_list_prime(){
		String gen = "";
		if(isSymbol(token) && token.getToken().equals(",")){
			gen += ", ";
			token = nextToken();
			if(parameter_type()){
				gen += genStack.pop();
				if(isID(token)){
					gen += tokenStack.peek().getToken();
                    genStack.push(tokenStack.peek().getToken());
					token = nextToken();
					if(non_empty_list_prime()){
						gen += genStack.pop();
						genStack.push(gen);
						return true;
					}
					else{
						return false;
					}
				}
				else{
					return false;
				}
			}
			else{
				return false;
			}
		}
		genStack.push("");
		return true;
	}

	//<data decls> --> empty | <id after ID> <id list prime> semicolon <data decls prime>
	private boolean data_decls() {
		if (id_after_id()){
			genStack.pop();
			if(id_list_prime()){
				if(isSymbol(token) && token.getToken().equals(";")) {
					getLocalVar(genStack.pop());
					genStack.clear();
					//System.out.println(";");
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
					getLocalVar(genStack.pop());
					genStack.pop();				
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
			getLocalVar(genStack.pop());
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
			genStack.push(token.getToken());
			token = nextToken();
			if(id_after_id()){
				genStack.pop();
				return true;
			}
		}
		return false;
	}

	//<id after ID> --> left_bracket <expression> right_bracket | empty
	private boolean id_after_id(){
		String gen = "";
        String[] array = null;
        if(isSymbol(token) && token.getToken().equals("[")){
            String var = genStack.pop();
            boolean init = !genStack.empty() && genStack.peek().equals("int");
			token = nextToken();
			if(expression()){
                String value = genStack.pop();
                if(isNumeric(value)) {
                    int val = Integer.parseInt(value);
                    if (init) {
                        array = getArrayVar(var);
                        gen =  array[0] + "[";
                        if (symbolTable.parent == null) {
                            for (int i = 0; i < val; i++) {
                                symbolTable.incGlobalCount();
                            }
                        } else {
                            for (int i = 0; i < val; i++) {
                                symbolTable.incLocalCount();
                            }
                        }
                    } else {
                        array = var.split(" ");
                        var = "";
                        int base = Integer.parseInt(array[1]);
                        gen += array[0] + "[" + Integer.toString((base + val));
                    }
                } else {
                    array = var.split(" ");
                    gen = array[0] + "[";
                    if(Integer.parseInt(array[1]) > 0){
                        String loc = "local[" + symbolTable.getLocalCount() + "]";
                        symbolTable.incLocalCount();
                        String eq = loc + " = " + value + " + " + array[1] + ";";
                        genStack.push(eq);
                        gen += loc;
                    }else {
                        gen += value;
                    }
                    var = "";
                }
				if(isSymbol(token) && token.getToken().equals("]")) {
                    gen += "]";
                    genStack.push(var);
                    genStack.push(gen);
					token = nextToken();
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}
		genStack.push("");
		return true;
	}

	//<block statements> --> left_brace <statements> right_brace
	private boolean block_statements(){
		String gen = "";
		if(isSymbol(token) && token.getToken().equals("{")){
			//gen += "{";
			token = nextToken();
			if(statements()){
				gen += genStack.pop();
				if(isSymbol(token) && token.getToken().equals("}")) {
					//gen += "}";
					genStack.push(gen);
					token = nextToken();
					return true;
				}
			}
		}
		return false;
	}

	//<statements> --> empty | <statement> <statements>
	private boolean statements(){
		String gen = "";
		if(statement()){
			//gen += genStack.pop();
			stmts++;
			if(statements()){
				//gen += genStack.pop();
				//genStack.push(gen);
				return true;
			}
			return false;
		}
		//genStack.push("");
		return true;
	}

	//<statement> --> ID <statement after id> | <if statement> | <while statement> | <return statement> | <break statement> | <continue statement> |
	// read left_parenthesis  ID right_parenthesis semicolon | write left_parenthesis<expression> right_parenthesis semicolon |
	// print left_parenthesis  STRING right_parenthesis semicolon
	private boolean statement(){
		String gen = "";
		if(isID(token)){
			//System.out.print(getLocalVar(tokenStack.peek().getToken()) + " " + tokenStack.peek().getToken());
			genStack.push(tokenStack.peek().getToken());
			token = nextToken();
			if(statement_after_id()){
				String af_id = genStack.pop();
				String id = genStack.pop();
				gen += id + af_id;
				genStack.push(gen);
				return true;
			}
			return false;
		}else if(if_statement()){
			return true;
		}else if(while_statement()){
			return true;
		}else if(return_statement()){
			return true;
		}else if(break_statement()){
			return true;
		}else if(continue_statement()){
			return true;
		}else if(isReserved(token)){
			if(token.getToken().equals("read")) {
				gen += tokenStack.peek().getToken();
				token = nextToken();
				if (isSymbol(token) && token.getToken().equals("(")) {
					gen += tokenStack.peek().getToken();
					token = nextToken();
					if (isID(token)) {
						gen += getLocalVar(tokenStack.peek().getToken());
						token = nextToken();
						if (isSymbol(token) && token.getToken().equals(")")) {
							gen += tokenStack.peek().getToken();
							token = nextToken();
							if (isSymbol(token) && token.getToken().equals(";")) {
								gen += tokenStack.peek().getToken();
								token = nextToken();
								genStack.push(gen);
								return true;
							}
						}
					}
				}
			}else if(token.getToken().equals("write")){
				gen += tokenStack.peek().getToken();
				token = nextToken();
				if (isSymbol(token) && token.getToken().equals("(")) {
					gen += tokenStack.peek().getToken();
					token = nextToken();
					if (expression()) {
						gen += genStack.pop();
						if (isSymbol(token) && token.getToken().equals(")")) {
							gen += tokenStack.peek().getToken();
							token = nextToken();
							if (isSymbol(token) && token.getToken().equals(";")) {
								gen += tokenStack.peek().getToken();
								token = nextToken();
								genStack.push(gen);
								return true;
							}
						}
					}
				}
			}else if(token.getToken().equals("print")){
				gen += tokenStack.peek().getToken();
				token = nextToken();
				if (isSymbol(token) && token.getToken().equals("(")) {
					gen += tokenStack.peek().getToken();
					token = nextToken();
					if (isString(token)) {
						gen += tokenStack.peek().getToken();
						token = nextToken();
						if (isSymbol(token) && token.getToken().equals(")")) {
							gen += tokenStack.peek().getToken();
							token = nextToken();
							if (isSymbol(token) && token.getToken().equals(";")) {
								gen += tokenStack.peek().getToken();
								genStack.push(gen);
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
		String gen = "";
		String id = genStack.pop();
		if(func_call()){
			String af_id = genStack.pop();
			genStack.push(id);
			gen += af_id;
			genStack.push(gen);
			return true;
		}
		genStack.push(getLocalVar(id));
		id_after_id();
		gen += genStack.pop();
		id = genStack.pop();
		if(assignment()){
			gen += genStack.pop();
			genStack.push(id);
			genStack.push(gen);
			return true;
		};
		return false;
	}

	//<assignment> --> equal_sign <expression> semicolon
	private boolean assignment(){
		String gen = "";
		if(isSymbol(token) && token.getToken().equals("=")){
			//System.out.print(" " + tokenStack.peek().getToken());
			gen += " " + tokenStack.peek().getToken() + " ";
			token = nextToken();
			if(expression()){
				gen += genStack.pop();
				if(isSymbol(token) && token.getToken().equals(";")){
					gen += ";";
					token = nextToken();
					genStack.push(gen);
					return true;
				}
			}
		}
		return false;
	}
	//<func call> --> left_parenthesis <expr list> right_parenthesis semicolon
	private boolean func_call(){
		String gen = "";
		if(isSymbol(token) && token.getToken().equals("(")){
			gen += tokenStack.peek().getToken();
			token = nextToken();
			if(expr_list()){
				gen += genStack.pop();
				if(isSymbol(token) && token.getToken().equals(")")){
					gen += tokenStack.peek().getToken();
					token = nextToken();
					if(isSymbol(token) && token.getToken().equals(";")){
						gen += tokenStack.peek().getToken();
						token = nextToken();
						genStack.push(gen);
						return true;
					}
				}
			}
		}
		return false;
	}

	//<expr list> --> empty | <non-empty expr list>
	private boolean expr_list(){
		if(!non_empty_expr_list()){
			genStack.push("");
		}
		return true;
	}

	//<non-empty expr list> --> <expression> <after expression>
	private boolean non_empty_expr_list(){
		String gen = "";
		if(expression()){
			gen += genStack.pop();
			if(after_expression()){
				gen += genStack.pop();
				genStack.push(gen);
				return true;
			}
		}
		return false;
	}

	//<non-empty expr list prime> --> comma <expression> <non-empty expr list prime> | empty
	private boolean non_empty_expr_list_prime(){
		String gen = "";
		if(isSymbol(token) && token.getToken().equals(",")){
			gen += ",";
			token = nextToken();
			if(expression()){
				gen += genStack.pop();
				if(non_empty_expr_list_prime()){
					gen += genStack.pop();
					genStack.push(gen);
					return true;
				}
			}
			return false;
		}
		genStack.push("");
		return true;
	}
	//<after expression> --> <non-empty expr list prime> | <comparison op> <expression>
	private boolean after_expression(){
		String gen = "";
		if(comparison_op()){
			gen += genStack.pop();
			if(expression()){
				gen += genStack.pop();
				genStack.push(gen);
				return true;
			}
		}
		else if(non_empty_expr_list_prime()){
			gen += genStack.pop();
			genStack.push(gen);
			return true;
		}
		return false;
	}

	//<if statement> --> if left_parenthesis <condition expression> right_parenthesis <block statements>
	private boolean if_statement(){
		String gen = "";
		if(isReserved(token) && token.getToken().equals("if")){
			gen += tokenStack.peek().getToken();
			token = nextToken();
			if(isSymbol(token) && token.getToken().equals("(")){
				gen += tokenStack.peek().getToken();
				token = nextToken();
				if(condition_expression()){
					gen += genStack.pop();
					if(isSymbol(token) && token.getToken().equals(")")){
						gen += tokenStack.peek().getToken();
						gen += " goto c" + symbolTable.getLabelCount() + ";\n";
						symbolTable.incLabelCount();
						String el_label = "c" + symbolTable.getLabelCount();
						gen += "goto " + el_label + ";\n";
						symbolTable.incLabelCount();
						gen += "c" + (symbolTable.getLabelCount() - 2) + ":;";
						genStack.push(gen);
						token = nextToken();
						if(block_statements()){
							gen = el_label + ":;";
							genStack.push(gen);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	//<condition expression> -->  <condition> <after condition>
	private boolean condition_expression(){
		String gen = "";
		if(condition()){
			gen += genStack.pop();
			if(after_condition()){
				if(genStack.peek().length() > 0){
					String loc1 = "local[" + symbolTable.getLocalCount() + "]";
					symbolTable.incLocalCount();
					String cond2 = genStack.pop();
					String comp_op = genStack.pop();
					genStack.push(loc1 + " = " + gen + ";");
					String loc2 = "local[" + symbolTable.getLocalCount() + "]";
					symbolTable.incLocalCount();
					genStack.push(loc2 + " = " + cond2 + ";");
					genStack.push(loc1 + comp_op + loc2);
				}
				else{
					genStack.push(gen);
				}
				return true;
			}
		}
		return false;
	}

	//<after condition> --> <condition op> <condition> | empty
	private boolean after_condition(){
		if(condition_op()){
			if(condition()){
				return true;
			}
		}
		genStack.push("");
		return true;
	}

	//<condition op> --> double_end_sign | double_or_sign
	private boolean condition_op(){
		if(isSymbol(token) && (token.getToken().equals("&&") || token.getToken().equals("||"))){
			genStack.push(" " + tokenStack.peek().getToken() + " ");
			token = nextToken();
			return true;
		}
		return false;
	}

	//<condition> --> <expression>  <after expression>
	private boolean condition(){
		String gen = "";
		if(expression()){
			gen += genStack.pop();
			if(after_expression()){
				gen += genStack.pop();
				genStack.push(gen);
				return true;
			}
		}
		return false;
	}

	//<comparison op> --> == | != | > | >= | < | <=
	private boolean comparison_op(){
		if(isSymbol(token)){
			if(token.getToken().equals("==") || token.getToken().equals("!=") || token.getToken().equals(">") || token.getToken().equals(">=")
					|| token.getToken().equals("<") || token.getToken().equals("<=")){
				genStack.push(tokenStack.peek().getToken());
				token = nextToken();
				return true;
			}
		}
		return false;
	}

	//<while statement> --> while left_parenthesis <condition expression> right_parenthesis <block statements>
	private boolean while_statement(){
		String gen = "";
		if(isReserved(token) && token.getToken().equals("while")){
			int lCount = symbolTable.getLabelCount();
			String label = "c" + lCount + "";
			symbolTable.incLabelCount();
			gen += label + ":;\nif";
			token = nextToken();
			if(isSymbol(token) && token.getToken().equals("(")){
				gen += tokenStack.peek().getToken();
				token = nextToken();
				if(condition_expression()){
					gen += genStack.pop();
					if(isSymbol(token) && token.getToken().equals(")")){
						gen += tokenStack.peek().getToken();
						gen += " goto c" + symbolTable.getLabelCount() + ";\n";
						symbolTable.incLabelCount();
						gen += "goto c" + symbolTable.getLabelCount() + ";\n";
						symbolTable.incLabelCount();
						gen += "c" + (symbolTable.getLabelCount() - 2) + ":;";
						genStack.push(gen);
						token = nextToken();
						genStack.push("scope");
						if(block_statements()){
							replaceBreakAndContinue(lCount);
							gen = "goto " + label + ";\n";
							gen += "c" + (lCount + 2) + ":;";
							genStack.push(gen);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	//<return statement> --> return <after return>
	private boolean return_statement(){
		String gen = "";
		if(isReserved(token) && token.getToken().equals("return")){
			gen += tokenStack.peek().getToken();
			token = nextToken();
			if(after_return()){
				gen +=  genStack.pop();
				genStack.push(gen);
				return true;
			}
		}
		return false;
	}

	//<after return> --> <expression> semicolon | semicolon
	private boolean after_return(){
		String gen = "";
		if(expression()){
			gen += genStack.pop();
			if(isNumeric(gen)){
				String loc = "local[" + symbolTable.getLocalCount() + "]";
                symbolTable.incLocalCount();
				//String loc = genStack.pop();
				genStack.push(loc + " = " + gen + ";");
				gen = " " + loc;
				//genStack.push(gen);
			}
			if(isSymbol(token) && token.getToken().equals(";")){
				gen += ";";
				genStack.push(" " + gen);
				token = nextToken();
				return true;
			}
			return false;
		}else if(isSymbol(token) && token.getToken().equals(";")){
			genStack.push(tokenStack.peek().getToken());
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
				genStack.push("break;");
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
				genStack.push("continue;");
				token = nextToken();
				return true;
			}
		}
		return false;
	}

	//<expression> --> <term> <expression prime>
	private boolean expression(){
		String gen = "";
		if(term()){
			gen += genStack.pop();
			if(expression_prime()){
				String e_prime = genStack.pop();
				if(e_prime.length() > 0){
					gen += e_prime + ";";
                    String loc = genStack.pop();
                    String reorder = genStack.pop();
                    genStack.push(loc + " = " + gen);
                    genStack.push(reorder);
                    return true;
				}
				genStack.push(gen);
				return true;
			}
		}
		return false;
	}

	//<expression prime> --> <addop> <term> <expression prime> | empty
	private boolean expression_prime(){
        String loc = "local[" + symbolTable.getLocalCount() + "]";
        String lastLoc = loc;
		String gen = "";
        if(addop()){
            symbolTable.incLocalCount();
			gen += genStack.pop();
			if(term()){
				gen += genStack.pop();
				if(expression_prime()){
					String e_prime = genStack.pop();
					if(e_prime.length() > 0){
                        gen += ";\n" + genStack.pop() + " = " + loc + e_prime;
                        lastLoc = genStack.pop();
					}
                    genStack.push(lastLoc);
                    genStack.push(loc);
					genStack.push(gen);
					return true;
				}
			}
			return false;
		}
		genStack.push("");
		return true;
	}
	//<addop> --> plus_sign | minus_sign
	private boolean addop(){
		if(isSymbol(token) && (token.getToken().equals("+") || token.getToken().equals("-"))){
			genStack.push(" " + tokenStack.peek().getToken() + " ");
			token = nextToken();
			return true;
		}
		return false;
	}

	//<term> --> <factor> <term prime>
	private boolean term(){
        //String loc = "local[" + symbolTable.getLocalCount() + "]";
		String gen = "";
		if(factor()){
			gen += genStack.pop();
			if(term_prime()){
				String t_prime = genStack.pop();
				if(t_prime.length() > 0){
                    gen += t_prime + ";";
                    String loc = genStack.pop();
                    String reorder = genStack.pop();
                    genStack.push(loc + " = " + gen);
                    genStack.push(reorder);
                    return true;
                }
                genStack.push(gen);
				return true;
			}
		}
		return false;
	}

	//<term prime> --> <mulop> <factor> <term prime> | empty
	private boolean term_prime(){
        String loc = "local[" + symbolTable.getLocalCount() + "]";
        String lastLoc = loc;
		String gen = "";
		if(mulop()){
            symbolTable.incLocalCount();
			gen += genStack.pop();
			if(factor()){
				gen += genStack.pop();
				if(term_prime()){
					String t_prime = genStack.pop();
					if(t_prime.length() > 0){
                        gen += ";\n" + genStack.pop() + " = " + loc +  t_prime;
                        lastLoc = genStack.pop();
					}
                    genStack.push(lastLoc);
                    genStack.push(loc);
                    genStack.push(gen);
					return true;
				}
			}
			return false;
		}
		genStack.push("");
		return true;
	}

	//<mulop> --> star_sign | forward_slash
	private boolean mulop(){
		if(isSymbol(token) && (token.getToken().equals("*") || token.getToken().equals("/"))){
			genStack.push(" " + tokenStack.peek().getToken() + " ");
			token = nextToken();
			return true;
		}
		return false;
	}

	//<factor> --> ID <after ID> | NUMBER | minus_sign NUMBER | left_parenthesis <expression> right_parenthesis
	private boolean factor(){
		if(isID(token)){
			String gen = "";
			String id = tokenStack.peek().getToken();
			token = nextToken();
			genStack.push(id);
			if(factor_after_id()){
				String fac_af_id = genStack.pop();
				gen += fac_af_id;
				genStack.push(gen);
				return true;
			}
		}else if(isNumber(token)){
			String gen = tokenStack.peek().getToken();
			genStack.push(gen);
			token = nextToken();
			return true;
		}else if(isSymbol(token) && token.getToken().equals("-")){
			String gen = " " + tokenStack.peek().getToken();
			token = nextToken();
			if(isNumber(token)){
				gen += " " + tokenStack.peek().getToken();
				genStack.push(gen);
				token = nextToken();
				return true;
			}
		}else if(isSymbol(token) && token.getToken().equals("(")){
			String gen = "";
			token = nextToken();
			if(expression()){
				gen += genStack.pop();
				if(isSymbol(token) && token.getToken().equals(")")){
					token = nextToken();
					genStack.push(gen);
					return true;
				}
			}
		}
		return false;
	}
	//<factor after ID> --> left_parenthesis <expr list> right_parenthesis | left_bracket <expression> right_bracket | empty
	private boolean factor_after_id(){
		String gen = "";
		String id = genStack.pop();
		if(isSymbol(token) && token.getToken().equals("(")){
			String loc = "local[" + symbolTable.getLocalCount() + "]";
			symbolTable.incLocalCount();
			gen = loc + " = " + id + "(";
			token = nextToken();
			if(expr_list()){
				gen += genStack.pop();
				if(isSymbol(token) && token.getToken().equals(")")){
					gen += ");";
					token = nextToken();
					genStack.push(gen);
					genStack.push(loc);
					return true;
				}
			}
			return false;
		}else if(isSymbol(token) && token.getToken().equals("[")){
            String[] array = getArrayVar(id);
            String var = array[0];
            boolean init = genStack.peek().equals("int");
            token = nextToken();
            if(expression()){
                String value = genStack.pop();
                if(isNumeric(value)) {
                    int val = Integer.parseInt(value);
                    if (init) {
                        array = getArrayVar(var);
                        gen =  array[0] + "[";
                        if (symbolTable.parent == null) {
                            for (int i = 0; i < val; i++) {
                                symbolTable.incGlobalCount();
                            }
                        } else {
                            for (int i = 0; i < val; i++) {
                                symbolTable.incLocalCount();
                            }
                        }
                    } else {
                        array = var.split(" ");
                        var = "";
                        int base = Integer.parseInt(array[1]);
                        gen += array[0] + "[" + Integer.toString((base + val));
                    }
                } else {
                    array = var.split(" ");
                    gen = array[0] + "[";
                    if(Integer.parseInt(array[1]) > 0){
                        String loc = "local[" + symbolTable.getLocalCount() + "]";
                        symbolTable.incLocalCount();
                        String eq = loc + " = " + value + " + " + array[1] + ";";
                        genStack.push(eq);
                        gen += loc;
                    }else {
                        gen += value;
                    }
                    var = "";
                }
                if(isSymbol(token) && token.getToken().equals("]")) {
                    gen += "]";
                    genStack.push(var);
                    genStack.push(gen);
                    token = nextToken();
                    return true;
				}
			}
			return false;
		}
		genStack.push(getLocalVar(id));
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

	private String getLocalVar(String var){
		SymbolTable st = symbolTable;
		String gen = st.symbols.get(var);
		while(gen == null){
			st = st.getParent();
			if(st == null){
				break;
			}
			gen = st.symbols.get(var);
		}

		if(gen == null){
            if(symbolTable.parent == null){
                gen = "global[" + symbolTable.getGlobalCount() + "]";
                symbolTable.symbols.put(var,gen);
                symbolTable.incGlobalCount();
            }else {
                gen = "local[" + symbolTable.getLocalCount() + "]";
                symbolTable.symbols.put(var, gen);
                symbolTable.incLocalCount();
            }
		}
		return gen;
	}

    private String[] getArrayVar(String var){
        String[] array = new String[2];
        SymbolTable st = symbolTable;
        String gen = st.symbols.get(var);
        String val = "";
        while(gen == null){
            st = st.getParent();
            if(st == null){
                break;
            }
            gen = st.symbols.get(var);
        }

        if(gen == null){
            if(symbolTable.parent == null){
                val = Integer.toString(symbolTable.getGlobalCount());
                gen = "global";
                symbolTable.symbols.put(var, gen + " " + val);
            }else {
                val = Integer.toString(symbolTable.getLocalCount());
                gen = "local";
                symbolTable.symbols.put(var, gen + " " + val);
            }
        }else{
            array = gen.split(" ");
        }
        array[0] = gen;
        array[1] = val;
        return array;
    }

	private boolean isNumeric(String str){
		try{
			double d = Double.parseDouble(str);
		}catch(NumberFormatException e){
			return false;
		}
		return true;
	}
	
	private void replaceBreakAndContinue(int lCount){
		Stack<String> holder = new Stack<>();
		String gen = "";
		while(!(gen = genStack.pop()).equals("scope")){
			if(gen.equals("break;")){
				holder.push("goto c" + (lCount + 2) + ";");
			}else if(gen.equals("continue;")){
				holder.push("goto c" + (lCount) + ";");
			}else{
				holder.push(gen);
			}
		}
		
		while(!holder.empty()){
			genStack.push(holder.pop());
		}
	}
}
