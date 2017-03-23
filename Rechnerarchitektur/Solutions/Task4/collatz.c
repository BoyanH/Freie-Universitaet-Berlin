#include <stdio.h>
#include <stdlib.h>

extern int collatz(unsigned int);

int main(int argc, char* argv[]) 
{

	unsigned int k;

	if(argc < 2) 
	{
		printf("Enter i to calculate collatz(i): ");
		scanf("%d", &k);
	}
	else 
	{
		k = atoi(argv[0]);
	}

	printf("collatz(%d) = %d\n", k, collatz(k));

	return 0;
}