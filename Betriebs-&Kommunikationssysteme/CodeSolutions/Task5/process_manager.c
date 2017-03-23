#include <stdio.h>
#include <stdint.h>
#include "process_manager.h"

void init_process_scheduler(elem head, uint64_t count, uint64_t list_of_processes[count][2]) {

    //initialize our structure, add all processes to the list
    for(uint64_t i = 0; i < count; i++) {

        //allocate memory enough for the structure of the current process
        elem new_process = (elem) malloc(sizeof(_elem));
        
        //enther the data give/initial data
        new_process->arrival_time   = list_of_processes[i][0];
        new_process->execution_time = list_of_processes[i][1];
        new_process->pId            = i;

        new_process->cycles_done    = 0;
        new_process->cycles_todo    = 0;
        
        //unfortunately, in c we can't set current in each loop and use that to add elements in the end of an array or something similar
        //therefore, we set the new element at the beginning of the array, or more like beginning of a stack/chain kind of thing

        head->prev->next    = new_process;
        new_process->prev   = head->prev;

        head->prev        = new_process;
        new_process->next = head;
        //this is basicly how a double-nested-list is build

        fprintf(stdout, "Process with pId=%d added.\n", (int)new_process->pId);
    }
}


elem round_robin(elem head, elem current, int tStep) {

    //this one is easy, if finished, remove, else go to next process, as round robin gives each process equal amount of time
    if(current->execution_time == 0) {
        return mark_as_ready(current);
    }
    return current->next;
}


elem fcfs(elem head, elem current, int tStep) {

    //easy as well, make sure the process has finished before switching it. So if it hasn't finished, return it
    if(current->execution_time == 0) {
        return mark_as_ready(current);
    }
    return current;
}


elem spn(elem head, elem current, int tStep) {

    if(current->execution_time == 0) {
        mark_as_ready(current);
    }

    //here is where it gets kind of tricky. need to loop all our processes through and find the shortest, save it in shortest and return it
    elem shortest = current;

    while(current != head) {

        if(shortest->execution_time > current->execution_time) {

            shortest = current;
        }
        current = current->next;
    }
    return shortest;
}

elem mark_as_ready(elem process) {

    fprintf(stdout, "Process #%d ready. \t cycles_done: %d \t cycles_waited: %d \n", (int)process->pId, (int) process->cycles_done, (int) process->cycles_waited);

    //with some help from stack overflow on removing elements from double nested lists, releasing memory

    elem deleted = process->next; //save element to be deleted to be able to return it later

    process->next->prev = process->prev; //make sure you connect both sides of the chain after you pop out the process
    process->prev->next = process->next;

    free(process); //free the allocated memory
    process = NULL; //break any references

    return deleted;
}
