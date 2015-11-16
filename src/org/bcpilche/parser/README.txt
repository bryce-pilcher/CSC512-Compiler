README for Project 3

Code Generation functionality added to parser.

Compile the parser by using the provided make file with the make command.

To run the program, enter:
java -cp ./classes org.bcpilche.parser.Parse <program to parse>

This top down parser was built using recursive function calls to methods that make up the non-terminals.  The counts of
variables, functions, and statements are tracked with ints and stored in an int array to be passed back to the parse class.
The production rules for each non-terminal method are documented above the method itself.

The code generation was added to the parser in a stack implementation.  Most function calls result in something being
pushed on the stack and these items are popped when exiting the after_func_decl function and written to an output file
because this is the end of a large unit of code.

**BUG FIX**
The bug from project 2 was discovered and fixed by removing a space after 'if' in the list of reserved keywords.  I suspect
this was a bug in project 1 as well.  This caused if statements to be treated like a function, which appeard ok as long as
the 'if's weren't nested.  When an 'if' was nested in another 'if' or 'while', it failed because you could not define a function
in an if statement according to the rules of the grammar.