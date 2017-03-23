#include "hristov_malloc.h"

int main() {

    memory_print();

    int *arr;
    arr = memory_allocate(100);

    memory_print();
    memory_free(arr);
    memory_print();

    return 0;
}