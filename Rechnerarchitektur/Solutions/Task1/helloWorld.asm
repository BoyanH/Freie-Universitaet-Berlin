;Übungsgruppe: Boyan Hristov, Nedeltscho Petrov
;Übung: Do, Hoang Ha; Donnerstag; 12:00 – 14: 00


section     .text
global      _start                              ;must be defined for linker (ld)

_start:                                         ;set entry point for linker

    mov     edx,len                             ;set message length
    mov     ecx,msg                             ;declare the message to be written
    mov     ebx,1                               ;file descriptor (stdout)
    mov     eax,4                               ;system call number (sys_write)
    int     0x80                                ;call kernel

    mov     eax,1                               ;system call number (sys_exit)
    int     0x80                                ;call kernel

section     .data

msg     db  'Hello, world!',0xa                 ;say hello to the world
len     equ $ - msg                             ;length of our string
