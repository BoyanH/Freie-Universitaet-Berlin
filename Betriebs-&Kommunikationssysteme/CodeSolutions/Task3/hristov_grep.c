#include <stdio.h>
#include <string.h>

void find_repetitions(FILE* read_stream, char* searched_string) {

	char crnt_line[100];
	char crnt_occurance[100];
	int occurance_index;

	while(fscanf(read_stream , "%[^\n]\n" , crnt_line)!=EOF){

		if(strstr(crnt_line , searched_string) !=NULL){
			
			fprintf(stdout, "%s\n", crnt_line);
		}
	}
}

int main(int argc, char* argv[]) {

	FILE* read_stream;

	if(argc == 2) { 														// no file was specified, use stdin

		find_repetitions(stdin, argv[1]);
	}
	else if(argc == 3) {

		read_stream = fopen(argv[2], "r");

		if(read_stream == NULL) {

			fprintf(stderr, "%s\n", "The specified file cannot be opened!");
			return 1;
		}

		find_repetitions(read_stream, argv[1]);
		fclose(read_stream); 												//always clsoe the stream after work is done, but never if it is one of the standart streams
	}
	else {

		fprintf(stderr, "%s\n", "Invalid user input!");
		return 1;
	}

	return 0;
}