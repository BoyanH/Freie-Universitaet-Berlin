#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <sys/types.h>
#include <time.h> 
#include <stdbool.h>
#include <sys/un.h>

int main(int argc, char *argv[])
{
	//initialize variables, use good old web-developer favourite 8080
	int listenfd = 0, connfd = 0, port = 8080;
	struct sockaddr_un serv_path;
	struct sockaddr_in serv_addr;
	struct sockaddr addr_other;
	socklen_t addr_other_len = sizeof(addr_other); 

	//le random message I used to try sending data in both directions
	char* sendBuff = "Hello there stranger! I recieved your message!";
	char recvBuff[300];
	int message_error_code = 0;

	int type = 0;
	int additional_argument = 0;
	bool tcp = false;
	bool udp = false;
	bool unix = false;

	if(argc < 2) {

		fprintf(stdout, "\n Usage: %s <server_path> -<socket type> <message>\n", argv[0]);
		return EXIT_FAILURE;
	}
	else if(strcmp(argv[1], "-u") == 0) {

		type =  SOCK_DGRAM;
		additional_argument = IPPROTO_UDP;
		udp = true;

	}
	else if(strcmp(argv[1], "-t") == 0) {

		type = SOCK_STREAM;
		tcp = true;
	}
	else if(strcmp(argv[1], "-U") == 0) {

		unix = true;
	}

	if(argc >= 3) {

		sendBuff = argv[3];
	}
	else {

		sendBuff = "No message given!";
	}


	memset(&serv_addr, '0', sizeof(serv_addr));
	if(!unix) {
		//initialize the socket
		listenfd = socket(AF_INET, type, additional_argument);

		//configure address and port
		serv_addr.sin_family = AF_INET;
		serv_addr.sin_addr.s_addr = inet_addr(argv[2]);
		serv_addr.sin_port = htons(port); 

		//bind socket to our configuration
		bind(listenfd, (struct sockaddr*)&serv_addr, sizeof(serv_addr)); 

		//start lstening for connections
		listen(listenfd, 10);
		fprintf(stdout, "Server running on port 8080.\n");
	}
	else {

		listenfd = socket(AF_UNIX, SOCK_STREAM, 0);

		if(listenfd < 0) {

			fprintf(stderr, "Failed to open socket!\n");
			return EXIT_FAILURE;
		}

		serv_path.sun_family = AF_UNIX;
		strcpy(serv_path.sun_path, argv[2]);

		if (bind(listenfd, (struct sockaddr *) &serv_path, sizeof(struct sockaddr_un))) {
		
			fprintf(stderr, "Error binding socket!\n");	
			return EXIT_FAILURE;
		}

		listen(listenfd, 5);
		fprintf(stdout, "Unix domain socket server running with path: %s\n", argv[2]);
	}

	//never stop server
	while(1) {

		//initialize the recieve buffer with spaces, so we don't print too much garbage  		
		memset(recvBuff, ' ', sizeof(recvBuff));

		if(tcp) {
			//accept user connection
			connfd = accept(listenfd, (struct sockaddr*)NULL, NULL); 
      
			fprintf(stdout, "Client connected\n");
			message_error_code = recv(connfd, recvBuff, sizeof(recvBuff)-1, 0);
		}
		else if(udp) {

			message_error_code = recvfrom(listenfd, recvBuff, sizeof(recvBuff), 0, &addr_other, &addr_other_len);
		}
		else if(unix) {

			connfd = accept(listenfd, (struct sockaddr*)NULL, NULL);
			message_error_code = read(connfd, recvBuff, sizeof(recvBuff));
		}

		//recieve message from client in initialized buffer
		if(message_error_code < 0) {

			fprintf(stderr, "recieve failed\n");
		}
		else {

			printf("Message from client: %s\n", recvBuff);
		}

		if(tcp) {

			message_error_code = send(connfd , sendBuff , strlen(sendBuff) , 0);
		}
		else if (udp) {

			message_error_code = sendto(listenfd, sendBuff, sizeof(sendBuff), 0, (struct sockaddr*) &addr_other, addr_other_len);
		}
		else if(unix) {

			message_error_code = write(connfd, sendBuff, sizeof(sendBuff));
		}


		//send back message to client
		if( message_error_code < 0) {
			fprintf(stderr, "Send failed");
			return 1;
		}
		fprintf(stdout, "Data Send\n");

		//close connection
		close(connfd);

		//sleep, so we don't get too much CPU usage
		sleep(1);
	 }
}