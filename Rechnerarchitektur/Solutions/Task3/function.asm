section .data
section .bss
section .text
global function

function:			;x = rdi; y = rsi;
	imul rdi, 2 	;x = x*2
	imul rsi, 30	;y = y*30
	mov rax, 7		;result = 7
	add rax, rdi	;result = result + x*2
	add rax, rsi	;result = result + y*30
	ret