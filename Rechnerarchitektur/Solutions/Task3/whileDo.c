#include <stdio.h>
#include <stdlib.h>

extern int whileDo (unsigned int, unsigned int);

int main (int argc, char* argv[]) {

	unsigned int x = 0;
	unsigned int y = 0;

	if(argc < 2) {
		puts("Enter x and y to count iteration for while(X>Y) loop.\n");
		puts("X: ");
		scanf("%u", &x);
		puts("\nY: ");
		scanf("%u", &y);
		puts("\n");
	} else {
		y = atoi(argv[1]);
	}

	int result = whileDo(x, y);
	printf("While x>y do z++; x = %d, y = %d; Result: z=%d\n", x, y, result);

	return 0;
}