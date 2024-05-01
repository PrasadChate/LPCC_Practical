%{
#include <stdio.h>
%}

%%

"int"    { printf("Keyword: int\n"); }
"float"  { printf("Keyword: float\n"); }
[a-zA-Z_][a-zA-Z0-9_]* { printf("Identifier: %s\n", yytext); }
[0-9]+   { printf("Numeric: %s\n", yytext); }
"="      { printf("Assignment operator: =\n"); }
"+"      { printf("Addition operator: +\n"); }
"*"      { printf("Multiplication operator: *\n"); }
"("      { printf("Left Parenthesis\n"); }
")"      { printf("Right Parenthesis\n"); }
";"      { printf("Semicolon\n"); }
\n       { /* Newline, do nothing */ }
[ \t]    { /* Whitespace, do nothing */ }
.        { printf("Unknown: %s\n", yytext); }

%%

int yywrap() {
    return 1; // Indicate end of input
}

int main() {
    char input[] = "{\nint a=3;\nint b=4;\nfloat c;\nc = (a*a + b*b) *2\n}";
    yy_scan_string(input); // Set input string
    yylex(); // Call lexer
    return 0;
}