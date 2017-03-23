#include<stdio.h>
#include<stdlib.h>

char isSumOfRestEqualToLast(int amount, char **numbers) {

	int sum = 0;
	int lastNumber = atoi(numbers[amount-1]);

	for (int num = 0; num < amount - 1; ++num)
	{
		int currentNum = atoi(numbers[num]);
		sum += currentNum;
	}


	return sum == lastNumber ? '1' : '0';
}

int main(int argc, char **argv) {


	printf("%c\n", isSumOfRestEqualToLast(argc, argv));

	return 0;
}