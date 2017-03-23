#include <stdio.h>
#include <stdlib.h>

extern int forLoop (unsigned int, unsigned int);

int main (int argc, char* argv[]) {

	unsigned int x = 0;
	unsigned int y = 0;

	if(argc < 2) {
		puts("Enter x and y to get the sum of all x down to y.\n");
		puts("X: ");
		scanf("%u", &x);
		puts("\nY: ");
		scanf("%u", &y);
		puts("\n");
	} else {
		y = atoi(argv[1]);
	}

	int result = forLoop(x, y);
	printf("int result = 0; \n (for int x = %d, int y = %d; x <= y; x--;) result += x;\n result=%d\n", x, y, result);

	return 0;
}