section .text

global insertSort

insertSort:
	
	push rax				;save all the needed registers to return them to default later
	push rbx
	push rdx
	push r8
	push r9
	push rbp
	mov rbp, rsp			;save the stack pointer in the base pointer

	cmp rsi, 1
	jle end 				;a string with 1 or less characters is sorted already

	mov r8, 1
	outerLoop:

		mov rsp, rdi
		add rsp, r8

		mov al, [rsp]		;save the currently to be sorted element
		
		mov r9, r8
		dec r9				;start loop from prev position and search for the element's position in the sorted list
		innerLoop:

			mov rsp, rdi
			add rsp, r9 	;go to list[i]

			mov ah, [rsp] 	;save list[i]
			cmp ah, al
			ja break		;if greater or equal, crnt element is sorted, else, switch positions

			mov [rsp], al	;switch positions, using 8-bit registers; COSTED ME 3 HOURS, IMPORTAN!!!
			mov rsp, rdi
			add rsp, r8
			mov [rsp], ah

			dec r9
			cmp r9, 0
			jg innerLoop

			break:



		inc r8
		cmp r8, rsi
		jl outerLoop


end:

	mov rsp, rbp			;go to the top of the call stack

	pop rbp					;return the registers to their previous state
	pop r9
	pop r8
	pop rdx
	pop rbx
	pop rax

	ret