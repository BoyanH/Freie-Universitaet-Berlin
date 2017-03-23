global gauss

section .text
gauss:

	mov rbx, 0 ;iteration counter / crnt number to add
	mov rcx, rax ; our number for gauss
	mov rax, 0; the gauss sum
	jmp addNumber ;start the loop

	addNumber:
		add rbx, 1
		add rax, rbx ;gaussSum += crntNum
		cmp rbx, rcx ;compare the iteration with the final number
		jl addNumber ;continou if this is not the last iteration
    	ret ;otherwise, return