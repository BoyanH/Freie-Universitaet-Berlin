#include <stdlib.h>
#include <stdio.h>
#include <stdint.h>

#define LEN 5

extern char* insertSort(char*, uint64_t);

void printArray(char* to_show, uint64_t len) {
	for(uint64_t i=0; i<len; i++) {
		if(i == 0) { // erstes Element
			printf("\nArray: %c ", to_show[i]);
		} else {
			printf(",%c ", to_show[i]);
		}
	}

	printf("\n");
}

int main(int argc, char* argv[]){
	
	char to_sort[5] = "World";
	
	printArray(to_sort, LEN);
	insertSort(to_sort, LEN);
	printArray(to_sort, LEN);
	
	return EXIT_SUCCESS;
}

