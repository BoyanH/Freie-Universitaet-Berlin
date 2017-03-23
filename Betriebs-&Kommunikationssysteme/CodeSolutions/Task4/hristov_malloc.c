#include<stdio.h>
#include <stdint.h>
#include "hristov_malloc.h"

struct block *freeList = (void *) memory;

void init() {
    freeList->size = MEM_SIZE - sizeof(struct block);
    freeList->free = 1;
    freeList->next = NULL;

    //initialize our memory, which is created by a nested chain of structures, each of which pointing to the next one
    //this way, we know at which pointer the next memory block begins and how big our current one is, as well as whether or not it is free
}

void *split(struct block *main_block, int32_t byte_count) {


    //create our new block, which is placed at the end of the memory
    //don't forget to subtract the size of the structure as well, otherwise we are not giving full byte_count bytes to the user
    struct block *new_block = (void *) (memory + main_block->size - byte_count);
    new_block->size = byte_count;
    new_block->free = 0; //allocated
    new_block->next = NULL; //last in memory

    main_block->size = (main_block->size) - byte_count - sizeof(struct block); //we are allocating not only the size requested by the user,
                                                                            //but the size needed to support our structure
    main_block->next = new_block; //connect our memory again, each cell must point to the next one

    return (void*)(++new_block); //return next pointer, as current one is the structure block, which consists of information, useful for us only
                                //this information should never, under any circumstances, be overwritten
}

void *memory_allocate(int32_t byte_count) {
    struct block *crnt;
    void *result;
    if (!(freeList->size)) { //initialize our memory if we don't find any structures
        init();
        fprintf(stdout, "Memory initialized\n");
    }
    crnt = freeList;
    //while all cells we went through are either to small or taken, go on untill the end of the memory if nothing found
    while ((((crnt->size) < byte_count) || ((crnt->free) == 0)) && (crnt->next != NULL)) {

        crnt = crnt->next;
        fprintf(stdout, "Block too small or taken.\n");
    }

    //if the free cell (there is only one free cell in our memory) is smaller, we need to cut a part of it and allocate it
    if ((crnt->size) > byte_count) {
        
        fprintf(stdout, "A bigger block found, splitting and allocating.\n");
        return split(crnt, byte_count);
    }
    //if we are lucky, and we have exactly enough data to allocate a cell and support it using struct, we are lucky and we
    //can simply mark our last block as taken
    else if(crnt-> size == byte_count + (int32_t)sizeof(struct block)) {

    	crnt->free = 0;
        result = (void *) (++crnt);
        fprintf(stdout, "Perfect match allocated\n");
        return result;
    }

    //otherwise, we did our best, but there is just not enough memory, notify the user with error message and return NULL
    result = NULL;
    fprintf(stderr, "Insufficient memory\n");
    return result;
}

//as we use the logic, that our free memory cosists of one big block, we need to merge the free blocks after allocating
void merge() {
    struct block *crnt;
    crnt = freeList;

    //go through the whole memory
    while ((crnt->next) != NULL) {

        //if two neighbour cells are both empty merge them
        if ((crnt->free) && (crnt->next->free)) {
            crnt->size += (crnt->next->size) + sizeof(struct block); //set size of the first one equal to size of both + the freed structure
            crnt->next = crnt->next->next;  //set next to the over-next cell
        }
        //else, keep looking for cells to merge
        else {
        	crnt = crnt->next;
        }
    }
}

void memory_free(void *pointer) {


    if (((void *) memory <= pointer) && (pointer <= (void *) (memory + MEM_SIZE))) {
        struct block *crnt = pointer; //get the block given to the user

        --crnt;          //go back to the previous index, which is the struct
        crnt->free = 1; //set it to free
        merge();       //merge it to keep our logic with a single big free memory cell
    }
    else printf("Please provide a valid pointer allocated by memory_allocate\n");
}

void memory_print() {
    if (!(freeList->size)) {
        init();
    }

    struct block *block = freeList;
    int count = 0;
    fprintf(stdout, "#################################################\n");
    fprintf(stdout, "Memory usage: %d / %d bytes used \n", MEM_SIZE - block->size, MEM_SIZE);
    fprintf(stdout, "#################################################\n");


    fprintf(stdout, "# |at\t\t |free\t\t |size\t \t\t|next block\n");
    while (block != NULL) {
        fprintf(stdout, "%d |%p\t |%d \t\t |%d\t  \t|%p\n", ++count, (void*)block, block->free, block->size, (void*)block->next);
        block = block->next;
    }
    fprintf(stdout, "\n\n");
}