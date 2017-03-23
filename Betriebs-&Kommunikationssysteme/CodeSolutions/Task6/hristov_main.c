#include <stdio.h>
#include <stdlib.h>
#include <dirent.h> 
#include "hristov_petrov_ls.h"

int main(int argc, char* argv[]) {


	int ls_return_code = ls(argc, argv);

	if(ls_return_code == EXIT_SUCCESS) 
		return EXIT_SUCCESS;


	printf("%s\n", "There was an error in function ls");

	return EXIT_FAILURE;
}