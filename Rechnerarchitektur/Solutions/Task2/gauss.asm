global gauss

section .text
gauss: ; (n^2 + n)/2

	mov rbx, rax ;move our parameter to another register to save it temp=parameter
	mul rax ; parameter = parameter*parameter
	add rax, rbx ;parameter += temp
	mov rcx, 2 
	div rcx ; parameter /= 2
    ret