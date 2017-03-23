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

int main(int argc, char *argv[]) {
   
    //initialize variables
    int sockfd = 0;
    char recvBuff[50];
    char* sendBuff;
    struct sockaddr_in serv_addr; 

    if(argc < 3) {

        fprintf(stdout, "\n Usage: %s <server_path> <message>\n",argv[0]);
        return EXIT_FAILURE;
    }

    //server code commented better, it is the same anyways

    sendBuff = argv[2];

    memset(recvBuff, ' ',sizeof(recvBuff));

    //create socket
    if((sockfd = socket(AF_INET, SOCK_STREAM, 0)) < 0){
        fprintf(stderr, "Error while creating socket!\n");
        return EXIT_FAILURE;
    } 

    memset(&serv_addr, '0', sizeof(serv_addr)); 

    //add configs
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_port = htons(8080); 

    
    //bind configs
    if(inet_pton(AF_INET, argv[1], &serv_addr.sin_addr)<=0){
        fprintf(stderr, "\n inet_pton error occured\n");
        return 1;
    } 

    //connect to socket
    if( connect(sockfd, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) < 0){
       fprintf(stderr, "\n Error : Connect Failed \n");
       return 1;
    }

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

    return 0;
}