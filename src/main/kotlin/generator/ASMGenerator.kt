package generator

import parser.nodes.Expression
import parser.nodes.NumberLiteralExpressionNode
import parser.statements.ExitStatement
import parser.statements.Statement

class ASMGenerator(private val statements: List<Statement>) {
    private val asmBuilder = ASMBuilder(StringBuilder())

    fun generateAssembly(): String {
        this.asmBuilder.appendWithoutIndent("global _start")
        this.asmBuilder.appendWithoutIndent("_start:")
        this.asmBuilder.stackFrameStart()

        for (statement in this.statements) {
            asmBuilder.append("; $statement")
            statement.toAssembly(asmBuilder)
            asmBuilder.append("")
        }

        // Add exit with code 0 so that it exits with 0 if there was no other exit before that
        ExitStatement(Expression(NumberLiteralExpressionNode("0"))).toAssembly(asmBuilder)
        this.asmBuilder.stackFrameEnd()

        this.addBuildInFunctions()

        return asmBuilder.toString()
    }

    private fun addBuildInFunctions() {
        this.asmBuilder.append("")

        this.asmBuilder.appendWithoutIndent(this.memcpyFunction())
    }

    private fun memcpyFunction(): String = """
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
""".trimIndent()
}