#include <stdio.h>
#include <stdlib.h>

extern int gauss(unsigned int);

int main(int argc, char* argv[]) {
	
	unsigned int gauss_num = 0;
	
	if(argc < 2) {
		puts("Gib bitte eine Zahl ein:");
		scanf("%u", &gauss_num);
	} else {
		gauss_num = atoi(argv[1]);
	}

	int gauss_sum = gauss(gauss_num);
	printf("Gauss Summe: %d\n", gauss_sum);

	return 0;
}

