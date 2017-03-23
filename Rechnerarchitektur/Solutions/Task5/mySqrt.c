#include <stdio.h>
#include <stdlib.h>

extern unsigned int mySqrt(unsigned int);

int main(int argc, char* argv[]) {
	
	unsigned int sqrt_num = 0;
	
	if(argc < 2) {
		puts("Enter n to get the square root of n. \n N: ");
		scanf("%u", &sqrt_num);
	} else {
		sqrt_num = atoi(argv[1]);
	}

	unsigned int sqrt_erg = mySqrt(sqrt_num);
	printf("Square root of %u is: %u\n", sqrt_num, sqrt_erg);

	return 0;
}

