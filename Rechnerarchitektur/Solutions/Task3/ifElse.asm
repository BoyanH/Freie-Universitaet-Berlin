section .data
section .bss
section .text
global ifElse

ifElse: 			; a = rdi; b = rsi; x = rdx; y = rcx; z = r8
	cmp rdi, rsi
	je aEqualB 		;for code readability; it will go on anyways
	jne else		;else if A!=B, jump to else

aEqualB:
	inc rdx			;x=x+1
	mov rcx, r8	;y = z
	jmp end 		;jump to end to skip else statement

else:
	mov rdi, rsi	;a = b;

end:				;I add all registers together and return them to display result
	mov rax, rdi
	add rax, rsi
	add rax, rdx
	add rax, rcx
	add rax, r8
	ret