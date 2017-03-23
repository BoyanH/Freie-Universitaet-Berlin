section .data
section .bss
section .text
global forLoop

forLoop:
	mov rax, 0 			;int sum = 0;
	cmp rdi, rsi 		; compare x with y
	jl end				;if (x<y) break;

loop: 
	add rax, rdi  		; sum = sum+x;
	dec rdi  			; x = x-1; / x--;
	cmp rdi, rsi
	jge loop 			; x>=y - loop again

end:
	ret