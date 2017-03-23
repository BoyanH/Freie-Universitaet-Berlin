#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

typedef struct  __elem {         // Elemente  der  verketteten  Liste

	uint64_t  cycles_done;    // Anzahl  b e r e i t s  durchlaufener  Zyklen
	uint64_t  cycles_waited;  // Anzahl  b e r e i t s  gewarteter  Zyklen
	uint64_t  cycles_todo;    // Anzahl  der  zu  verarbeitenden  Zyklen
	uint64_t  pId;              //  Prozess ID

	uint64_t arrival_time;
	uint64_t execution_time;

	struct __elem *next;    //  Doppelt  verkettete  Liste
	struct __elem *prev;
} _elem;
typedef _elem* elem;

void init_process_scheduler(elem head, uint64_t count, uint64_t list_of_processes[count][2]);
elem mark_as_ready(elem process);
elem round_robin(elem head,elem current,int tStep);
elem fcfs(elem head,elem current,int tStep);
elem spn(elem head,elem current,int tStep);