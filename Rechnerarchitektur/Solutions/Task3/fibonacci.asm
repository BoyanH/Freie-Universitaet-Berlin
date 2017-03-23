section .data
section .bss
section .text
global getFibonacci

getFibonacci:
	mov rax, 0				;x = 0. Fibonacci number to 0
	mov rdx, 1				;y = 1. Fibonacci number to 1
	mov rsi, 0				;k = 0; this will be the sum of prev 2
	cmp rdi, 0
	je end 					;if 0. fibonacci is requested, return 0

getNextFibonacci:
	mov rsi, rax			;k = x
	add rsi, rdx			;k = k+y
	mov rax, rdx			;x = y
	mov rdx, rsi			;y = k
	dec rdi					;one loop less to calculate
	cmp rdi, 0
	jg 	getNextFibonacci 	;if fibonacci not yet calculate, call next loop

end:
	ret