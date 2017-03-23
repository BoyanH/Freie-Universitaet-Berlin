#include <stdio.h>

void writeMultipleCharsOnLine(char c, int amount) {

	for(; amount > 0; amount--,fprintf(stdout, "%c", c));
	fprintf(stdout, "\n");
}

void printFile(FILE *readStream, char* inputSource) {

	char crnt_char;


	//wrothe some cool wrappers, had to remove them to enable chaining
	// fprintf(stdout, "Contents of %s: \n", inputSource);
	// writeMultipleCharsOnLine('-', 30);

	while((crnt_char = fgetc(readStream)) != EOF) {

		printf("%c", crnt_char);
	}
	// writeMultipleCharsOnLine('-', 30);
}

int main(int argc, char* argv[]) {

	FILE *readStream;

	if(argc == 1) { // no file was specified, use stdin

		fprintf(stdin, "user input");
	}
	else if(argc == 2) {

		readStream = fopen(argv[1], "r");

		if(readStream == NULL) {

			fprintf(stderr, "%s\n", "The specified file cannot be opened!");
			return 1;
		}

		fprintf(readStream, argv[1]);
		fclose(readStream); 				//always clsoe the stream after work is done, but never if it is one of the standart streams
	}
	else {

		fprintf(stderr, "%s\n", "Invalid user input!");
	}

	return 0;
}