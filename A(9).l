%{
#include <stdio.h>
%}

%%

"int" { printf("Keyword: int\n"); }
"="   { printf("Equal to\n"); }
";"   { printf("Semicolon\n"); }
"{"   { printf("Left Brace\n"); }
"}"   { printf("Right Brace\n"); }
"+"   { printf("Add\n"); }
"-"   { printf("Subtract\n"); }
"*"   { printf("Multiply\n"); }
"/"   { printf("Divide\n"); }
[0-9]+ { printf("Numeric\n"); }
[a-zA-Z_][a-zA-Z0-9_]* { printf("Identifier\n"); }
[ \t\n] ; /* ignore other characters */
.     { printf("Unknown: %s\n", yytext); };

%%

int yywrap() {
    return 1;
};


int main() {
  char input[] = "int m=10,n=2,o;\no=m-n;";
  yy_scan_string(input);
  yylex();
  return 0;
}