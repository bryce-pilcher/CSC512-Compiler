JFLAGS = -g -cp .:./classes -d ./classes
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        Token.java \
        IdToken.java \
        NumberToken.java \
        ReservedToken.java \
        StringToken.java \
        MetaToken.java \
        SymbolToken.java \
	EOFToken.java \
	Scanner.java \
	SymbolTable.java \
	Parser.java \
        Parse.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
