#include<stdio.h>
#include <stdint.h>

#define MEM_SIZE 102400

char memory[MEM_SIZE];

struct block{
    int size;
    int free;
    struct block *next;
};

void init();
void *split(struct block *main_block,int32_t byte_count);
void *memory_allocate(int32_t byte_count);
void merge();
void memory_free(void *pointer);

void memory_print();