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

int main(int argc, char *argv[])
{
    //initialize variables, use good old web-developer favourite 8080
    int listenfd = 0, connfd = 0, port = 8080;
    struct sockaddr_in serv_addr; 

    //le random message I used to try sending data in both directions
    char* sendBuff = "Hello there stranger! I recieved your message!";
    char recvBuff[300];

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
    listen(listenfd, 10);
    fprintf(stdout, "Server running on port 8080.\n");

    //never stop server
    while(1) {
        
        //accept user connection
        connfd = accept(listenfd, (struct sockaddr*)NULL, NULL); 
       
        //initialize the recieve buffer with spaces, so we don't print too much garbage       
        memset(recvBuff, ' ', sizeof(serv_addr)); 
        fprintf(stdout, "Client connected\n");

        //recieve message from client in initialized buffer
        if(recv(connfd, recvBuff, sizeof(recvBuff)-1, 0) < 0) {

            fprintf(stderr, "recieve failed\n");
        }
        else {

            printf("Message from client: %s\n", recvBuff);
        }

        //send back message to client
        if( send(connfd , sendBuff , strlen(sendBuff) , 0) < 0) {
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