package generator

import errors.generator.UnknownVariableException
import generator.types.TypeDescriptor
import kotlin.math.absoluteValue

class ASMBuilder(private val asmBuffer: StringBuilder) {
    private val stack = VirtualStackDescriptor()
    private val lookupTable = VariableLookupTable(stack)

    fun stackFrameStart() {
        this.push("rbp")
        this.mov("rbp", "rsp")
    }

    fun stackFrameEnd() {
        this.mov("rsp", "rbp")
        this.append("ret")
    }

    fun memcpy(offsetTo: Int, offsetFrom: Int, bytes: UInt) {
        this.lea("rdi", "rsp", offsetTo)
        this.lea("rsi", "rsp", offsetFrom)
        this.mov("rdx", bytes.toString())
        this.call("memcpy")
    }

    fun call(name: String) {
        this.append("call $name")
    }

    fun lea(into: String, from: String, offset: Int) {
        this.append("lea $into, ${pointerWithOffset(from, offset)}")
    }

    fun growStack(bytes: UInt) {
        this.sub("rsp", bytes.toString())
        this.stack.grow(bytes)
    }

    fun shrinkStack(bytes: UInt) {
        this.add("rsp", bytes.toString())
        this.stack.shrink(bytes)
    }

    fun getVariable(name: String): Pair<UInt, TypeDescriptor> {
        if (!this.lookupTable.doesVariablesExits(name))
            throw UnknownVariableException(name)

        return this.lookupTable.getVariable(name)
    }

    fun registerVariable(name: String, type: TypeDescriptor) {
        this.lookupTable.registerNewVariable(name, type)
    }

    private fun stackSize(): UInt = this.stack.getCurrentStackSize()

    fun offsetToVariable(variable: Pair<UInt, TypeDescriptor>): Int {
        return this.stackSize().toInt() - variable.first.toInt()
    }

    fun pointerWithOffset(from: String, offset: Int, size: MemorySizes? = null): String {
        val sizeString = size?.toString() ?: ""
        return if (offset >= 0) {
            "$sizeString [$from + ${offset.absoluteValue}]"
        } else {
            "$sizeString [$from - ${offset.absoluteValue}]"
        }
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

    fun add(into: String, from: String) {
        this.append("add $into, $from")
    }

    fun sub(into: String, from: String) {
        this.append("sub $into, $from")
    }

    fun imul(into: String, from: String) {
        this.append("imul $into, $from")
    }

    fun div(from: String) {
        this.append("div $from")
    }

    fun xor(into: String, from: String) {
        this.append("xor $into, $from")
    }

    fun append(asmLine: String) {
        this.asmBuffer.append('\t').appendLine(asmLine)
    }

    override fun toString(): String = this.asmBuffer.toString()
    fun appendWithoutIndent(text: String) {
        this.asmBuffer.appendLine(text)
    }
}