%{
#include <stdio.h>
%}

%%

"the" | "a" | "an" {printf("Article: %s\n", yytext);}


"it" | "he" | "she" | "they" {printf("Pronoun: %s\n", yytext);}


"is" | "was" | "had" | "do" | "run" | "dread" |"arrives" {printf("Verb: %s\n", yytext);}


"to" | "in" | "on" | "with" {printf("Preposition: %s\n", yytext);}

"Destiny" {printf("Noun: %s\n", yytext);}

[\n\t]+   { /* Ignore everything else */ }

%%

int yywrap(){
	return 1;
}

int main() {
    char input[] = "Dread it. Run from it. Destiny arrives all the same.";
    yy_scan_string(input);
    yylex();
    return 0;
}