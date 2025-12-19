grammar ConfigLang;

options {
    language = Java;
}

file
    : (statement)* EOF
    ;

statement
    : constDecl
    | constExpr
    | dict
    ;

constDecl
    : VAR IDENT value
    ;

constExpr
    : CARET LPAREN expr RPAREN
    ;

expr
    : PLUS IDENT NUMBER               #addConstNumber
    | PLUS IDENT IDENT                #addConstConst
    | SQRT IDENT                      #sqrtConst
    ;

dict
    : LBRACK pairList? RBRACK
    ;

pairList
    : pair (COMMA pair)*
    ;

pair
    : IDENT ARROW value
    ;

value
    : NUMBER
    | dict
    ;

NUMBER
    : '0' [bB] [01]+
    ;

VAR     : 'var';
CARET   : '^';
PLUS    : '+';
SQRT    : 'sqrt';
ARROW   : '=>';
LBRACK  : '[';
RBRACK  : ']';
LPAREN  : '(';
RPAREN  : ')';
COMMA   : ',';

IDENT
    : [a-zA-Z] [_a-zA-Z0-9]*
    ;

LINE_COMMENT
    : '//' ~[\r\n]* -> skip
    ;

WS
    : [ \t\r\n]+ -> skip
    ;


