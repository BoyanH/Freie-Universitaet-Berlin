section .data
section .bss
section .text
global whileDo

whileDo:
	mov rax, 0 			;Set count of iterations to 0, this will be returned
	cmp rdi, rsi 		; compare x with y
	jle end				;if (x<=y) break;

loop: 
	inc rax  			; z = z+1; / z++;
	dec rdi  			; x = x-1; / x--;
	cmp rdi, rsi
	jg loop 			; while(x>y)

end:
	ret