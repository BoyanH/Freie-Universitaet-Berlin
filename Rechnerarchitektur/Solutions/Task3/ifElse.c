#include <stdio.h>
#include <stdlib.h>

extern int ifElse (unsigned int, unsigned int, unsigned int, unsigned int, unsigned int);

int main (int argc, char* argv[]) {

	unsigned int a = 0;
	unsigned int b = 0;
	unsigned int x = 0;
	unsigned int y = 0;
	unsigned int z = 0;

	if(argc < 5) {
		puts("Enter a, b, x, y and z for if A = B then begin X:=X+1; Y:=Z end else A:=B;\n");
		puts("A: ");
		scanf("%u", &a);
		puts("B: ");
		scanf("%u", &b);
		puts("X: ");
		scanf("%u", &x);
		puts("Y: ");
		scanf("%u", &y);
		puts("Z: ");
		scanf("%u", &z);
	} else {
		a = atoi(argv[0]);
		b = atoi(argv[1]);
		x = atoi(argv[2]);
		y = atoi(argv[3]);
		z = atoi(argv[4]);
	}

	int result = ifElse(a, b, x, y, z);
	printf("if A = B then begin X:=X+1; Y:=Z end else A:=B; For a = %d, b = %d, x = %d, y = %d, z = %d => Result: z=%d\n",
			a, b, x, y, z, result);

	return 0;
}