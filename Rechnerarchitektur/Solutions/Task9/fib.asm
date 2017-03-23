section .text
global fib
fib:
	push rdi						;pass the argument to the routine
	call fib_helper
	pop rdi							;remove it from the stack
	ret 							;return result

	fib_helper:
	
	    mov rax, [rsp + 8]			;use the return address to store the fib index in order no to use any important registers and require saving
	    cmp rax, 2

	    jg continue
	    
	    mov rax, 1 					;fib(2) = fib(1) = 1
	    jmp end

		continue:
		    push rcx 				;save rcx
		    push r8					;save r8
		    dec rax
		    push rax
		    call fib_helper
		    mov rcx, rax 			;rcx register must remain unchanged, so we can use it

		    mov r8, [rsp]			;use r8 register just to decrement the fibonacci index in stack
		    dec r8
		    mov [rsp], r8

		    call fib_helper
		    add rax, rcx 			;add the result of the previous fib call to the crnt
		    
		    add rsp, 8 				;remove value from stack
		    pop r8					;return r8 to default
		    pop rcx 				;return rcx to default
		
		end:
			ret