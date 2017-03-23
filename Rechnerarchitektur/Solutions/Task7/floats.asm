section .text
global floatAdd, floatSub

floatAdd:
	addss xmm0, xmm1
	ret

floatSub:
	subss xmm0, xmm1
	ret