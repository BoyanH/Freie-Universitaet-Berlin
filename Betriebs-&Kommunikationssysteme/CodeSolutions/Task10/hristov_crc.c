#include <stdio.h>
#include <unistd.h>
#include <stdint.h>
#include <stdlib.h>
#include <stddef.h>
#include <string.h>
#include <sys/stat.h> 
#include <fcntl.h>
#include <stdbool.h>

#define POLYNOMIAL 0x18005
#define CRC16 0x8005;

//CRC16-IBM => polynomial = x^16 + x^15 + x^2 + x^0 = 1 1000 0000 0000 0101 (2) = 1 8 0 0 5 (16)


uint16_t crc_msb_first(char *data_p, uint16_t size) {

	int current_bit;
	uint16_t rest = 0;
	uint32_t data;

	if(data_p == NULL) {

		fprintf(stderr, "Input is empty!\n");
		return -1;
	}

	while(size > 0) {

		//xxxx xxxx - our current input
		memset(&data, 0, sizeof(data));
		memcpy(&data, data_p, sizeof(*data_p));

		data <<= 9;
		data += rest; //if there is rest from previous loop, add it

		for(current_bit = 0; current_bit < 8; current_bit++) {

			if( (data & (1 << (23 - current_bit))) != 0) {

				//xxxx xxxx <<= 9 (size of POLYNOMIAL)
				//xxxx xxxx 0000 0000 0 - DATA[i]
				//1100 0000 0000 0010 1 - POLYNOMIAL
				//xxxx xxxx 0000 0000 00 - DATA[i] <<=9+1
				// 110 0000 0000 0001 01 - POLYNOMIAL

				//at last step
				//0000 000x 0000 0000 0000 0000
				//        1 1000 0000 0000 0101

				data = data ^ POLYNOMIAL;
			}

			if(current_bit != 7) {
				data <<= 1;
			}
			else {

				//we use 16-bit CRC, so take the last 16 bits as rest;

				//1111 1111 1111 1111 (2) = 0xffff (16)

				rest = data & 0xffff;
			}
		}

		size--; //went through first item of array
		data_p++;	//go to the next
	}

	return rest;
}

uint16_t crc_lsb_first(char *data, uint16_t size) {

	uint16_t out = 0;
    int bits_read = 0, bit_flag;

    if(data == NULL)
        return 0;

    while(size > 0) {
        bit_flag = out >> 15;

        out <<= 1;
        out |= (*data >> bits_read) & 1;

        bits_read++;
        if(bits_read > 7)
        {
            bits_read = 0;
            data++;
            size--;
        }

        if(bit_flag)
            out ^= CRC16;

    }

    int i;
    for (i = 0; i < 16; ++i) {
        bit_flag = out >> 15;
        out <<= 1;
        if(bit_flag)
            out ^= CRC16;
    }

    uint16_t crc = 0;
    i = 0x8000;
    int j = 0x0001;
    for (; i != 0; i >>=1, j <<= 1) {
        if (i & out) crc |= j;
    }

    return crc;
}

void getOppositeFileExtension(int len, bool type_txt, char* new_str) {

	if(!type_txt) {

		new_str[len-3] = 't';
		new_str[len-2] = 'x';
		new_str[len-1] = 't';
	}
	else {

		new_str[len-3] = 'c';
		new_str[len-2] = 'r';
		new_str[len-1] = 'c';	
	}
}

int main(int argc, char* argv[]) {

	bool is_crc = false;
	bool is_txt = false;

	uint16_t crc_result;

	if(argc < 2) {

		fprintf(stderr, "Usage: <filename.txt/filename.crc>\n");
		return 1;
	}
	else if(strstr(argv[1], ".crc") != NULL) {

		is_crc = true;
	}
	else if(strstr(argv[1], ".txt") != NULL) {

		is_txt = true;
	}
	else {

		fprintf(stderr, "Unsupported file format entered!\n");
		return 1;
	}


	int len = strlen(argv[1]);
	char secondary_file[len];
	strcpy(secondary_file, argv[1]);

	getOppositeFileExtension(len, is_txt, secondary_file);

	FILE *read_file = fopen(argv[1], "r+");
	FILE *write_file = fopen(secondary_file, "w+");
	uint16_t crc_writable[1];

	if(is_txt) {

		fseek(read_file, 0, SEEK_END);
		long fsize = ftell(read_file);
		fseek(read_file, 0, SEEK_SET);  //same as rewind(f);

		char *string = malloc(fsize + 1);

		fread(string, fsize, 1, read_file);

		string[fsize] = 0;

		crc_result = crc_msb_first(string, fsize);
		crc_writable[0] = crc_result;


														//used lsb_first
		//------------------------------------------_BRUTE FORCING EQUAL STRINGS_---------------------------------------------------------
		// char *string2 = malloc(fsize+1);
		// string2[fsize] = 0;
		// int idx = 0;

		// memcpy(string2, string, fsize);

		// string2[0] = 'A';

		// while(crc_lsb_first(string2, fsize) != crc_lsb_first(string, fsize) && strstr(string, string2) == NULL ) {

		// 	if(idx >= fsize || idx < 0) 
		// 		idx = 0;

		// 	printf("%c\n", string2[idx]);

		// 	string2[idx] += 1;

		// 	printf("%c\n", string2[idx]);

		// 	idx++;

		// }

		// printf("%s\n", string2);

		// if(fwrite(string2, 1, fsize, write_file) == 0) {

		// 	fprintf(stderr, "Error while writing file content\n");
		// }

		// printf("%x\n", crc_result);
		//------------------------------------------_BRUTE FORCING EQUAL STRINGS_---------------------------------------------------------


		if(fwrite(crc_writable, 1, 2, write_file) == 0) {

			fprintf(stderr, "Error while adding CRC16-IBM\n");
		}

		if(fwrite(string, 1, fsize, write_file) == 0) {

			fprintf(stderr, "Error while writing file content\n");
		}
	}
	else if(is_crc) {

		fread(crc_writable, 1, 2, read_file);


		fseek(read_file, 0, SEEK_END);
		long fsize = ftell(read_file);
		fseek(read_file, 0, SEEK_SET);  //same as rewind(f);

		char *string = malloc(fsize + 1);

		fread(string, fsize, 1, read_file);

		string[fsize] = 0;

		//first 2 bytes are CRC
		string++;
		string++;

		if(crc_msb_first(string, fsize-2) == crc_writable[0]) {

			fprintf(stdout, "GJ! Passed CRC check!\n");

			if(fwrite(string, 1, fsize-2, write_file) == 0) {

				fprintf(stderr, "Error while writing file content\n");
			}
		}
		else {

			fprintf(stderr, "CRC16-IBM check failed!\n");
			return 1;
		}
	}

	
	//if we are here, everything is fine, delete old file
	remove(argv[1]);
	
	fclose(write_file);
	fclose(read_file);

	return 0;
}