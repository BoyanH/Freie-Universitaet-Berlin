section .text
global init, push, top, isEmpty, pop

init:

	push rbp		;save the starting base pointer

	mov rbp, rsp	;set base pointer to stack pointer

	sub rsp, 8		;subtract 8 from the base pointer to go to next stack index
	mov [rsp], rsp	;save the last stack index at first position, currently the adress itself
	
	mov rsp, rbp	;move the stack pointer back to the beginning
	pop rbp			;remove rbp from the stack and return it to its previous register

	ret

push:

	push rbp		;save the starting base pointer
	mov rbp, rsp	;set base pointer to stack pointer

	sub rsp, 8		;go to the first element of stack, where the last address of the stack is saved
	mov rax, [rsp]	;get the value
	sub rax, 8		;go to next address
	mov [rsp], rax	;save the new last stack address
	
	mov rsp, rax	;move the stack pointer to the last address
	mov [rsp], rdi	;save there the new value
	
	
	mov rsp, rbp	;move the stack pointer back to the beginning
	pop rbp			;remove rbp from the stack and return it to its previous register

	ret

top: 
	push rbp
	mov rbp, rsp

	sub rsp, 8
	mov rax, [rsp]			;here is the address of the last element
	mov rax, [rax]			;get the value in that address

	mov rsp, rbp
	pop rbp

	ret

isEmpty:

	push rbp
	mov rbp, rsp

	sub rsp, 8
	mov rbx, [rsp]			;here is the address of the last element
	
	cmp rsp, rbx
	jle empty

	notEmpty:
		mov rax, 0
		jmp end

	empty:
		mov rax, 1

	end:
		mov rsp, rbp
		pop rbp

		ret

pop:

	push rbp		;save the starting base pointer
	mov rbp, rsp	;set base pointer to stack pointer

	sub rsp, 8		;go to the first element of stack, where the last address of the stack is saved
	mov rax, [rsp]	;get the value

	add rax, 8		;go to the previous adress, remove last element
	mov [rsp], rax	;save the new last stack address

	sub rax, 8
	mov rsp, rax	;move to the last element
	mov rax, [rsp]	;get the last element
	
	mov rsp, rbp	;move the stack pointer back to the beginning
	pop rbp			;remove rbp from the stack and return it to its previous register

	ret