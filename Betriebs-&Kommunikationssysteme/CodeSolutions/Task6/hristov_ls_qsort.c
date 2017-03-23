#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <dirent.h> 
#include <string.h>
#include <stdbool.h>
#include <sys/stat.h>
#include <time.h>
#include "hristov_ls.h"
#define valid_flags "lart"
#define dashes "-------------------------------------------------------------------------------"

char* pwd_sys_environment;



//-----------------helper functions---------------------------

char* append_to_string(char* str1, char* str2) {

	size_t len1 = strlen(str1);
	size_t len2 = strlen(str2);
    char *final_str = malloc(len1 + len2 + 1 );

    strcpy(final_str, str1);
    strcat(final_str, str2);
    
    return final_str;
}
//-----------------helper functions---------------------------



int check_working_dir(void) {

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

				fprintf(stderr, "Invalid flag parsed!\n");
				continue;
			}


			flags = append_to_string(flags, argv[i]);
		}
	}

	return flags;
}

bool are_valid_flags(char* flag_string) {

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
	int (*crnt_filter)(struct dirent*) = NULL;

	if(check_working_dir() == EXIT_FAILURE) {

		fprintf(stderr, "System environment PWD not defined. Cannot access the working directory.\n");
		return EXIT_FAILURE;
	}

	requested_directory = find_requested_dir(argc, argv);
	flags = parse_flags(argc, argv, flags);


	if(strchr(flags, 'a') == NULL) {

		crnt_filter = is_hidden_filter;
	}



	number_of_files = scandir(requested_directory, &directory_entries_arr, (int (*)(const struct dirent*))crnt_filter, 0);
	//would be better to check for sort flag and sort as we read, though I didn't 
	
	print_dir_according_to_flags(directory_entries_arr, number_of_files, flags);



	return EXIT_SUCCESS;
}

void print_dir_according_to_flags(struct dirent ** dir_entries, int count, char* flags) {

	int initial_count;

	size_t flags_length = strlen(flags);

	bool chronologically_sorted = false;
	bool latest_last = false;
	bool in_list = false;
	bool show_hidden = false;

	//in order to make the operations as requested in the task
	for(size_t fidx=0; fidx < flags_length ; fidx++) {

		switch(flags[fidx]) {

			case 'l': in_list = true; break;
			case 'r': latest_last = true; break;
			case 'a': show_hidden = true; break;
			case 't': chronologically_sorted = true; break;
		}
	}

	if (count < 0)
		fprintf(stderr, "Error while scanning dir\n"); //at least the current dir should be returned, as we are reading something similar to "ls -a"
	else {

		initial_count = count;


		if(chronologically_sorted) 
			qsort(dir_entries, count, sizeof(struct dirent*), chronologically_compare);

		while (count--) {
			
			struct dirent *crnt_file = latest_last ? dir_entries[initial_count - count -1] : dir_entries[count];

			if(crnt_file->d_name[0] == '.' && !show_hidden)
				continue;

			fprintf(stdout, "%s%s", crnt_file->d_name, in_list ? "\n" : "\t");
			free(crnt_file);
		}
		free(dir_entries);
		fprintf(stdout, "%s", in_list ? "" : "\n");
	}
}

int is_hidden_filter(struct dirent *file) {

	return file->d_name[0] != '.';
}

int chronologically_compare(const void * a, const void * b) {

	//DON'T TOUCH PLS, PURE MAGIC!!!

	struct stat fileA_stat;
	struct stat fileB_stat;

	char* file1 = (*(struct dirent **) a)->d_name;
	char* file2 = (*(struct dirent **) b)->d_name;


	time_t time1, time2;

	stat( file1, &fileA_stat);
	stat( file2, &fileB_stat);

	time1 = fileA_stat.st_mtime;
	time2 = fileB_stat.st_mtime;

	if(false) {

		fprintf(stderr, "Could not read information about files in directory!\n");
		return 0;
	}
	else {

		return time1 >= time2;
	}

	return 0;
}































