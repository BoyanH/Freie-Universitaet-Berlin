section .data
section .bss
section .text
global collatz

collatz:
	mov rsi, 2		;save 2 in a register, we are gonna use it later
	mov rcx, 0 		;i = 0
	cmp rdi, 1		;if k<=1 skip loop, condition not met
	jle end

loop:
	inc rcx			;i = i+1
	mov rax, rdi
	mov rdx, 0		;clear previous rests
	div rsi
	cmp rdx, 1
	je odd

even:
	mov rax, rdi
	div rsi			;k = k/2
	mov rdi, rax
	jmp endIfElse 	;skip odd label
odd:
	imul rdi, 3		;k = k*3
	inc rdi			;k = k+1

endIfElse:

	cmp rdi, 1 		;while k>0...
	jg loop

end:
	mov rax, rcx 
	ret 			;return i