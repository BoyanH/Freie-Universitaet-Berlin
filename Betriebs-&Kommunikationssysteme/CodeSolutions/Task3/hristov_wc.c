#include <stdio.h>
#include <string.h>

/*
	output_array[0] = words count
	output_array[1] = lines count
	output_array[2] = bytes count

*/

int is_separator(char c) {

	char separators[] = {' ', ',', '.', '?', '!', '\n'}; //all our possible word separators, add more if you wish :P
	int separators_size = sizeof(separators);
	int is_separator = 0;

	for(int i=0; i < separators_size; i++) {

		if(separators[i] == c) {

			is_separator = 1;
			break;
		}
	}

	return is_separator;
}

void count_lines_words_and_bytes(FILE* read_stream, int output_array[]) {

	char crnt_char;
	int in_new_word = 1;

	while((crnt_char = fgetc(read_stream)) != EOF) { //read all characters untill end of file

		//if our current word is still empty, don't increase word count, 2 separators after one another
		if(is_separator(crnt_char) == 1 && in_new_word == 0) {

			in_new_word = 1;
			output_array[0]++; //new word found
		}
		else if(is_separator(crnt_char) == 0 && in_new_word == 1) { //our word is no longer empty, note that

			in_new_word = 0;
		}
		//separator after separator case is not handled, not words are added, crnt word is still empty

		if(crnt_char == '\n') {

			output_array[1]++; //new line found
		}


		output_array[2]++; //new symbol found
	}
}


int main(int argc, char* argv[]) {

	FILE* read_stream;
	int output_array[3] = {0};

	if(argc == 1) { 														// no file was specified, use stdin

		count_lines_words_and_bytes(stdin, output_array);
	}
	else if(argc == 2) {

		read_stream = fopen(argv[1], "r");

		if(read_stream == NULL) {

			fprintf(stderr, "%s\n", "The specified file cannot be opened!");
			return 1;
		}

		count_lines_words_and_bytes(read_stream, output_array);
		fclose(read_stream); 												//always clsoe the stream after work is done, but never if it is one of the standart streams
	}
	else {

		fprintf(stderr, "%s\n", "Invalid user input!");
		return 1;
	}

	fprintf(stdout, "Lines: %d, Words: %d, Bytes: %d\n", output_array[1], output_array[0], output_array[2]);

	return 0;
}