#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include "process_manager.h"
#include <string.h>
#include <unistd.h>

#define PROCESSES_COUNT 4

void start_process_scheduler(char* user_choice) {

	uint64_t processes [PROCESSES_COUNT][2]= {{0 ,3} ,{2 ,7} ,{4 ,1} ,{6 ,5}};

	elem head = (elem)malloc(sizeof(_elem));
	head->next = head;
	head->prev = head;

	int tStep = 0;
	elem current;

	elem (*algorithm) (elem, uint64_t, uint64_t);

	printf("%s\n", user_choice);
	if(strcmp(user_choice, "fcfs") == 0) {

		algorithm = &fcfs;
	}
	else if(strcmp(user_choice, "spn") == 0) {

		algorithm = &round_robin;
	}
	else if(strcmp(user_choice, "rr") == 0) {

		algorithm = &spn;
	}
	else {

		fprintf(stderr, "%s\n", "No such algorithm!");
	}


	init_process_scheduler(head, PROCESSES_COUNT, processes);

	//loop the processes with the given algorith as shown in tutorium
	current = head->next;
	while(head->next != head) {

		//IMPORTANT: maker sure you go to the next element if stuck in head element, otherwise will go in endless loop after 1 process
		if(current == head) {

			current = current->next;
		}
		
		//get the next process to execute
		current = algorithm(head, current, tStep);
		
		//mark cycles, time waited, etc.
		current->cycles_done++;
	    current->cycles_waited += tStep - current->arrival_time;
	    current->arrival_time = tStep + 1;
	    current->execution_time--;

	    //increase steps done
		tStep++;
	}
	fprintf(stdout, "Steps completed before all processes were executed: %d\n", tStep);


}

int main(int argc, char* argv[]) {

	char* dashes = "-----------------------------------------------------------------";


	if(argc < 2) {
	fprintf(stdout, "%s\n No Arguments were parsed. Chose a process scheduling algorithm: \n %s \n %s \n %s \n  %s\n", dashes, 
		"First Come First Serve: fcfs",
		"Shortest Process Next : spn",
		"Round Robin           : rr",
		dashes);
	}
	else if(argc == 2) {

		start_process_scheduler(argv[1]);
	}
	else {

		fprintf(stderr, "%s\n", "No such functionallity!");
	}

	

	return EXIT_SUCCESS;
}