README

Compile the parser by using the provided make file with the make command.

To run the program, enter:
java -cp ./classes org.bcpilche.parser.Parse <program to parse>

This top down parser was built using recursive function calls to methods that make up the non-terminals.  The counts of 
variables, functions, and statements are tracked with ints and stored in an int array to be passed back to the parse class.
The production rules for each non-terminal method are documented above the method itself.  


Grammar
Number of Distinct Non-Terminals: 48 (actually counted, but = count(‘-->’) - 2)
Number of Production Rules: 94 (actually counted, but also = count(‘|’) - 1 + count of (‘-->’) - 2) 

<program> --> <prog start> 
<func list> --> empty | <func> <func list’>
<func list’> empty | <func decl’> <after func decl> 
<func> --> <func decl> <after func decl>
<after func decl> --> semicolon | left_brace <data decls’> <statements> right_brace 
<func decl> --> left_parenthesis <parameter list> right_parenthesis
<func decl’> --> <type name> ID left_parenthesis <parameter list> right_parenthesis
<prog start> --> <type name> ID <data or func>
<data or func> --> <data decls> | <func list>
<type name> --> int | void | binary | decimal 
<parameter list> --> empty | void | <non-empty list> 
<parameter type> --> int | binary | decimal
<non-empty list> --> <parameter type> ID <non-empty list’>
<non-empty list’> --> comma <type name> ID <non-empty list’>  | empty
<data decls> --> empty | <id after ID> <id list’> semicolon <data decls’> 
<data decls’> --> empty | <type name> <id list> semicolon <data decls’>
<id list> --> <id> <id list’> 
<id list’> --> comma <id> <id list’> | empty
<id> --> ID <id after ID> 
<id after ID> --> left_bracket <expression> right_bracket | empty
<block statements> --> left_brace <statements> right_brace 
<statements> --> empty | <statement> <statements> 
<statement> --> ID <statement after id> | <if statement> | <while statement> | <return statement> | <break statement> | <continue statement> | read left_parenthesis  ID right_parenthesis semicolon | write left_parenthesis<expression> right_parenthesis semicolon | print left_parenthesis  STRING right_parenthesis semicolon 
<statement after id> --> <id after ID> <assignment> | <func call>
<assignment> --> equal_sign <expression> semicolon 
<func call> --> left_parenthesis <expr list> right_parenthesis semicolon
<expr list> --> empty | <non-empty expr list> 
<non-empty expr list> --> <expression> <after expression>
<non-empty expr list’> --> comma <expression> <non-empty expr list’> | empty
<after expression> --> <non-empty expr list’> | <comparison op> <expression> 
<if statement> --> if left_parenthesis <condition expression> right_parenthesis <block statements> 
<condition expression> -->  <condition> <after condition> 
<after condition> --> <condition op> <condition> | empty
<condition op> --> double_end_sign | double_or_sign 
<condition> --> <expression>  <after expression>
<comparison op> --> == | != | > | >= | < | <=
<while statement> --> while left_parenthesis <condition expression> right_parenthesis <block statements> 
<return statement> --> return <after return>
<after return> --> <expression> semicolon | semicolon
<break statement> --> break semicolon 
<continue statement> --> continue semicolon
<expression> --> <term> <expression’>
<expression’> --> <addop> <term> <expression’> | empty
<addop> --> plus_sign | minus_sign 
<term> --> <factor> <term’> 
<term’> --> <mulop> <factor> <term’> | empty
<mulop> --> star_sign | forward_slash 
<factor> --> ID <factor after ID> | NUMBER | minus_sign NUMBER | left_parenthesis <expression> right_parenthesis
<factor after ID> --> left_parenthesis <expr list> right_parenthesis | left_bracket <expression> right_bracket | empty
