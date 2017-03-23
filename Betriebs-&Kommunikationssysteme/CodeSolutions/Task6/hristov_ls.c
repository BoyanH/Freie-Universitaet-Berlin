#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <dirent.h> 
#include <string.h>
#include <stdbool.h>
#include <sys/stat.h>
#include <time.h>
#include "hristov_petrov_ls.h"
#define valid_flags "lart"
#define dashes "-------------------------------------------------------------------------------"

char* pwd_sys_environment;


int check_working_dir(void) {

	//if we can't get PWD variable, something's not good
	pwd_sys_environment = getenv("PWD");
	 if(  pwd_sys_environment== NULL ) {
	 	return EXIT_FAILURE;
	 }

	 return EXIT_SUCCESS;
}

char* find_requested_dir(int argc, char* argv[]) {

	//no sense to read the programm name, so skip argv[0]
	for(int i = 1; i < argc; i++) {

		if(argv[i][0] != '-') {

			return argv[i];
		}
	}

	return pwd_sys_environment; //if no directory parsed, use the current one
}


//we understand flags as single letters; a whole word is terefore a sequence of flags.
char* parse_flags(int argc, char* argv[], char *flags) {

	for(int i = 1; i < argc; i++) {

		if(argv[i][0] == '-') {


			bool flags_valid;
			argv[i]++; //don't copy the dash in front of the flag as well

			if(!(flags_valid = are_valid_flags(argv[i]) )) {

				//if an argument includes an invalid flag, skip the whole argument
				fprintf(stderr, "Invalid flag entered!\n");
				continue;
			}


			flags = append_to_string(flags, argv[i]);
		}
	}

	return flags;
}

bool are_valid_flags(char* flag_string) {

	//check if a non-defined flag was parsed;

	size_t str_len = strlen(flag_string);

	for(size_t i = 0; i < str_len; i++) {

		if(strchr(valid_flags, flag_string[i]) == NULL) {
		    return false;
		}
	}

	return true;
}



int ls(int argc, char* argv[]) {

	char* requested_directory;
	struct dirent **directory_entries_arr;
	char* flags = "";
	int number_of_files;

	int (*crnt_filter) (struct dirent*) = NULL;
	int (*crnt_sort) (const struct dirent**, const struct dirent** b) = NULL;


	//if there is no workig directory, chances are we won't be able to access any other as well
	if(check_working_dir() == EXIT_FAILURE) {

		fprintf(stderr, "System environment PWD not defined. Cannot access the working directory.\n");
		return EXIT_FAILURE;
	}

	requested_directory = find_requested_dir(argc, argv); //determine which directory should be used for ls. If none ist stated, use PWD variable.
														//we can do that safely here, as we checked that it exists in check_working_dir()
	
	flags = parse_flags(argc, argv, flags);				//parse all flags as a string, so we can search in it more easily


	//check for hidden elements and sorting by date, as we can pass those functions directly to scandir
	if(strchr(flags, 'a') == NULL) {

		crnt_filter = is_hidden_filter;
	}

	if(strchr(flags, 't') != NULL) {

		crnt_sort = chronologically_compare;
	} 



	//according to flags, pass the corresponding function for filter and sort
	//if the flags don't exist, we use the initial ones, NULL
	number_of_files = scandir(requested_directory, &directory_entries_arr, (int (*)(const struct dirent*))crnt_filter, crnt_sort);
	
	print_dir_according_to_flags(directory_entries_arr, number_of_files, flags);



	return EXIT_SUCCESS;
}

void print_dir_according_to_flags(struct dirent ** dir_entries, int count, char* flags) {

	int initial_count;

	bool latest_last = strchr(flags, 'r');
	bool in_list = strchr(flags, 'l');

	if (count < 0)
		fprintf(stderr, "Error while scanning dir\n"); //at least the current dir should be returned, as we are reading something similar to "ls -a"
	else {

		//save the initial count, as we decrement count, but we need the initial for reverse flag
		initial_count = count;

		while (count--) {
			
			//if reverse flag is on, use the opposite index
			struct dirent *crnt_file = latest_last ? dir_entries[initial_count - count -1] : dir_entries[count];

			//if list flag is on use new lines after each file, otherwise use tabs
			fprintf(stdout, "%s%s", crnt_file->d_name, in_list ? "\n" : "\t");
			free(crnt_file);	//remember to free the memory from the buffer, we won't need it anymore
		}
		free(dir_entries);
		fprintf(stdout, "%s", in_list ? "" : "\n");
	}
}

int is_hidden_filter(struct dirent *file) {

	return file->d_name[0] != '.';			//on linux, hidden files begin with .
}

int chronologically_compare(const struct dirent** a, const struct dirent** b) {


	struct stat fileA_stat;
	struct stat fileB_stat;

	bool file_a_failed = false;
	bool file_b_failed = false;

	//was a major problem for me to get this right. We are working with array of pointers, so remember to get the real value out of it
	//not only the pointer while casting. therefore we need this * before the cast
	char* file1 = (*(struct dirent **) a)->d_name;
	char* file2 = (*(struct dirent **) b)->d_name;


	time_t time1, time2; //for easier casting

	file_a_failed = stat( file1, &fileA_stat); //check for errors, while reading information about files
	file_b_failed = stat( file2, &fileB_stat);

	if(file_a_failed || file_b_failed) { //handle on error, when filed could not be examined

		fprintf(stderr, "Could not read information about files in directory!\n");
		return 0;
	}
	else {

		time1 = fileA_stat.st_mtime; //or get time
		time2 = fileB_stat.st_mtime;

		return time1 >= time2; //and return if a >= b, what the compare function expects
	}

	return 0;	//stop compiler from complaining
}





//-----------------helper functions---------------------------

char* append_to_string(char* str1, char* str2) {

	//copy both strings into a bigger one

	size_t len1 = strlen(str1);
	size_t len2 = strlen(str2);
    char *final_str = malloc(len1 + len2 + 1 );

    strcpy(final_str, str1);
    strcat(final_str, str2);
    
    return final_str;
}
//-----------------helper functions---------------------------











