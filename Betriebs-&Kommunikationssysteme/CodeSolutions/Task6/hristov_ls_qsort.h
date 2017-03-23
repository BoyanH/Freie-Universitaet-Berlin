#define _GNU_SOURCE
#include <stdbool.h>
#include <dirent.h>

int check_working_dir(void);
char* find_requested_dir(int argc, char* argv[]);
char* parse_flags(int argc, char* argv[], char *flags);
int ls(int argc, char* argv[]);
void print_dir_according_to_flags(struct dirent ** dir_entries, int count, char* flags);

char* append_to_string(char* str1, char* str2);
bool are_valid_flags(char* flag_string);

int is_hidden_filter(struct dirent *file);
int chronologically_compare(const void * a, const void * b);
