#include <stdio.h>
#include <stdlib.h>

extern int getFibonacci (unsigned int);

int main (int argc, char* argv[]) {

	unsigned int num = 0;

	if(argc < 2) {
		puts("Enter n to get the nth Fibonacci number.\n");
		puts("N: ");
		scanf("%u", &num);
	} else {
		num = atoi(argv[1]);
	}

	int nthFib = getFibonacci(num);
	printf("The %d. Fibonacci number is: %d\n", num, nthFib);

	return 0;
}