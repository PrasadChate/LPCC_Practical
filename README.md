# LPCC_Practice
** (C)1. Write a program to generate Symbol table of a two-pass Assembler for the given Assembly language source code.**

*INPUT/CODE*

`    START 180
    READ M
    READ N
LOOP	 MOVER AREG, M  
	 MOVER BREG, N  
    COMP BREG, =’200’ 
	 BC GT, LOOP
BACK SUB AREG, M
            COMP AREG, =’500’
	 BC  LT, BACK 
    STOP
M	 DS	1
N	 DS	1
	 END
`


