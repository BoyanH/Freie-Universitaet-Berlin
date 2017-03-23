#include <stdio.h>
#include <stdlib.h>

extern float floatAdd(float, float);
extern float floatSub(float, float);


float main(int argc, char* argv[]) {
    
    float float1 = 0;
    float float2 = 0;
    
    if(argc < 3) {
        puts("Enter 2 floats to add and the subtract them. \n Float1: ");
        scanf("%f", &float1);
        puts("Float2: ");
        scanf("%f", &float2);
    } else {
        float1 = atof(argv[1]);
        float2 = atof(argv[2]);
    }

    float addResult  = floatAdd(float1, float2);
    float subResult = floatSub(float1, float2);

    printf("%f + %f = %f\n", float1, float2, addResult);
    printf("%f - %f = %f\n", float1, float2, subResult);

    return 0;
}

