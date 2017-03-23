#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <netdb.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <arpa/inet.h> 
#include <stdbool.h>
#include <sys/un.h>

int main(int argc, char *argv[]) {
   
    //initialize variables
    int sockfd = 0;
    char recvBuff[50];
    char* sendBuff;
    struct sockaddr_in serv_addr;
    struct sockaddr_un serv_path;

    int main_type = AF_INET;
    int type = 0;
    int additional_argument = 0;
    bool unix = false;


    if(argc < 2) {

        fprintf(stdout, "\n Usage: %s <server_path> -<socket type> <message>\n", argv[0]);
        return EXIT_FAILURE;
    }
    else if(strcmp(argv[1], "-u") == 0) {

    	type =  SOCK_DGRAM;
    	additional_argument = IPPROTO_UDP;

    }
    else if(strcmp(argv[1], "-t") == 0) {

    	type = SOCK_STREAM;
    }
    else if(strcmp(argv[1], "-U") == 0) {

    	main_type = AF_UNIX;
    	type = SOCK_STREAM;
    	unix = true;
    }

    if(argc >= 3) {

	    sendBuff = argv[3];
    }
    else {

    	sendBuff = "No message given!";
    }

    //server code commented better, it is the same anyways

    memset(recvBuff, ' ',sizeof(recvBuff));

    memset(&serv_addr, '0', sizeof(serv_addr)); 

    if(!unix) { 

		//create socket
	    if((sockfd = socket(main_type, type, additional_argument)) < 0){
	        fprintf(stderr, "Error while creating socket!\n");
	        return EXIT_FAILURE;
	    } 

	    //add configs
	    serv_addr.sin_family = AF_INET;
	    serv_addr.sin_port = htons(8080); 
    
	    //bind configs
	    if(inet_pton(AF_INET, argv[2], &serv_addr.sin_addr)<=0){
	        fprintf(stderr, "\n inet_pton error occured\n");
	        return 1;
	    } 

	    //connect to socket
	    if( connect(sockfd, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) < 0){
	       fprintf(stderr, "\n Error : Connect Failed \n");
	       return 1;
	    }
	}
	else {

		//create socket
	    if((sockfd = socket(AF_UNIX, SOCK_STREAM, 0)) < 0){
	        fprintf(stderr, "Error while creating socket!\n");
	        return EXIT_FAILURE;
	    } 

		serv_path.sun_family = AF_UNIX;
		strcpy(serv_path.sun_path, argv[2]);

		if (connect(sockfd, (struct sockaddr *) &serv_path, sizeof(struct sockaddr_un)) < 0) {
			fprintf(stderr, "Error while creating socket!\n");
	        return EXIT_FAILURE;
		}

		printf("Connected to server!\n");
	}


	if(!unix) {

		//send data
	    if( send(sockfd , sendBuff , strlen(sendBuff) , 0) < 0) {
	        fprintf(stderr, "Sending failed\n");
	        return 1;
	    }
	    fprintf(stdout, "Message sent\n");

	    //recieve data
	    if(recv(sockfd, recvBuff, sizeof(recvBuff)-1, 0) < 0) {

	        fprintf(stderr, "recieve failed\n");
	    }
	    else {

	        fprintf(stdout, "Server response: %s\n", recvBuff);
	    }
	}
	else {

		if( write(sockfd , sendBuff , strlen(sendBuff)) < 0) {
	        fprintf(stderr, "Sending failed\n");
	        return 1;
	    }
	    fprintf(stdout, "Message sent\n");

	    if(read(sockfd, recvBuff, sizeof(recvBuff)) < 0) {

	    	fprintf(stderr, "recieve failed\n");
	    }
	    else {

	        fprintf(stdout, "Server response: %s\n", recvBuff);
	    }
	}

    close(sockfd);
    return 0;
}