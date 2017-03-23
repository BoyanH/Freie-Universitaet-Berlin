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
#include <sys/select.h>

#define INVALID_SOCKET -1
#define SOCKET_ERROR 1
#define CLIENTS_COUNT 8

int main(int argc, char *argv[])
{

	int clients[CLIENTS_COUNT];

	//initialize variables, use good old web-developer favourite 8080
	int listenfd = 0, port = 8080, rc = 0, nfds = 0;
	struct sockaddr_in serv_addr; 

	fd_set read_fds;

	char recvBuff[64];

	//initialize the socket
	listenfd = socket(AF_INET, SOCK_STREAM, 0);
	memset(&serv_addr, '0', sizeof(serv_addr));

	if(argc < 2) {

		fprintf(stdout, "\n Usage: %s <server_path> \n",argv[0]);
		return EXIT_FAILURE;
	}

	//configure address and port
	serv_addr.sin_family = AF_INET;
	serv_addr.sin_addr.s_addr = inet_addr(argv[1]);
	serv_addr.sin_port = htons(port); 

	//bind socket to our configuration
	bind(listenfd, (struct sockaddr*)&serv_addr, sizeof(serv_addr)); 

	//start lstening for connections
	rc = listen(listenfd, 10);
	fprintf(stdout, "Server running on port 8080.\n");

	for(int i = 0; i<CLIENTS_COUNT; i++) {

		clients[i] = INVALID_SOCKET;
	}

	//never stop server
	while(1) {
		
		FD_ZERO(&read_fds); // Inhalt leeren
		FD_SET(listenfd,&read_fds); // Den Socket der verbindungen annimmt hinzufügen

		for(int i=0;i<CLIENTS_COUNT;i++) {

		  if(clients[i]!=INVALID_SOCKET) {
			
			if(clients[i] > nfds)
				nfds = clients[i];

			FD_SET(clients[i],&read_fds);
		  }
		}

		//why + 4? WHY DOES IT WORK LIKE THAT?!? WUT?!? 
		//SHOULD BE +1
		if(select(nfds+4,&read_fds,NULL,NULL,NULL) < 1) {

			fprintf(stderr, "Error selecting sockets\n");
		}

		if(FD_ISSET(listenfd,&read_fds)) {

		  // einen freien platz für den neuen client suchen, und die verbingung annehmen
		  for(int i=0;i<CLIENTS_COUNT;i++) {
			if(clients[i]==INVALID_SOCKET) {
			
				clients[i]=accept(listenfd,NULL,NULL);
				printf("New chatter connected (%d)\n",i);
				break;
			}
		  }
		}

		for(int i=0;i<CLIENTS_COUNT;i++) {

			if(clients[i]==INVALID_SOCKET) {

				continue; // ungültiger socket, d.h. kein verbunder client an dieser position im array
			}
			if(FD_ISSET(clients[i],&read_fds)) {
			
			rc=recv(clients[i],recvBuff,sizeof(recvBuff),0);
			// prüfen ob die verbindung geschlossen wurde oder ein fehler auftrat
			
			if(rc==0 || rc==SOCKET_ERROR) {

				printf("Client %d leaved the chat room\n",i);
				close(clients[i]); // socket schliessen 
				clients[i]=INVALID_SOCKET; // seinen platz wieder freigeben
			} 
			else {
			  
				recvBuff[rc]='\0';
				// daten ausgeben und eine antwort senden
				printf("Client %d wrote: %s\n",i,recvBuff);
			  
			  

				for(int j = 0; j < CLIENTS_COUNT; j++) {

					if(j != INVALID_SOCKET && i != j) {

						send(clients[j],recvBuff,(int)strlen(recvBuff),0);
					}
				}

			}
		  }
		}
		sleep(1);
	 }
}