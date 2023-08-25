global _start
_start:
    ; memory cheaper but longer version (can use up to 7 bytes less memory)
    sub rsp, 1
    mov BYTE [rsp], `\n`

    sub rsp, 2
    mov WORD [rsp], `mn`

    sub rsp, 4
    mov DWORD [rsp], `ijkl`

	mov rax, `abcdefgh`
	push rax

    mov rax, 1
    mov rdi, 1
    lea rsi, [rsp]
    mov rdx, 15
    syscall

    ; memory expensive but shorter version (can use up to 7 byte more memory)
    mov rax, `ijklmn\n`
    push rax

    mov rax, `abcdefgh`
    push rax

    mov rax, 1
    mov rdi, 1
    lea rsi, [rsp]
    mov rdx, 15
    syscall

	mov rax, 60
	mov rdi, 0
	syscall
