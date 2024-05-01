%{
#include <stdio.h>
#include <string.h>

int total_value = 0;
int i_value = 0;
%}
%%

"int"    { printf("Keyword: int\n"); }
"printf" { printf("Function: printf\n"); }
"\""     { printf("Double quote\n"); }
"="      { printf("Assignment operator: =\n"); }
","      { printf("Comma\n"); }
";"      { printf("Semicolon\n"); }
[0-9]+   { printf("Numeric: %s\n", yytext); }
[a-zA-Z_][a-zA-Z0-9_]* { printf("Identifier: %s\n", yytext); }
"("      { printf("Left Parenthesis\n"); }
")"      { printf("Right Parenthesis\n"); }
"%"      { printf("Percentage\n"); }
\n       { /* Newline, do nothing */ }
[ \t]    { /* Whitespace, do nothing */ }
.        { printf("Unknown: %s\n", yytext); }

%%

int yywrap() {
    return 1;
}

int main() {
    char input[] = "{\nint total = 100;\nint i = 10;\nprintf(\"The value of total and i is : %d, %d\", total, i);\n}";
    yy_scan_string(input);
    yylex();
    return 0;
}