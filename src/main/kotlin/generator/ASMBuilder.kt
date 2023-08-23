package generator

import errors.generator.UnknownVariableException

class ASMBuilder(private val asmBuffer: StringBuilder) {
    private val stack = VirtualStackDescriptor()
    val lookupTable = VariableLookupTable(stack)

    fun init() {
        this.asmBuffer.appendLine("global _start")
        this.asmBuffer.appendLine("_start:")
    }

    fun stackAddrWithOffset(offset: UInt): String = "QWORD [rsp + $offset]"

    fun getVariableOffset(name: String): UInt {
        if (!this.lookupTable.doesVariablesExits(name))
            throw UnknownVariableException(name)

        return this.stack.getCurrentStackSize() - this.lookupTable.getVariablePosition(name)
    }

    fun mov(into: String, from: String) = this.append("mov $into, $from")

    fun pop(into: String) {
        this.append("pop $into")
        this.stack.popNumber()
    }

    fun push(from: String) {
        this.append("push $from")
        this.stack.pushNumber()
    }

    fun append(asmLine: String) {
        this.asmBuffer.append('\t').appendLine(asmLine)
    }

    override fun toString(): String = this.asmBuffer.toString()
}