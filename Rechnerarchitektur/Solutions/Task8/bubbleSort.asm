section .text

global bubblesort

bubblesort:
	
	push rsp
	push rax			;save all the needed registers to return them to default later
	push rbx
	push rdx
	push r8
	push r9
	push r10
	push r11
	push r12
	push r13
	push r14
	push r15
	push rbp
	mov rbp, rsp		;save the stack pointer in the base pointer

	mov r8, rsi
	sub r8, 1			;save length-1 for loop checks

	cmp rsi, 1
	jle end				;if the array has 1 or less elements, return it, it is sorted

	mov r13, 0			;outer loop counter

bubbleSortLoopOuter:

	inc r13

	bubbleSortLoopInner:

		mov rsp, rdi		;move the stack pointer to the first element of the array
		mov rax, [rsp]		;move 1 element to rax
		mov rdx, 0			;save index of array, inner loop counter

		cmp rdx, r8			;i < arr.length -1
		jge end

		nexElemLoop:
			
			mov r9, rsp
			mov r10, rdx
			imul r10, 8
			add r9, r10			;get pointer of prev element

			add rdx, 1			;increment arr index

			mov r11, rsp
			mov r10, rdx
			imul r10, 8
			add r11, r10		;get pointer of crnt element


			mov r14, [r9]
			mov r15, [r11]
			cmp r14, r15		;cmp crnt and next
			jle nexElemLoopEnd 	;if crnt <= next continue, else switch


			switchPositions:
				mov r12, [r11]
				mov r14, [r9]
				mov [r11], r14
				mov [r9], r12


			nexElemLoopEnd:

				cmp rdx, r8
				jl nexElemLoop ;if the crnt index is not the last, go to next element

	cmp r13, rsi
	jl bubbleSortLoopOuter



end:
	mov rsp, rbp		;go to the top of the call stack

	pop rbp				;return the registers to their previous state
	pop r15
	pop r14
	pop r13
	pop r12
	pop r11
	pop r10
	pop r9
	pop r8
	pop rdx
	pop rbx
	pop rax
	pop rsp

	ret