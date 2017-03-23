#include <stdlib.h>
#include <stdio.h>
#include <stdint.h>
#include <time.h>

#define LEN 10

extern int64_t* bubblesort(int64_t*, uint64_t);

void printArray(int64_t* to_show, uint64_t len) {
	for(uint64_t i=0; i<len; i++) {
		if(i == 0) { // erstes Element
			printf("Array: %ld, ", to_show[i]);
		} else if(i == (len-1)) { // letztes Element
			printf("%ld\n", to_show[i]);
		} else {
			printf("%ld, ", to_show[i]);
		}
	}
}

int main(int argc, char* argv[]){
	srand(time(NULL));	
	int64_t to_sort[LEN];
	for(int8_t i=0; i<LEN; i++) {
		to_sort[i] = rand();
		// int8_t neg = rand() % 2;
		// if(neg) {
		// 	to_sort[i] = to_sort[i] * -1;
		// }
	}
	
	printArray(to_sort, LEN);
	bubblesort(to_sort, LEN);
	printArray(to_sort, LEN);
	
	return EXIT_SUCCESS;
}

