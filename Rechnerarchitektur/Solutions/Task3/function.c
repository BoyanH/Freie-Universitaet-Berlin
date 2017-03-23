#include <stdio.h>
#include <stdlib.h>

extern int function (unsigned int, unsigned int);

int main (int argc, char* argv[]) {

	unsigned int x = 0;
	unsigned int y = 0;

	if(argc < 2) {
		puts("Enter x and y to get function f(x, y) return 7 + x*2 + y*30.\n");
		puts("X: ");
		scanf("%u", &x);
		puts("\nY: ");
		scanf("%u", &y);
		puts("\n");
	} else {
		x = atoi(argv[0]);
		y = atoi(argv[1]);
	}

	int result = function(x, y);
	printf("f(x, y) return 7 + x*2 + y*30; x = %d, y = %d; Result: f(x, y)=%d\n", x, y, result);

	return 0;
}