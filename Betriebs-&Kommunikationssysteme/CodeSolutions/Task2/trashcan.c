#include <sys/stat.h> 
#include <fcntl.h>
#include <string.h>


#define buffer_size 8192
#define trashcan_name "./.ti3_trashcan"
#define trashcan_destination "./.ti3_trashcan/"

int copy(char *sourcename, char *targetname) {

//ERROR CODES:  
//	0- SUCCESS!!! /\^>^/\
//	1- source file not found
//	2- target file cound not be created
//	3- I/O error, content was read, but couldn't be written

	int read_fd, write_fd; 													//read/write file descriptors
    int read_stream, write_stream; 											//read/write streams; more like read/write pipes, as we use them multiple times in one read/write operation
    char buffer[buffer_size]; 												//size of buffer char array, where data will be saved on read and from where it would be written

	read_fd = open(sourcename, O_RDONLY | O_SYNC, 0666); 					//open file with extended permissions

	if(read_fd == -1) {

		perror("Source file could not be found!");
		return 1;
	}

	printf("%s\n", targetname);

	write_fd = open(targetname, O_WRONLY | O_CREAT, 0666); 					//create file with extended permissions
    if(write_fd == -1){
        
        perror("File could not be created!");
        return 2;
    }


     while((read_stream = read (read_fd, &buffer, buffer_size)) > 0){ 		//read as much of the file, as our buffer is capable of saving
            write_stream = write (write_fd, &buffer, (int) read_stream);	//write the data in the new file
            if(write_stream != read_stream){

            	perror("Error while writing!");
                return 3;
            }
    }

    close (read_fd);
    close (write_fd);
 
    return 0; 																//success
}

int main(int argc, char* argv[]) {

//ERROR CODES:
//	2 - user tried to use trashcan on the program or its source code
//	3 - wrong input, copy was called with number of arguments != 4


	char* pathToFileInTrashcan[strlen(trashcan_destination) + strlen(argv[2])]; //define array of chars, capable of taking our whole path to file in trashcan

	mkdir(trashcan_name, S_IRWXU | S_IRWXG | S_IRWXO);							//create the trashcan folder, if it doesn't exist. If it already does, command is ignored

	if(strcmp(argv[1], "copy") == 0) {

		if(argc != 4) {
			perror("Wrong input! Please enter copy <sourcename> <targetname>");
			return 3;
		}
		else {

			copy(argv[2], argv[3]);
		}
	}
	else if(argv[1][0] == '-') {

		if(strcmp(argv[2], "trashcan") == 0 || strcmp(argv[2], "trashcan.c") == 0) {

			perror("Tried to corrupt program files.");
			return 2;
		}

		strncpy(pathToFileInTrashcan, trashcan_destination, strlen(trashcan_destination) + strlen(argv[2]));
		strcat(pathToFileInTrashcan, argv[2]);

		switch (argv[1][1]) {													//handle all the possible commands

			case 'd': 
				copy(argv[2], pathToFileInTrashcan); 
				remove(argv[2]); 												//removes file/directory by given path
				break;
			case 'r': 
				copy(pathToFileInTrashcan, argv[2]); 
				remove(pathToFileInTrashcan); 
				break;
			case 'f': 
				remove(pathToFileInTrashcan); 
				break;
			default:
				perror("%s\n", "Such command does not exist!"); 
				break;
		}
	}

	return 0;
}