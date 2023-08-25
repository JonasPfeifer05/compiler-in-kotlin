global _start
_start:
    ; add 64bit integer to stack
    mov rax, "abcdefgh"
    push rax

    ; reserve place for 64bit integer on the stack
    mov rax, "xxxxxxxx"
    push rax

    ; dst addr
    mov rdi, rsp

    ; src addr
    lea rsi, [rsp + 8]

    ; amount of bytes to copy
    mov rdx, 2

    ; call memcpy(dst, src, len)
    call memcpy

    ; print 8 characters stored from the stack top
    mov rax, 1
    mov rdi, 1
    mov rdx, 8
    mov rsi, rsp
    syscall

    ; exit with code 0
    mov rax, 60
    mov rdi, 0
    syscall

memcpy:
    ; initializing stack frame
    push rbp
    mov rbp, rsp

    ; initialize offset with 0
    mov rcx, 0

memcpy_while:
    ; check if there are remaining bytes to copy
    cmp rcx, rdx
    je memcpy_end

    ; Copy the memory with offset
    mov al, BYTE [rsi+rcx]
    mov BYTE [rdi+rcx], al

    ; increase offset
    add rcx, 1

    jmp memcpy_while
memcpy_end:

    ; restoring old stack frame
    mov rsp, rbp
    pop rbp

    ret
