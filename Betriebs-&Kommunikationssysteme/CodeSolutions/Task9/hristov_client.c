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
    char recvBuff[64];
    char sendBuff[64];
    struct sockaddr_in serv_addr; 

    if(argc < 2) {

        fprintf(stdout, "\n Usage: %s <server_path> \n",argv[0]);
        return EXIT_FAILURE;
    }

    //server code commented better, it is the same anyways

    memset(recvBuff, ' ',sizeof(recvBuff));
    memset(sendBuff, ' ',sizeof(sendBuff));

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

    while(1) {

        fgets(sendBuff, sizeof(sendBuff), stdin);

        if(strlen(sendBuff) >= 1) {

            if( send(sockfd , sendBuff , strlen(sendBuff) , 0) < 0) {
                fprintf(stderr, "Sending failed\n");
                return 1;
            }
        }


        if(recv(sockfd, recvBuff, sizeof(recvBuff)-1, 0) < 0) {

            fprintf(stderr, "recieve failed\n");
        }
        else {
            fprintf(stdout, "%s\n", recvBuff);    
        }

        sleep(1);
    }

    return 0;
}