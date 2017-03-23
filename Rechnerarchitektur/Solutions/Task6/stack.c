#include <stdio.h>
#include <stdint.h>

extern void init();
extern void push(int64_t);
extern int64_t pop();
extern int64_t top();
extern int64_t isEmpty();

int main(int argc, char const *argv[])
{
    init();
    int64_t v  = 0x123456789abcdef0;
    int64_t v1 = 0xfedcba9876543210;
    int64_t v2 = 0x13579bdf2468ace0;
    int64_t v3 = 0x2468ace013579bdf;
    int64_t v4 = 0x133700dead00beef;
    printf("Operation |      Result      |     Expected\n");
    printf("----------+------------------+-----------------\n");
    printf("IS_EMPTY  | %16lx | %16lx\n", isEmpty(), 1);
    printf("PUSH      |                - | %16lx\n", v, v);
    push(v);
    printf("TOP       | %16lx | %16lx\n", top(), v);
    printf("IS_EMPTY  | %16lx | %16lx\n", isEmpty(), 0);
    printf("POP       | %16lx | %16lx\n", pop(), v);
    printf("IS_EMPTY  | %16lx | %16lx\n", isEmpty(), 1);
    printf("PUSH      |                - | %16lx\n", v1, v1);
    push(v1);
    printf("PUSH      |                - | %16lx\n", v2, v2);
    push(v2);
    printf("PUSH      |                - | %16lx\n", v3, v3);
    push(v3);
    printf("TOP       | %16lx | %16lx\n", top(), v3);
    printf("IS_EMPTY  | %16lx | %16lx\n", isEmpty(), 0);
    printf("POP       | %16lx | %16lx\n", pop(), v3);
    printf("PUSH      |                - | %16lx\n", v4, v4);
    push(v4);
    printf("TOP       | %16lx | %16lx\n", top(), v4);
    printf("POP       | %16lx | %16lx\n", pop(), v4);
    printf("POP       | %16lx | %16lx\n", pop(), v2);
    printf("TOP       | %16lx | %16lx\n", top(), v1);
    printf("POP       | %16lx | %16lx\n", pop(), v1);
    return 0;
}
