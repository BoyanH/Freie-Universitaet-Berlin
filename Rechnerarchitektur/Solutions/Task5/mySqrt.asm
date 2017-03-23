section .text
global mySqrt

mySqrt:
	mov rsi, rdi 		;a = value
	mov r10, rdi		;saveValue = value
	mov rcx, 0			;b = 0
	mov rbx, 2			;save 2, required for devision

loopCheck: 				; |a-b| = a-b where a is the bigger value
	mov r8, rsi			;a' = a
	mov r9, rcx			;b' = b
	cmp r8, r9			;compare a and b
	jge firstBigger

	mov rbp, r8		;temp = a
	mov r8, r9		;a = b
	mov r9, rbp		;b = temp 

firstBigger:
	mov rax, r8		;c = a
	sub rax, r9		;c = c - b
	cmp rax, 1
	jle returnMin

loop:
	add rsi, rcx		;a = a+b
	mov rax, rsi		;rax = a
	mov rdx, 0			;clear rest of division
	div rbx				;rax = rax / 2
	mov rsi, rax		;a = rax

	mov rax, r10		;rax = value (first argument)
	mov rdx, 0			;clear rest of division
	div rsi				;rax = rax/a
	mov rcx, rax		;b = rax

	jmp loopCheck 		;if |a-b| > 1 loop again

returnMin:
	cmp rsi, rcx
	jle aSmaller

bSmaller:
	mov rax, rcx
	jmp end

aSmaller:
	mov rax, rsi

end:
	ret