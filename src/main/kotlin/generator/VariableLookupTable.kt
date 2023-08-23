package generator

class VariableLookupTable(private val stack: VirtualStackDescriptor) {
    private val variableToStackPointer: HashMap<String, UInt> = HashMap()

    fun doesVariablesExits(name: String): Boolean = variableToStackPointer.containsKey(name)

    fun registerNewVariable(name: String) {
        this.variableToStackPointer[name] = this.stack.getCurrentStackSize()
    }

    fun getVariablePosition(name: String): UInt {
        return this.variableToStackPointer[name]!!
    }
}